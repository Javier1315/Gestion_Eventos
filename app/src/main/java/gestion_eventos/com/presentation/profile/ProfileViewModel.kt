package gestion_eventos.com.presentation.profile

import gestion_eventos.com.domain.model.User

class ProfileViewModel {
    var user: User? = null
        private set

    fun setUser(user: User) {
        this.user = user
    }
}
