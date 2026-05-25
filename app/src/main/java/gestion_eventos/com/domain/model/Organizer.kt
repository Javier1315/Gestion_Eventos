package gestion_eventos.com.domain.model

class Organizer(
    override val id: String,
    override val name: String,
    override val email: String,
    val organizationName: String
) : User(id, name, email)
