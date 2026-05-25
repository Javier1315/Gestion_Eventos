package gestion_eventos.com.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gestion_eventos.com.data.repository.AuthRepositoryImpl
import gestion_eventos.com.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {
    // Expone el estado de autenticacion y coordina login, registro y cierre de sesion.
    private val _state = MutableStateFlow(
        AuthState(user = authRepository.getCurrentUser())
    )
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        if (!validateCredentials(email, password)) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { authRepository.login(email.trim(), password) }
                .onSuccess { user ->
                    _state.update { it.copy(isLoading = false, user = user) }
                }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "No se pudo iniciar sesion."
                        )
                    }
                }
        }
    }

    fun register(name: String, email: String, password: String) {
        if (name.isBlank()) {
            _state.update { it.copy(error = "Ingresa tu nombre.") }
            return
        }

        if (!validateCredentials(email, password)) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { authRepository.register(name.trim(), email.trim(), password) }
                .onSuccess { user ->
                    _state.update { it.copy(isLoading = false, user = user) }
                }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "No se pudo crear la cuenta."
                        )
                    }
                }
        }
    }

    fun loginWithGoogle(idToken: String) {
        // Inicia sesion con el token recibido desde Google Sign In.
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { authRepository.loginWithGoogle(idToken) }
                .onSuccess { user ->
                    _state.update { it.copy(isLoading = false, user = user) }
                }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "No se pudo iniciar sesion con Google."
                        )
                    }
                }
        }
    }

    fun showGoogleLoginError(message: String) {
        _state.update { it.copy(isLoading = false, error = message) }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _state.update { AuthState() }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun validateCredentials(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Ingresa correo y contrasena.") }
            return false
        }

        if (password.length < 6) {
            _state.update { it.copy(error = "La contrasena debe tener al menos 6 caracteres.") }
            return false
        }

        return true
    }
}
