package gestion_eventos.com.data.remote.api

import retrofit2.Response

object ApiResponseHandler {
    // Centraliza el manejo de respuestas exitosas y errores HTTP.
    fun <T> unwrap(response: Response<T>): T {
        if (response.isSuccessful) {
            return response.body() ?: throw IllegalStateException("Respuesta vacia del servidor.")
        }

        throw IllegalStateException("Error ${response.code()}: ${response.message()}")
    }
}
