package gestion_eventos.com.domain.repository

import gestion_eventos.com.domain.model.User

interface AuthRepository {
    // Define las operaciones de autenticacion que usa la capa de presentacion.
    suspend fun login(email: String, password: String): User
    suspend fun register(name: String, email: String, password: String): User
    suspend fun loginWithGoogle(idToken: String): User
    suspend fun loginWithSocialProvider(providerName: String): User
    suspend fun logout()
    fun getCurrentUser(): User?
}
