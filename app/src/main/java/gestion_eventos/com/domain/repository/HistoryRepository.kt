package gestion_eventos.com.domain.repository

import gestion_eventos.com.domain.model.Event
import gestion_eventos.com.domain.model.HistoryStats

interface HistoryRepository {
    // Define consultas de historial y estadisticas del usuario.
    suspend fun getAttendedEvents(userId: String): List<Event>
    suspend fun getCreatedEvents(organizerId: String): List<Event>
    suspend fun getParticipationCount(userId: String): Int
    suspend fun getStats(userId: String): HistoryStats
}
