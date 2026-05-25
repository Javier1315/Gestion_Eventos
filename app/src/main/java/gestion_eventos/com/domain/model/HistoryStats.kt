package gestion_eventos.com.domain.model

data class HistoryStats(
    // Resume la participacion del usuario para la pantalla de historial.
    val attendedEvents: Int = 0,
    val upcomingEvents: Int = 0,
    val pastEvents: Int = 0,
    val canceledEvents: Int = 0,
    val comments: Int = 0,
    val ratings: Int = 0
)
