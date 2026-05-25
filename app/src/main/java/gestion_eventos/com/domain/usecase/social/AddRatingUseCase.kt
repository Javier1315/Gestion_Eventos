package gestion_eventos.com.domain.usecase.social

import gestion_eventos.com.domain.model.Rating
import gestion_eventos.com.domain.repository.CommentRepository

class AddRatingUseCase(private val repository: CommentRepository) {
    suspend operator fun invoke(rating: Rating) = repository.addRating(rating)
}
