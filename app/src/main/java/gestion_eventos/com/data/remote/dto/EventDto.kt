package gestion_eventos.com.data.remote.dto

data class EventDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val dateTimeMillis: Long = 0L,
    val location: String = "",
    val organizerId: String = "",
    val category: String = "ACADEMIC",
    val imageUrl: String? = null,
    val isPastEvent: Boolean = false,
    val isCanceled: Boolean = false,
    val canceledAt: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
