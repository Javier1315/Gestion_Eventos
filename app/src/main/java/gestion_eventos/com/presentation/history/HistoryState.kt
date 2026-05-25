package gestion_eventos.com.presentation.history

import gestion_eventos.com.domain.model.Event
import gestion_eventos.com.domain.model.HistoryStats

data class HistoryState(
    // Estado de historial, notificaciones y estadisticas del usuario.
    val attendedEvents: List<Event> = emptyList(),
    val stats: HistoryStats = HistoryStats(),
    val isLoading: Boolean = false,
    val error: String? = null
)
