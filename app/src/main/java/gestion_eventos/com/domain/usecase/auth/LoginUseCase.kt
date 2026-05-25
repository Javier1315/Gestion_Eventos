package gestion_eventos.com.domain.usecase.auth

import gestion_eventos.com.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)
}
