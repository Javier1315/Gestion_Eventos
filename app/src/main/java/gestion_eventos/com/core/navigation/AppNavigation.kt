package gestion_eventos.com.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import gestion_eventos.com.presentation.auth.AuthViewModel
import gestion_eventos.com.presentation.auth.LoginScreen
import gestion_eventos.com.presentation.auth.RegisterScreen
import gestion_eventos.com.presentation.events.EventListScreen
import gestion_eventos.com.presentation.events.CreateEventScreen
import gestion_eventos.com.presentation.events.EditEventScreen
import gestion_eventos.com.presentation.events.EventDetailScreen
import gestion_eventos.com.presentation.events.EventDetailViewModel
import gestion_eventos.com.presentation.events.EventViewModel
import gestion_eventos.com.presentation.history.HistoryViewModel
import gestion_eventos.com.presentation.history.HistoryScreen
import gestion_eventos.com.presentation.profile.ProfileScreen
import gestion_eventos.com.domain.model.Event

@Composable
fun AppNavigation() {
    // Controla el flujo principal entre autenticacion, pestanas, detalle y edicion.
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.state.collectAsState()
    var authScreen by remember { mutableStateOf(AuthScreen.Login) }
    var selectedTab by remember { mutableStateOf(AppTab.Events) }
    val user = authState.user

    if (user == null) {
        when (authScreen) {
            AuthScreen.Login -> LoginScreen(
                state = authState,
                onLoginClick = authViewModel::login,
                onGoogleLoginClick = authViewModel::loginWithGoogle,
                onGoogleLoginError = authViewModel::showGoogleLoginError,
                onRegisterClick = {
                    authViewModel.clearError()
                    authScreen = AuthScreen.Register
                }
            )

            AuthScreen.Register -> RegisterScreen(
                state = authState,
                onRegisterClick = authViewModel::register,
                onLoginClick = {
                    authViewModel.clearError()
                    authScreen = AuthScreen.Login
                }
            )
        }
        return
    }

    val eventViewModel: EventViewModel = viewModel()
    val detailViewModel: EventDetailViewModel = viewModel()
    val historyViewModel: HistoryViewModel = viewModel()
    val eventState by eventViewModel.state.collectAsState()
    val detailState by detailViewModel.state.collectAsState()
    val historyState by historyViewModel.state.collectAsState()
    // selectedEvent y editingEvent funcionan como navegacion simple sin libreria externa.
    var selectedEvent by remember { mutableStateOf<Event?>(null) }
    var editingEvent by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(selectedTab, user.id) {
        // Refresca historial al entrar a la pestana para mostrar avisos recientes.
        if (selectedTab == AppTab.History) {
            historyViewModel.loadHistory(user.id)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = NavigationBarDefaultsElevation
            ) {
                AppTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = {
                            selectedEvent = null
                            editingEvent = null
                            selectedTab = tab
                            if (tab == AppTab.Events) {
                                eventViewModel.loadEvents()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(text = tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            val eventToEdit = editingEvent
            if (eventToEdit != null) {
                // La pantalla de edicion tiene prioridad sobre el detalle.
                EditEventScreen(
                    event = eventToEdit,
                    state = eventState,
                    onBackClick = {
                        editingEvent = null
                        selectedEvent = eventToEdit
                    },
                    onUpdateClick = { event, title, description, location, dateTimeMillis, category, imageUrl ->
                        eventViewModel.updateEvent(
                            originalEvent = event,
                            title = title,
                            description = description,
                            location = location,
                            dateTimeMillis = dateTimeMillis,
                            category = category,
                            imageUrl = imageUrl,
                            onUpdated = {
                                editingEvent = null
                                selectedEvent = null
                                selectedTab = AppTab.Events
                                eventViewModel.clearMessages()
                            }
                        )
                    }
                )
                return@Box
            }

            val event = selectedEvent

            if (event != null) {
                // El detalle centraliza RSVP, comentarios, calificaciones y acciones de gestion.
                EventDetailScreen(
                    event = event,
                    state = detailState,
                    userId = user.id,
                    userName = user.name,
                    canManage = event.organizerId == user.id,
                    isSavingEvent = eventState.isSaving,
                    onBackClick = {
                        detailViewModel.clearMessages()
                        selectedEvent = null
                    },
                    onEditClick = {
                        detailViewModel.clearMessages()
                        editingEvent = event
                        selectedEvent = null
                    },
                    onDeleteClick = {
                        // Al cancelar, se recarga historial para reflejar la notificacion.
                        eventViewModel.deleteEvent(event.id) {
                            detailViewModel.clearMessages()
                            selectedEvent = null
                            selectedTab = AppTab.Events
                            historyViewModel.loadHistory(user.id)
                        }
                    },
                    onConfirmAttendance = { eventId, userId ->
                        detailViewModel.confirmAttendance(eventId, userId)
                    },
                    onAddComment = { eventId, userId, userName, message ->
                        detailViewModel.addComment(eventId, userId, userName, message)
                    },
                    onAddRating = { eventId, userId, value ->
                        detailViewModel.addRating(eventId, userId, value)
                    }
                )
                return@Box
            }

            when (selectedTab) {
                AppTab.Events -> EventListScreen(
                    state = eventState,
                    onCreateClick = {
                        eventViewModel.clearMessages()
                        selectedTab = AppTab.Create
                    },
                    onRetryClick = eventViewModel::loadEvents,
                    onEventClick = { clickedEvent ->
                        detailViewModel.clearMessages()
                        editingEvent = null
                        detailViewModel.loadEventData(clickedEvent.id, user.id)
                        selectedEvent = clickedEvent
                    }
                )

                AppTab.Create -> CreateEventScreen(
                    state = eventState,
                    organizerId = user.id,
                    onCreateClick = { title, description, location, dateTimeMillis, category, imageUrl, organizerId ->
                        eventViewModel.createEvent(
                            title = title,
                            description = description,
                            location = location,
                            dateTimeMillis = dateTimeMillis,
                            category = category,
                            imageUrl = imageUrl,
                            organizerId = organizerId,
                            onCreated = {
                                selectedTab = AppTab.Events
                                eventViewModel.clearMessages()
                            }
                        )
                    }
                )
                AppTab.History -> HistoryScreen(state = historyState)
                AppTab.Profile -> ProfileScreen(
                    user = user,
                    onLogoutClick = authViewModel::logout
                )
            }
        }
    }
}

private val NavigationBarDefaultsElevation = 0.dp

private enum class AppTab(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    // Define las pestanas visibles despues de iniciar sesion.
    Events("Eventos", Icons.Rounded.CalendarMonth),
    Create("Crear", Icons.Rounded.AddCircle),
    History("Historial", Icons.Rounded.History),
    Profile("Perfil", Icons.Rounded.Person)
}

private enum class AuthScreen {
    Login,
    Register
}
