package gestion_eventos.com.domain.repository

import gestion_eventos.com.domain.model.Event

interface EventRepository {
    // Define el contrato de acceso a eventos para mantener la capa de dominio aislada.
    suspend fun createEvent(event: Event)
    suspend fun updateEvent(event: Event)
    suspend fun getUpcomingEvents(): List<Event>
    suspend fun getPastEvents(): List<Event>
    suspend fun getEventById(eventId: String): Event?
    suspend fun deleteEvent(eventId: String)
}
