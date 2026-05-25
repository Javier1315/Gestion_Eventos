package gestion_eventos.com.data.remote.dto

data class RatingDto(
    val id: String,
    val eventId: String,
    val userId: String,
    val value: Int,
    val createdAt: String
)
