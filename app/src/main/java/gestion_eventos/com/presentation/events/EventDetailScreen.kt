package gestion_eventos.com.presentation.events

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gestion_eventos.com.core.ui.components.LocalEventCover
import gestion_eventos.com.core.utils.DateUtils
import gestion_eventos.com.domain.model.Comment
import gestion_eventos.com.domain.model.Event

@Composable
fun EventDetailScreen(
    event: Event,
    state: EventDetailState,
    userId: String,
    userName: String,
    canManage: Boolean,
    isSavingEvent: Boolean,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onConfirmAttendance: (String, String) -> Unit,
    onAddComment: (String, String, String, String) -> Unit,
    onAddRating: (String, String, Int) -> Unit
) {
    // Muestra la informacion completa del evento y sus acciones sociales.
    val context = LocalContext.current
    var comment by remember(event.id) { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.commentVersion) {
        if (state.commentVersion > 0) {
            comment = ""
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            DetailHeader(event = event, onBackClick = onBackClick)
        }

        item {
            ShareSection(
                onShareClick = { shareEvent(context, event) },
                onEmailClick = { shareEventByEmail(context, event) }
            )
        }

        if (canManage) {
            item {
                ManagementSection(
                    isSaving = isSavingEvent,
                    onEditClick = onEditClick,
                    onDeleteClick = { showDeleteDialog = true }
                )
            }
        }

        item {
            RsvpSection(
                isLoading = state.isSavingRsvp,
                isConfirmed = state.isRsvpConfirmed,
                onConfirm = { onConfirmAttendance(event.id, userId) }
            )
        }

        if (state.isLoading) {
            item {
                LoadingSection()
            }
        }

        if (!state.error.isNullOrBlank()) {
            item {
                StatusSection(
                    message = state.error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (!state.successMessage.isNullOrBlank()) {
            item {
                StatusSection(
                    message = state.successMessage,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        item {
            RatingSection(
                average = state.averageRating,
                count = state.ratings.size,
                userRating = state.userRating,
                isSaving = state.isSavingRating,
                onAddRating = { value -> onAddRating(event.id, userId, value) }
            )
        }

        item {
            CommentForm(
                comment = comment,
                isSaving = state.isSavingComment,
                onCommentChange = { comment = it },
                onSubmit = {
                    onAddComment(event.id, userId, userName, comment)
                }
            )
        }

        item {
            Text(
                text = "Comentarios",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (state.comments.isEmpty()) {
            item {
                EmptyComments()
            }
        } else {
            items(state.comments, key = { it.id }) { item ->
                CommentCard(comment = item)
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = "Cancelar evento") },
            text = {
                Text(text = "El evento dejara de aparecer en la lista principal y se avisara en el historial de quienes confirmaron asistencia.")
            },
            confirmButton = {
                TextButton(
                    enabled = !isSavingEvent,
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    }
                ) {
                    Text(text = "Cancelar evento")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = "Volver")
                }
            }
        )
    }
}

@Composable
private fun ShareSection(
    onShareClick: () -> Unit,
    onEmailClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Compartir evento",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onShareClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Rounded.Share, contentDescription = null)
                    Text(modifier = Modifier.padding(start = 6.dp), text = "Redes")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onEmailClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(imageVector = Icons.Rounded.Email, contentDescription = null)
                    Text(modifier = Modifier.padding(start = 6.dp), text = "Correo")
                }
            }
        }
    }
}

@Composable
private fun DetailHeader(
    event: Event,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.padding(top = 18.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Detalle del evento",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        LocalEventCover(
            imageUri = event.imageUrl,
            label = event.category.label,
            modifier = Modifier
                .fillMaxWidth()
                .height(164.dp)
        )

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = event.title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = event.category.label,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = event.description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            modifier = Modifier.padding(top = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailMeta(
                icon = Icons.Rounded.CalendarMonth,
                text = DateUtils.formatTimestamp(event.dateTimeMillis)
            )
            DetailMeta(
                icon = Icons.Rounded.LocationOn,
                text = event.location
            )
        }
    }
}

