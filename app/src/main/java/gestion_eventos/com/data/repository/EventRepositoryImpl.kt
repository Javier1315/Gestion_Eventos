package gestion_eventos.com.data.repository

import gestion_eventos.com.data.mapper.toDomain
import gestion_eventos.com.data.mapper.toRequestDto
import gestion_eventos.com.data.remote.api.ApiResponseHandler
import gestion_eventos.com.data.remote.api.ApiService
import gestion_eventos.com.data.remote.api.RetrofitClient
import gestion_eventos.com.domain.model.Event
import gestion_eventos.com.domain.repository.EventRepository

class EventRepositoryImpl(
    private val apiService: ApiService = RetrofitClient.apiService
) : EventRepository {
    // Implementa las operaciones de eventos usando la API REST.
    override suspend fun createEvent(event: Event) {
        // Envia el evento al backend para que sea guardado en Firestore.
        ApiResponseHandler.unwrap(apiService.createEvent(event.toRequestDto()))
    }

    override suspend fun updateEvent(event: Event) {
        // Actualiza los datos editables de un evento existente.
        ApiResponseHandler.unwrap(apiService.updateEvent(event.id, event.toRequestDto()))
    }

    override suspend fun getUpcomingEvents(): List<Event> {
        // La app oculta eventos pasados o cancelados en la pantalla principal.
        return ApiResponseHandler.unwrap(apiService.getEvents())
            .map { it.toDomain() }
            .filter { !it.isPastEvent && !it.isCanceled }
    }

    override suspend fun getPastEvents(): List<Event> {
        // Se usa para historial y consultas donde los cancelados tambien importan.
        return ApiResponseHandler.unwrap(apiService.getEvents())
            .map { it.toDomain() }
            .filter { it.isPastEvent || it.isCanceled }
    }

    override suspend fun getEventById(eventId: String): Event? {
        return ApiResponseHandler.unwrap(apiService.getEventById(eventId)).toDomain()
    }

    override suspend fun deleteEvent(eventId: String) {
        // DELETE en la API funciona como cancelacion logica, no borrado definitivo.
        val response = apiService.deleteEvent(eventId)
        if (!response.isSuccessful) {
            throw IllegalStateException("Error ${response.code()}: ${response.message()}")
        }
    }
}
