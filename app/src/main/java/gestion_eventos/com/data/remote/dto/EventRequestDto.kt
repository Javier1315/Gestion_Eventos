package gestion_eventos.com.data.remote.dto

data class EventRequestDto(
    val title: String,
    val description: String,
    val dateTimeMillis: Long,
    val location: String,
    val organizerId: String,
    val category: String,
    val imageUrl: String? = null,
    val isPastEvent: Boolean = false,
    val isCanceled: Boolean = false,
    val canceledAt: String? = null
)
