package gestion_eventos.com.domain.repository

import gestion_eventos.com.domain.model.Comment
import gestion_eventos.com.domain.model.Rating

interface CommentRepository {
    suspend fun addComment(comment: Comment)
    suspend fun getCommentsByEvent(eventId: String): List<Comment>
    suspend fun addRating(rating: Rating)
    suspend fun getRatingsByEvent(eventId: String): List<Rating>
}
