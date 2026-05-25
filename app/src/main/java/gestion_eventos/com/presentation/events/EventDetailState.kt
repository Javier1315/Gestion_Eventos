package gestion_eventos.com.presentation.events

import gestion_eventos.com.domain.model.Comment
import gestion_eventos.com.domain.model.Rating

data class EventDetailState(
    // Estado del detalle con RSVP, comentarios y calificaciones.
    val isLoading: Boolean = false,
    val isSavingRsvp: Boolean = false,
    val isSavingComment: Boolean = false,
    val isSavingRating: Boolean = false,
    val isRsvpConfirmed: Boolean = false,
    val userRating: Int = 0,
    val comments: List<Comment> = emptyList(),
    val ratings: List<Rating> = emptyList(),
    val commentVersion: Int = 0,
    val error: String? = null,
    val successMessage: String? = null
) {
    val averageRating: Double
        get() = if (ratings.isEmpty()) 0.0 else ratings.map { it.value }.average()
}
