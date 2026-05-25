package gestion_eventos.com.domain.usecase.history

import gestion_eventos.com.domain.repository.HistoryRepository

class GetAttendedEventsUseCase(private val repository: HistoryRepository) {
    suspend operator fun invoke(userId: String) = repository.getAttendedEvents(userId)
}
