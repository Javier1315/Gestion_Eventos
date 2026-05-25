package gestion_eventos.com.data.mapper

import gestion_eventos.com.data.local.entity.EventEntity
import gestion_eventos.com.data.remote.dto.EventDto
import gestion_eventos.com.data.remote.dto.EventRequestDto
import gestion_eventos.com.domain.model.Event
import gestion_eventos.com.domain.model.EventCategory

fun EventDto.toDomain(): Event {
    // Transforma la respuesta de la API al modelo de dominio.
    return Event(
        id = id,
        title = title,
        description = description,
        dateTimeMillis = dateTimeMillis,
        location = location,
        organizerId = organizerId,
        category = EventCategory.fromName(category),
        imageUrl = imageUrl,
        isPastEvent = isPastEvent,
        isCanceled = isCanceled,
        canceledAt = canceledAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Event.toDto(): EventDto {
    // Convierte el modelo de dominio al DTO completo.
    return EventDto(
        id = id,
        title = title,
        description = description,
        dateTimeMillis = dateTimeMillis,
        location = location,
        organizerId = organizerId,
        category = category.name,
        imageUrl = imageUrl,
        isPastEvent = isPastEvent,
        isCanceled = isCanceled,
        canceledAt = canceledAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Event.toRequestDto(): EventRequestDto {
    // Prepara el cuerpo enviado al backend al crear o editar eventos.
    return EventRequestDto(
        title = title,
        description = description,
        dateTimeMillis = dateTimeMillis,
        location = location,
        organizerId = organizerId,
        category = category.name,
        imageUrl = imageUrl,
        isPastEvent = isPastEvent,
        isCanceled = isCanceled,
        canceledAt = canceledAt
    )
}

fun EventEntity.toDomain(): Event {
    // Permite convertir una entidad local al modelo de dominio.
    return Event(
        id = id,
        title = title,
        description = description,
        dateTimeMillis = dateTimeMillis,
        location = location,
        organizerId = organizerId,
        category = EventCategory.fromName(category),
        imageUrl = imageUrl,
        isPastEvent = isPastEvent,
        isCanceled = isCanceled,
        canceledAt = canceledAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Event.toEntity(): EventEntity {
    // Convierte el modelo de dominio a entidad local.
    return EventEntity(
        id = id,
        title = title,
        description = description,
        dateTimeMillis = dateTimeMillis,
        location = location,
        organizerId = organizerId,
        category = category.name,
        imageUrl = imageUrl,
        isPastEvent = isPastEvent,
        isCanceled = isCanceled,
        canceledAt = canceledAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
