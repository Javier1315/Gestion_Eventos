package gestion_eventos.com.data.remote.dto

data class CommentDto(
    val id: String,
    val eventId: String,
    val userId: String,
    val userName: String? = null,
    val message: String,
    val createdAt: String
)
