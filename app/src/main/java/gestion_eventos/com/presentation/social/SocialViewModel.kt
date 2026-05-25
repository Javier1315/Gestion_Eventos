package gestion_eventos.com.presentation.social

class SocialViewModel {
    var currentRating = 0
        private set

    fun updateRating(value: Int) {
        currentRating = value.coerceIn(0, 5)
    }
}
