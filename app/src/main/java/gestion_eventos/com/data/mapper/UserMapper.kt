package gestion_eventos.com.data.mapper

import gestion_eventos.com.data.remote.dto.UserDto
import gestion_eventos.com.domain.model.User

fun UserDto.toDomain(): User {
    return User(id = id, name = name, email = email)
}

fun User.toDto(): UserDto {
    return UserDto(id = id, name = name, email = email)
}
