package gestion_eventos.com.domain.model

data class Rating(
    val id: String,
    val eventId: String,
    val userId: String,
    val value: Int,
    val createdAtMillis: Long
)
