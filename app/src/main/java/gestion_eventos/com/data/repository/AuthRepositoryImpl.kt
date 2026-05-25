package gestion_eventos.com.data.repository

import gestion_eventos.com.data.remote.firebase.FirebaseAuthService
import gestion_eventos.com.domain.model.User
import gestion_eventos.com.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authService: FirebaseAuthService = FirebaseAuthService()
) : AuthRepository {
    // Implementa el repositorio de autenticacion usando Firebase.
    override suspend fun login(email: String, password: String): User {
        return authService.login(email, password)
    }

    override suspend fun register(name: String, email: String, password: String): User {
        return authService.register(name, email, password)
    }

    override suspend fun loginWithGoogle(idToken: String): User {
        return authService.loginWithGoogle(idToken)
    }

    override suspend fun loginWithSocialProvider(providerName: String): User {
        return authService.loginWithProvider(providerName)
    }

    override suspend fun logout() {
        authService.logout()
    }

    override fun getCurrentUser(): User? {
        return authService.getCurrentUser()
    }
}
