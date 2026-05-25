package gestion_eventos.com.data.remote.dto

data class StatsDto(
    val userId: String,
    val attendedEvents: Int,
    val upcomingEvents: Int = 0,
    val pastEvents: Int = 0,
    val canceledEvents: Int = 0,
    val comments: Int,
    val ratings: Int
)
