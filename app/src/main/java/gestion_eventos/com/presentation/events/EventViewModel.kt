package gestion_eventos.com.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gestion_eventos.com.data.repository.EventRepositoryImpl
import gestion_eventos.com.domain.model.Event
import gestion_eventos.com.domain.model.EventCategory
import gestion_eventos.com.domain.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventViewModel(
    private val eventRepository: EventRepository = EventRepositoryImpl()
) : ViewModel() {
    // Controla la carga, creacion, edicion y cancelacion de eventos.
    private val _state = MutableStateFlow(EventState())
    val state: StateFlow<EventState> = _state.asStateFlow()

    init {
        // Carga eventos al crear el ViewModel para que la primera pantalla tenga datos.
        loadEvents()
    }

    fun loadEvents() {
        // Obtiene eventos activos desde el repositorio y actualiza la lista.
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching { eventRepository.getUpcomingEvents() }
                .onSuccess { events ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            events = events,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "No se pudieron cargar los eventos."
                        )
                    }
                }
        }
    }

    fun createEvent(
        title: String,
        description: String,
        location: String,
        dateTimeMillis: Long,
        category: EventCategory,
        imageUrl: String?,
        organizerId: String,
        onCreated: () -> Unit
    ) {
        // Valida campos basicos antes de enviar la creacion al backend.
        if (title.isBlank() || description.isBlank() || location.isBlank()) {
            _state.update { it.copy(error = "Completa titulo, descripcion y ubicacion.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null, successMessage = null) }

            // El backend asigna el id real, por eso aqui se envia vacio.
            val event = Event(
                id = "",
                title = title.trim(),
                description = description.trim(),
                dateTimeMillis = dateTimeMillis,
                location = location.trim(),
                organizerId = organizerId,
                category = category,
                imageUrl = imageUrl
            )

            runCatching {
                // Despues de crear se recarga la lista para reflejar el nuevo evento.
                eventRepository.createEvent(event)
                eventRepository.getUpcomingEvents()
            }.onSuccess { events ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        events = events,
                        error = null,
                        successMessage = "Evento creado correctamente."
                    )
                }
                onCreated()
            }.onFailure { throwable ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = throwable.message ?: "No se pudo crear el evento."
                    )
                }
            }
        }
    }

    fun updateEvent(
        originalEvent: Event,
        title: String,
        description: String,
        location: String,
        dateTimeMillis: Long,
        category: EventCategory,
        imageUrl: String?,
        onUpdated: () -> Unit
    ) {
        // Usa el evento original para conservar id, organizador y datos no editados.
        if (title.isBlank() || description.isBlank() || location.isBlank()) {
            _state.update { it.copy(error = "Completa titulo, descripcion y ubicacion.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null, successMessage = null) }

            // copy mantiene los campos que no se editan en pantalla.
            val updatedEvent = originalEvent.copy(
                title = title.trim(),
                description = description.trim(),
                dateTimeMillis = dateTimeMillis,
                location = location.trim(),
                category = category,
                imageUrl = imageUrl
            )

            runCatching {
                eventRepository.updateEvent(updatedEvent)
                eventRepository.getUpcomingEvents()
            }.onSuccess { events ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        events = events,
                        selectedEvent = updatedEvent,
                        error = null,
                        successMessage = "Evento actualizado correctamente."
                    )
                }
                onUpdated()
            }.onFailure { throwable ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = throwable.message ?: "No se pudo actualizar el evento."
                    )
                }
            }
        }
    }

    fun deleteEvent(eventId: String, onDeleted: () -> Unit) {
        // Cancela el evento sin borrarlo para conservar el historial de asistentes.
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null, successMessage = null) }

            runCatching {
                eventRepository.deleteEvent(eventId)
                eventRepository.getUpcomingEvents()
            }.onSuccess { events ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        events = events,
                        selectedEvent = null,
                        error = null,
                        successMessage = "Evento cancelado correctamente."
                    )
                }
                onDeleted()
            }.onFailure { throwable ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        error = throwable.message ?: "No se pudo cancelar el evento."
                    )
                }
            }
        }
    }

    fun selectEvent(eventId: String) {
        // Selecciona un evento desde la lista cargada en memoria de estado.
        _state.update { state ->
            state.copy(selectedEvent = state.events.firstOrNull { it.id == eventId })
        }
    }

    fun clearMessages() {
        // Limpia mensajes temporales al cambiar de pantalla.
        _state.update { it.copy(error = null, successMessage = null) }
    }
}
