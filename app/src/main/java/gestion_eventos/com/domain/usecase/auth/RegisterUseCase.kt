package gestion_eventos.com.domain.usecase.auth

import gestion_eventos.com.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String) {
        repository.register(name, email, password)
    }
}
