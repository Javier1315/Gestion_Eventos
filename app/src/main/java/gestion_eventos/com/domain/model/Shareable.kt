package gestion_eventos.com.domain.model

interface Shareable {
    // Contrato para objetos que pueden generar un mensaje compartible.
    fun shareMessage(): String
}
