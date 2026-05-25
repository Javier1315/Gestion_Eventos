package gestion_eventos.com.domain.model

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val dateTimeMillis: Long,
    val location: String,
    val organizerId: String,
    val category: EventCategory = EventCategory.ACADEMIC,
    val imageUrl: String? = null,
    val isPastEvent: Boolean = false,
    val isCanceled: Boolean = false,
    val canceledAt: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
) : Shareable {
    // Representa un evento y define como puede compartirse.
    override fun shareMessage(): String {
        return "Te invito al evento $title en $location."
    }
}
