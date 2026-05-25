package gestion_eventos.com.data.repository

import gestion_eventos.com.data.mapper.toDomain
import gestion_eventos.com.data.mapper.toRequestDto
import gestion_eventos.com.data.remote.api.ApiResponseHandler
import gestion_eventos.com.data.remote.api.ApiService
import gestion_eventos.com.data.remote.api.RetrofitClient
import gestion_eventos.com.domain.model.Comment
import gestion_eventos.com.domain.model.Rating
import gestion_eventos.com.domain.repository.CommentRepository

class CommentRepositoryImpl(
    private val apiService: ApiService = RetrofitClient.apiService
) : CommentRepository {
    // Implementa comentarios y calificaciones usando endpoints sociales.
    override suspend fun addComment(comment: Comment) {
        // Guarda un comentario nuevo para el evento indicado.
        ApiResponseHandler.unwrap(apiService.addComment(comment.eventId, comment.toRequestDto()))
    }

    override suspend fun getCommentsByEvent(eventId: String): List<Comment> {
        // Convierte los comentarios remotos a modelos de dominio.
        return ApiResponseHandler.unwrap(apiService.getComments(eventId)).map { it.toDomain() }
    }

    override suspend fun addRating(rating: Rating) {
        // Envia la calificacion; el backend actualiza si ya existia.
        ApiResponseHandler.unwrap(apiService.addRating(rating.eventId, rating.toRequestDto()))
    }

    override suspend fun getRatingsByEvent(eventId: String): List<Rating> {
        // Recupera todas las calificaciones para calcular promedio en UI.
        return ApiResponseHandler.unwrap(apiService.getRatings(eventId)).map { it.toDomain() }
    }
}
