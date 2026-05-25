package gestion_eventos.com.domain.usecase.events

import gestion_eventos.com.domain.repository.EventRepository

class DeleteEventUseCase(private val repository: EventRepository) {
    suspend operator fun invoke(eventId: String) = repository.deleteEvent(eventId)
}
