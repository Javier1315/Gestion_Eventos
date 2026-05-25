package gestion_eventos.com.domain.usecase.rsvp

import gestion_eventos.com.domain.repository.RSVPRepository

class ConfirmAttendanceUseCase(private val repository: RSVPRepository) {
    suspend operator fun invoke(eventId: String, userId: String) {
        repository.confirmAttendance(eventId, userId)
    }
}
