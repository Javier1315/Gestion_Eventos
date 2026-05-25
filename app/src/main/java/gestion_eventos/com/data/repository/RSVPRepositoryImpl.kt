package gestion_eventos.com.data.repository

import gestion_eventos.com.data.mapper.toDomain
import gestion_eventos.com.data.mapper.toRequestDto
import gestion_eventos.com.data.remote.api.ApiResponseHandler
import gestion_eventos.com.data.remote.api.ApiService
import gestion_eventos.com.data.remote.api.RetrofitClient
import gestion_eventos.com.domain.model.RSVP
import gestion_eventos.com.domain.model.RSVPStatus
import gestion_eventos.com.domain.repository.RSVPRepository

class RSVPRepositoryImpl(
    private val apiService: ApiService = RetrofitClient.apiService
) : RSVPRepository {
    override suspend fun confirmAttendance(eventId: String, userId: String): RSVP {
        return updateStatus(eventId, userId, RSVPStatus.CONFIRMED)
    }

    override suspend fun updateStatus(eventId: String, userId: String, status: RSVPStatus): RSVP {
        val response = apiService.createRsvp(eventId, status.toRequestDto(userId))
        return ApiResponseHandler.unwrap(response).toDomain()
    }

    override suspend fun getUserRsvps(userId: String): List<RSVP> {
        return ApiResponseHandler.unwrap(apiService.getUserRsvps(userId)).map { it.toDomain() }
    }
}
