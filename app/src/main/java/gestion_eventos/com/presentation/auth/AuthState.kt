package gestion_eventos.com.presentation.auth

import gestion_eventos.com.domain.model.User

data class AuthState(
    // Estado de sesion usado por las pantallas de autenticacion.
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)
