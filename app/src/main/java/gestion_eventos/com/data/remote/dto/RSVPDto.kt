package gestion_eventos.com.data.remote.dto

data class RSVPDto(
    val id: String,
    val eventId: String,
    val userId: String,
    val status: String,
    val updatedAt: String
)
