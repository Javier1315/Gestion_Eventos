package gestion_eventos.com.domain.model

open class User(
    // Modelo base para representar usuarios autenticados.
    open val id: String,
    open val name: String,
    open val email: String
)