private fun shareEvent(context: Context, event: Event) {
    // Abre el selector de Android para compartir el evento en otras apps.
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Invitacion: ${event.title}")
        putExtra(Intent.EXTRA_TEXT, buildShareText(event))
    }
    context.startActivity(Intent.createChooser(intent, "Compartir evento"))
}

private fun shareEventByEmail(context: Context, event: Event) {
    // Prepara un correo con los detalles del evento.
    val subject = "Invitacion: ${event.title}"
    val body = buildShareText(event)
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    if (emailIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(emailIntent)
        return
    }

    val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    runCatching {
        context.startActivity(Intent.createChooser(fallbackIntent, "Enviar por correo"))
    }.onFailure {
        Toast.makeText(context, "No se encontro una aplicacion de correo.", Toast.LENGTH_SHORT).show()
    }
}

private fun buildShareText(event: Event): String {
    // Construye el texto compartido por redes o correo.
    return """
        Te invito al evento: ${event.title}

        Categoria: ${event.category.label}
        Fecha y hora: ${DateUtils.formatTimestamp(event.dateTimeMillis)}
        Lugar: ${event.location}

        ${event.description}
    """.trimIndent()
}

@Composable
private fun DetailMeta(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun ManagementSection(
    isSaving: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Gestion del evento",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !isSaving,
                    onClick = onEditClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                    Text(modifier = Modifier.padding(start = 6.dp), text = "Editar")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !isSaving,
                    onClick = onDeleteClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onError, strokeWidth = 2.dp)
                    } else {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
                        Text(modifier = Modifier.padding(start = 6.dp), text = "Cancelar")
                    }
                }
            }
        }
    }
}

@Composable
private fun RsvpSection(
    isLoading: Boolean,
    isConfirmed: Boolean,
    onConfirm: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (isConfirmed) "Asistencia confirmada" else "Confirma tu asistencia",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isConfirmed) "Este evento ya aparece en tu historial." else "Reserva tu participacion en este evento.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && !isConfirmed,
                onClick = onConfirm,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(imageVector = Icons.Rounded.CheckCircle, contentDescription = null)
                    Text(
                        modifier = Modifier.padding(start = 6.dp),
                        text = if (isConfirmed) "Confirmado" else "Confirmar"
                    )
                }
            }
        }
    }
}

@Composable
private fun RatingSection(
    average: Double,
    count: Int,
    userRating: Int,
    isSaving: Boolean,
    onAddRating: (Int) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Calificaciones",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = if (count == 0) "Sin calificaciones todavia" else "Promedio ${"%.1f".format(average)} de $count calificaciones",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
            if (userRating > 0) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "Tu calificacion: $userRating de 5. Puedes actualizarla tocando otra estrella.",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                modifier = Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(5) { index ->
                    IconButton(
                        enabled = !isSaving,
                        onClick = { onAddRating(index + 1) }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "Calificar ${index + 1}",
                            tint = if (index < userRating) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentForm(
    comment: String,
    isSaving: Boolean,
    onCommentChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Agregar comentario",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = comment,
                onValueChange = onCommentChange,
                label = { Text("Tu comentario") },
                minLines = 3,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
            Button(
                enabled = !isSaving && comment.isNotBlank(),
                onClick = onSubmit,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(imageVector = Icons.AutoMirrored.Rounded.Send, contentDescription = null)
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "Enviar"
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentCard(comment: Comment) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = comment.userName.ifBlank { "Usuario" },
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = comment.message,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun EmptyComments() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Aun no hay comentarios para este evento.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun LoadingSection() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator()
            Text(
                text = "Cargando interacciones...",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun StatusSection(
    message: String,
    color: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = message,
            color = color,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
