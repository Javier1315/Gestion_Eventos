package gestion_eventos.com.data.local.entity

data class EventEntity(
    val id: String,
    val title: String,
    val description: String,
    val dateTimeMillis: Long,
    val location: String,
    val organizerId: String,
    val category: String,
    val imageUrl: String?,
    val isPastEvent: Boolean,
    val isCanceled: Boolean,
    val canceledAt: String?,
    val createdAt: String?,
    val updatedAt: String?
)
