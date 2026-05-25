package gestion_eventos.com.domain.usecase.events

import gestion_eventos.com.domain.model.Event
import gestion_eventos.com.domain.repository.EventRepository

class UpdateEventUseCase(private val repository: EventRepository) {
    suspend operator fun invoke(event: Event) = repository.updateEvent(event)
}
