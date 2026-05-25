package gestion_eventos.com.domain.usecase.events

import gestion_eventos.com.domain.repository.EventRepository

class GetUpcomingEventsUseCase(private val repository: EventRepository) {
    suspend operator fun invoke() = repository.getUpcomingEvents()
}
