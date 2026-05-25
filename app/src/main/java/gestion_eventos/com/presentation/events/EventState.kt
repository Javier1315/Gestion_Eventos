package gestion_eventos.com.presentation.events

import gestion_eventos.com.domain.model.Event

data class EventState(
    // Estado compartido para crear, listar, editar y cancelar eventos.
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val events: List<Event> = emptyList(),
    val selectedEvent: Event? = null,
    val error: String? = null,
    val successMessage: String? = null
)
