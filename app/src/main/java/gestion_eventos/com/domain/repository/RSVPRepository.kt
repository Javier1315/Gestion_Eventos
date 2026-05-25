package gestion_eventos.com.domain.repository

import gestion_eventos.com.domain.model.RSVP
import gestion_eventos.com.domain.model.RSVPStatus

interface RSVPRepository {
    suspend fun confirmAttendance(eventId: String, userId: String): RSVP
    suspend fun updateStatus(eventId: String, userId: String, status: RSVPStatus): RSVP
    suspend fun getUserRsvps(userId: String): List<RSVP>
}
