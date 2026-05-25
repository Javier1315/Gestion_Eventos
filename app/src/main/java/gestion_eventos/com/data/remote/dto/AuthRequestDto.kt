package gestion_eventos.com.data.remote.dto

data class AuthRequestDto(
    val name: String? = null,
    val email: String,
    val password: String
)
