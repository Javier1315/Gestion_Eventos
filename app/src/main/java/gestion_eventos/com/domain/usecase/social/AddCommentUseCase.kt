package gestion_eventos.com.domain.usecase.social

import gestion_eventos.com.domain.model.Comment
import gestion_eventos.com.domain.repository.CommentRepository

class AddCommentUseCase(private val repository: CommentRepository) {
    suspend operator fun invoke(comment: Comment) = repository.addComment(comment)
}
