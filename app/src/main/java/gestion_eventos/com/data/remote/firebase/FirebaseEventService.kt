package gestion_eventos.com.data.remote.firebase

import gestion_eventos.com.domain.model.Event

class FirebaseEventService {
    private val events = mutableListOf<Event>()

    suspend fun createEvent(event: Event) {
        events.add(event)
    }

    suspend fun updateEvent(event: Event) {
        val index = events.indexOfFirst { it.id == event.id }
        if (index >= 0) {
            events[index] = event
        }
    }

    suspend fun getEvents(): List<Event> {
        return events.toList()
    }

    suspend fun getEventById(eventId: String): Event? {
        return events.firstOrNull { it.id == eventId }
    }

    suspend fun deleteEvent(eventId: String) {
        events.removeAll { it.id == eventId }
    }
}
