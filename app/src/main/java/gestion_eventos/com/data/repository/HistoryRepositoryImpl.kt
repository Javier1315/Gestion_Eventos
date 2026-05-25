package gestion_eventos.com.data.repository

import gestion_eventos.com.data.mapper.toDomain
import gestion_eventos.com.data.remote.api.ApiResponseHandler
import gestion_eventos.com.data.remote.api.ApiService
import gestion_eventos.com.data.remote.api.RetrofitClient
import gestion_eventos.com.domain.model.Event
import gestion_eventos.com.domain.model.HistoryStats
import gestion_eventos.com.domain.repository.EventRepository
import gestion_eventos.com.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val apiService: ApiService = RetrofitClient.apiService,
    private val eventRepository: EventRepository = EventRepositoryImpl(apiService)
) : HistoryRepository {
    // Consulta historial y estadisticas desde la API.
    override suspend fun getAttendedEvents(userId: String): List<Event> {
        return ApiResponseHandler.unwrap(apiService.getHistory(userId)).map { it.toDomain() }
    }

    override suspend fun getCreatedEvents(organizerId: String): List<Event> {
        return eventRepository.getPastEvents().filter { it.organizerId == organizerId }
    }

    override suspend fun getParticipationCount(userId: String): Int {
        return ApiResponseHandler.unwrap(apiService.getStats(userId)).attendedEvents
    }

    override suspend fun getStats(userId: String): HistoryStats {
        val stats = ApiResponseHandler.unwrap(apiService.getStats(userId))
        return HistoryStats(
            attendedEvents = stats.attendedEvents,
            upcomingEvents = stats.upcomingEvents,
            pastEvents = stats.pastEvents,
            canceledEvents = stats.canceledEvents,
            comments = stats.comments,
            ratings = stats.ratings
        )
    }
}
