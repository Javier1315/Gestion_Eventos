package gestion_eventos.com.data.mapper

import gestion_eventos.com.data.remote.dto.CommentDto
import gestion_eventos.com.data.remote.dto.CommentRequestDto
import gestion_eventos.com.data.remote.dto.RSVPDto
import gestion_eventos.com.data.remote.dto.RSVPRequestDto
import gestion_eventos.com.data.remote.dto.RatingDto
import gestion_eventos.com.data.remote.dto.RatingRequestDto
import gestion_eventos.com.domain.model.Comment
import gestion_eventos.com.domain.model.RSVP
import gestion_eventos.com.domain.model.RSVPStatus
import gestion_eventos.com.domain.model.Rating

fun RSVPDto.toDomain(): RSVP {
    return RSVP(
        id = id,
        eventId = eventId,
        userId = userId,
        status = runCatching { RSVPStatus.valueOf(status) }.getOrDefault(RSVPStatus.PENDING),
        updatedAtMillis = 0L
    )
}

fun RSVPStatus.toRequestDto(userId: String): RSVPRequestDto {
    return RSVPRequestDto(userId = userId, status = name)
}

fun CommentDto.toDomain(): Comment {
    return Comment(
        id = id,
        eventId = eventId,
        userId = userId,
        userName = userName.orEmpty(),
        message = message,
        createdAtMillis = 0L
    )
}

fun Comment.toRequestDto(): CommentRequestDto {
    return CommentRequestDto(
        userId = userId,
        userName = userName,
        message = message
    )
}

fun RatingDto.toDomain(): Rating {
    return Rating(
        id = id,
        eventId = eventId,
        userId = userId,
        value = value,
        createdAtMillis = 0L
    )
}

fun Rating.toRequestDto(): RatingRequestDto {
    return RatingRequestDto(
        userId = userId,
        value = value
    )
}
