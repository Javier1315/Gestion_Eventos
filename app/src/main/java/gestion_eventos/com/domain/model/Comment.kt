package gestion_eventos.com.domain.model

data class Comment(
    val id: String,
    val eventId: String,
    val userId: String,
    val userName: String,
    val message: String,
    val createdAtMillis: Long
)
