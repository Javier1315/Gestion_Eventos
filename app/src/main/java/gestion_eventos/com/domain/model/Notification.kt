package gestion_eventos.com.domain.model

data class Notification(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val eventId: String? = null,
    val createdAtMillis: Long,
    val isRead: Boolean = false
)
