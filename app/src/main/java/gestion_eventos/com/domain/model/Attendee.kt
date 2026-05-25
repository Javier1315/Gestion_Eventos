package gestion_eventos.com.domain.model

class Attendee(
    override val id: String,
    override val name: String,
    override val email: String,
    val attendedEvents: List<String> = emptyList()
) : User(id, name, email)
