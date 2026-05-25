package gestion_eventos.com.presentation.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gestion_eventos.com.core.ui.components.LocalEventCover
import gestion_eventos.com.domain.model.Event
import gestion_eventos.com.domain.model.EventCategory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    event: Event,
    state: EventState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onUpdateClick: (Event, String, String, String, Long, EventCategory, String?) -> Unit
) {
    // Pantalla para actualizar los datos principales de un evento existente.
    var title by remember(event.id) { mutableStateOf(event.title) }
    var description by remember(event.id) { mutableStateOf(event.description) }
    var location by remember(event.id) { mutableStateOf(event.location) }
    var category by remember(event.id) { mutableStateOf(event.category) }
    var selectedDateMillis by remember(event.id) { mutableLongStateOf(startOfDay(event.dateTimeMillis)) }
    var selectedHour by remember(event.id) { mutableIntStateOf(hourOf(event.dateTimeMillis)) }
    var selectedMinute by remember(event.id) { mutableIntStateOf(minuteOf(event.dateTimeMillis)) }
    var validationMessage by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val dateTimeMillis = resolveDateTimeMillis(selectedDateMillis, selectedHour, selectedMinute)

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val pickedDate = datePickerState.selectedDateMillis ?: selectedDateMillis
                        val pickedDateTime = resolveDateTimeMillis(pickedDate, selectedHour, selectedMinute)
                        if (pickedDateTime < System.currentTimeMillis()) {
                            validationMessage = "No puedes programar eventos en fechas pasadas."
                        } else {
                            selectedDateMillis = pickedDate
                            validationMessage = null
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(text = "Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text = "Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val pickedDateTime = resolveDateTimeMillis(
                            selectedDateMillis,
                            timePickerState.hour,
                            timePickerState.minute
                        )
                        if (pickedDateTime < System.currentTimeMillis()) {
                            validationMessage = "La fecha y hora seleccionadas ya pasaron."
                        } else {
                            selectedHour = timePickerState.hour
                            selectedMinute = timePickerState.minute
                            validationMessage = null
                        }
                        showTimePicker = false
                    }
                ) {
                    Text(text = "Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text(text = "Cancelar")
                }
            },
            title = { Text(text = "Selecciona la hora") },
            text = { TimePicker(state = timePickerState) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Editar evento",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        LocalEventCover(
            imageUri = event.imageUrl,
            label = category.label,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        EditTextField(value = title, label = "Titulo del evento", onValueChange = { title = it })
        EditTextField(
            value = description,
            label = "Descripcion",
            minLines = 3,
            onValueChange = { description = it }
        )

        Text(
            text = "Categoria",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        EventCategory.entries.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowItems.forEach { item ->
                    CategoryChip(
                        modifier = Modifier.weight(1f),
                        category = item,
                        selected = category == item,
                        onClick = { category = item }
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DateTimeOption(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.CalendarMonth,
                text = formatDate(selectedDateMillis),
                onClick = { showDatePicker = true }
            )
            DateTimeOption(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.Schedule,
                text = formatTime(selectedHour, selectedMinute),
                onClick = { showTimePicker = true }
            )
        }

        EditTextField(value = location, label = "Lugar", onValueChange = { location = it })

        validationMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        state.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSaving,
            onClick = {
                validationMessage = validateEdit(title, description, location, dateTimeMillis)
                if (validationMessage == null) {
                    onUpdateClick(event, title, description, location, dateTimeMillis, category, event.imageUrl)
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            } else {
                Icon(imageVector = Icons.Rounded.Save, contentDescription = null)
                Text(modifier = Modifier.padding(start = 8.dp), text = "Guardar cambios")
            }
        }
    }
}

@Composable
private fun EditTextField(
    value: String,
    label: String,
    minLines: Int = 1,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        minLines = minLines,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun CategoryChip(
    category: EventCategory,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            text = category.label,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DateTimeOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Text(text = text, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private fun validateEdit(title: String, description: String, location: String, dateTimeMillis: Long): String? {
    return when {
        title.isBlank() -> "Escribe el titulo del evento."
        description.isBlank() -> "Escribe una descripcion."
        location.isBlank() -> "Escribe el lugar del evento."
        dateTimeMillis < System.currentTimeMillis() -> "No puedes programar eventos en fechas pasadas."
        else -> null
    }
}

private fun resolveDateTimeMillis(dateMillis: Long, hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = dateMillis
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

private fun startOfDay(timestamp: Long): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = timestamp
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

private fun hourOf(timestamp: Long): Int = Calendar.getInstance().apply { timeInMillis = timestamp }
    .get(Calendar.HOUR_OF_DAY)

private fun minuteOf(timestamp: Long): Int = Calendar.getInstance().apply { timeInMillis = timestamp }
    .get(Calendar.MINUTE)

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(timestamp)
}

private fun formatTime(hour: Int, minute: Int): String {
    return "%02d:%02d".format(hour, minute)
}
