package gestion_eventos.com.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gestion_eventos.com.data.repository.CommentRepositoryImpl
import gestion_eventos.com.data.repository.RSVPRepositoryImpl
import gestion_eventos.com.domain.model.Comment
import gestion_eventos.com.domain.model.Rating
import gestion_eventos.com.domain.model.RSVPStatus
import gestion_eventos.com.domain.repository.CommentRepository
import gestion_eventos.com.domain.repository.RSVPRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val rsvpRepository: RSVPRepository = RSVPRepositoryImpl(),
    private val commentRepository: CommentRepository = CommentRepositoryImpl()
) : ViewModel() {
    // Administra RSVP, comentarios y calificaciones del detalle del evento.
    private val _state = MutableStateFlow(EventDetailState())
    val state: StateFlow<EventDetailState> = _state.asStateFlow()

    fun loadEventData(eventId: String, userId: String) {
        // Carga comentarios, calificaciones y RSVP en una sola operacion de pantalla.
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            runCatching {
                // Se revisa RSVP del usuario para mostrar si ya confirmo asistencia.
                val comments = commentRepository.getCommentsByEvent(eventId)
                val ratings = commentRepository.getRatingsByEvent(eventId)
                val rsvps = rsvpRepository.getUserRsvps(userId)
                Triple(comments, ratings, rsvps.any { it.eventId == eventId && it.status == RSVPStatus.CONFIRMED })
            }.onSuccess { (comments, ratings, isConfirmed) ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        comments = comments,
                        ratings = ratings,
                        isRsvpConfirmed = isConfirmed,
                        userRating = ratings.firstOrNull { rating -> rating.userId == userId }?.value ?: 0
                    )
                }
            }.onFailure { throwable ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: "No se pudo cargar el detalle del evento."
                    )
                }
            }
        }
    }

    fun confirmAttendance(eventId: String, userId: String) {
        // Envia RSVP confirmado y bloquea el boton cuando ya existe.
        viewModelScope.launch {
            _state.update { it.copy(isSavingRsvp = true, error = null, successMessage = null) }

            runCatching { rsvpRepository.confirmAttendance(eventId, userId) }
                .onSuccess {
                    _state.update {
                        it.copy(
                            isSavingRsvp = false,
                            isRsvpConfirmed = true,
                            successMessage = "Asistencia confirmada."
                        )
                    }
                }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(
                            isSavingRsvp = false,
                            error = throwable.message ?: "No se pudo confirmar asistencia."
                        )
                    }
                }
        }
    }

    fun addComment(eventId: String, userId: String, userName: String, message: String) {
        // Evita enviar comentarios vacios al backend.
        if (message.isBlank()) {
            _state.update { it.copy(error = "Escribe un comentario.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSavingComment = true, error = null, successMessage = null) }

            val comment = Comment(
                // El backend genera el id definitivo del comentario.
                id = "",
                eventId = eventId,
                userId = userId,
                userName = userName,
                message = message.trim(),
                createdAtMillis = System.currentTimeMillis()
            )

            runCatching {
                // Se recargan comentarios para mostrar el orden real guardado.
                commentRepository.addComment(comment)
                commentRepository.getCommentsByEvent(eventId)
            }.onSuccess { comments ->
                _state.update {
                    it.copy(
                        isSavingComment = false,
                        comments = comments,
                        commentVersion = it.commentVersion + 1,
                        successMessage = "Comentario agregado."
                    )
                }
            }.onFailure { throwable ->
                _state.update {
                    it.copy(
                        isSavingComment = false,
                        error = throwable.message ?: "No se pudo agregar el comentario."
                    )
                }
            }
        }
    }

    fun addRating(eventId: String, userId: String, value: Int) {
        // El backend reemplaza la calificacion previa del mismo usuario.
        viewModelScope.launch {
            _state.update { it.copy(isSavingRating = true, error = null, successMessage = null) }

            val rating = Rating(
                // El id queda vacio porque la API usa eventId-userId como llave.
                id = "",
                eventId = eventId,
                userId = userId,
                value = value,
                createdAtMillis = System.currentTimeMillis()
            )

            runCatching {
                commentRepository.addRating(rating)
                commentRepository.getRatingsByEvent(eventId)
            }.onSuccess { ratings ->
                _state.update {
                    it.copy(
                        isSavingRating = false,
                        ratings = ratings,
                        userRating = value,
                        successMessage = "Calificacion enviada."
                    )
                }
            }.onFailure { throwable ->
                _state.update {
                    it.copy(
                        isSavingRating = false,
                        error = throwable.message ?: "No se pudo enviar la calificacion."
                    )
                }
            }
        }
    }

    fun clearMessages() {
        _state.update { it.copy(error = null, successMessage = null) }
    }
}
