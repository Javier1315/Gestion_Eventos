package gestion_eventos.com.core.navigation

sealed class Routes(val route: String) {
    data object Login : Routes("login")
    data object Register : Routes("register")
    data object EventList : Routes("event_list")
    data object EventDetail : Routes("event_detail")
    data object CreateEvent : Routes("create_event")
    data object EditEvent : Routes("edit_event")
    data object History : Routes("history")
    data object Stats : Routes("stats")
    data object Profile : Routes("profile")
}
