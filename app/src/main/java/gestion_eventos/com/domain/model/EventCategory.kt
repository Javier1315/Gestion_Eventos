package gestion_eventos.com.domain.model

enum class EventCategory(val label: String) {
    // Define las categorias disponibles para clasificar eventos.
    ACADEMIC("Academico"),
    COMMUNITY("Comunitario"),
    LUNCH("Almuerzo"),
    SPORTS("Deportivo"),
    CULTURAL("Cultural"),
    TECHNOLOGY("Tecnologia"),
    VOLUNTEERING("Voluntariado"),
    MEETING("Reunion");

    companion object {
        // Recupera una categoria desde el nombre guardado en la API.
        fun fromName(value: String?): EventCategory {
            return entries.firstOrNull { it.name == value } ?: ACADEMIC
        }
    }
}
