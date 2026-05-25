package gestion_eventos.com.domain.model

data class RSVP(
    val id: String,
    val eventId: String,
    val userId: String,
    val status: RSVPStatus,
    val updatedAtMillis: Long
)

enum class RSVPStatus {
    CONFIRMED,
    DECLINED,
    PENDING
}
