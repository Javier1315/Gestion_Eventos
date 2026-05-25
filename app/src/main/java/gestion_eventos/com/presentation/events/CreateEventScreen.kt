package gestion_eventos.com.presentation.events

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gestion_eventos.com.core.ui.components.LocalEventCover
import gestion_eventos.com.core.utils.LocalImageStorage
import gestion_eventos.com.domain.model.EventCategory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    state: EventState,
    organizerId: String,
    modifier: Modifier = Modifier,
    onCreateClick: (String, String, String, Long, EventCategory, String?, String) -> Unit
) {
    // Formulario por pasos para crear eventos con informacion, fecha, lugar y vista previa.
    val context = LocalContext.current
    var step by remember { mutableIntStateOf(0) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(EventCategory.ACADEMIC) }
    var coverImageUri by remember { mutableStateOf<String?>(null) }
    var selectedDateMillis by remember { mutableLongStateOf(0L) }
    var selectedHour by remember { mutableIntStateOf(18) }
    var selectedMinute by remember { mutableIntStateOf(0) }
    var location by remember { mutableStateOf("") }
    var locationDetails by remember { mutableStateOf("") }
    var validationMessage by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    // Photo Picker evita pedir permisos manuales para elegir una imagen.
    val coverPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            coverImageUri = LocalImageStorage.saveEventCover(context, it)
            if (coverImageUri == null) {
                validationMessage = "No se pudo guardar la portada seleccionada."
            }
        }
    }

    val dateText = selectedDateMillis.takeIf { it > 0L }?.let { formatDate(it) }.orEmpty()
    val timeText = formatTime(selectedHour, selectedMinute)
    // Une lugar principal y detalles extra para enviarlo como una sola ubicacion.
    val finalLocation = buildString {
        append(location.trim())
        if (locationDetails.isNotBlank()) {
            append(". ")
            append(locationDetails.trim())
        }
    }
    val dateTimeMillis = resolveDateTimeMillis(selectedDateMillis, selectedHour, selectedMinute)

    if (showDatePicker) {
        // DatePicker permite escoger fecha de forma visual y valida que no sea pasada.
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis.takeIf { it > 0L }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val pickedDate = datePickerState.selectedDateMillis ?: 0L
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
        // TimePicker evita escribir la hora manualmente y reduce errores de formato.
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
                        if (selectedDateMillis > 0L && pickedDateTime < System.currentTimeMillis()) {
                            validationMessage = "La hora seleccionada ya paso."
                            //esto solo es extra porque de nada sirve una app de programar eventos
                            //si se programa para el mismo dia
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
            .padding(horizontal = 18.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Crear evento",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Completa cada paso antes de publicar.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        StepProgress(currentStep = step)
        Spacer(modifier = Modifier.height(20.dp))

        when (step) {
            0 -> BasicInfoStep(
                // Primer paso: datos que identifican y clasifican el evento.
                title = title,
                description = description,
                category = category,
                coverImageUri = coverImageUri,
                onTitleChange = { title = it },
                onDescriptionChange = { description = it },
                onCategoryChange = { category = it },
                onCoverClick = {
                    coverPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )

            1 -> DateTimeStep(
                // Segundo paso: fecha y hora del evento.
                dateText = dateText,
                timeText = timeText,
                onDateClick = { showDatePicker = true },
                onTimeClick = { showTimePicker = true }
            )

            2 -> LocationStep(
                // Tercer paso: lugar e indicaciones para asistentes.
                location = location,
                locationDetails = locationDetails,
                onLocationChange = { location = it },
                onLocationDetailsChange = { locationDetails = it }
            )

            3 -> PublishStep(
                // Ultimo paso: vista previa antes de enviar al backend.
                title = title,
                description = description,
                category = category,
                dateText = dateText,
                timeText = timeText,
                location = finalLocation,
                coverImageUri = coverImageUri
            )
        }

        validationMessage?.let { message ->
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (!state.error.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (step > 0) {
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !state.isSaving,
                    onClick = {
                        // Permite regresar sin perder datos escritos en pasos anteriores.
                        validationMessage = null
                        step -= 1
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text = "Atras")
                }
            }

            Button(
                modifier = Modifier.weight(1f),
                enabled = !state.isSaving,
                onClick = {
                    // Cada paso valida solo los campos que le corresponden.
                    val error = validateStep(
                        step = step,
                        title = title,
                        description = description,
                        selectedDateMillis = selectedDateMillis,
                        dateTimeMillis = dateTimeMillis,
                        location = location
                    )
                    validationMessage = error

                    if (error == null && step < 3) {
                        step += 1
                    } else if (error == null) {
                        onCreateClick(
                            title,
                            description,
                            finalLocation,
                            dateTimeMillis,
                            category,
                            coverImageUri,
                            organizerId
                        )
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
                    if (step == 3) {
                        Icon(imageVector = Icons.Rounded.CheckCircle, contentDescription = null)
                    }
                    Text(
                        modifier = Modifier.padding(start = if (step == 3) 8.dp else 0.dp),
                        text = if (step == 3) "Publicar evento" else "Siguiente"
                    )
                }
            }
        }
    }
}

@Composable
private fun StepProgress(currentStep: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("Info", "Fecha", "Lugar", "Publicar").forEachIndexed { index, label ->
            val isSelected = index == currentStep
            val isDone = index < currentStep
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    color = when {
                        isSelected -> MaterialTheme.colorScheme.primary
                        isDone -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        text = "${index + 1}",
                        color = if (isSelected || isDone) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = label,
                    color = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun BasicInfoStep(
    title: String,
    description: String,
    category: EventCategory,
    coverImageUri: String?,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (EventCategory) -> Unit,
    onCoverClick: () -> Unit
) {
    CoverPicker(
        coverImageUri = coverImageUri,
        onClick = onCoverClick
    )
    Spacer(modifier = Modifier.height(16.dp))
    FormSection(title = "Informacion basica") {
        EventTextField(value = title, label = "Titulo del evento", onValueChange = onTitleChange)
        EventTextField(
            value = description,
            label = "Descripcion corta",
            minLines = 4,
            onValueChange = onDescriptionChange
        )
        Text(
            text = "Categoria",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        CategorySelector(
            selectedCategory = category,
            onCategoryChange = onCategoryChange
        )
    }
}

@Composable
private fun DateTimeStep(
    dateText: String,
    timeText: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    FormSection(title = "Fecha y hora") {
        PickerRow(
            label = "Fecha",
            value = dateText.ifBlank { "Seleccionar fecha" },
            icon = Icons.Rounded.CalendarMonth,
            onClick = onDateClick
        )
        PickerRow(
            label = "Hora",
            value = timeText,
            icon = Icons.Rounded.Schedule,
            onClick = onTimeClick
        )
    }
}

@Composable
private fun LocationStep(
    location: String,
    locationDetails: String,
    onLocationChange: (String) -> Unit,
    onLocationDetailsChange: (String) -> Unit
) {
    FormSection(title = "Lugar del evento") {
        EventTextField(
            value = location,
            label = "Nombre del lugar",
            leadingIcon = { Icon(Icons.Rounded.LocationOn, contentDescription = null) },
            onValueChange = onLocationChange
        )
        EventTextField(
            value = locationDetails,
            label = "Indicaciones o referencia",
            minLines = 3,
            onValueChange = onLocationDetailsChange
        )
    }
}

@Composable
private fun PublishStep(
    title: String,
    description: String,
    category: EventCategory,
    dateText: String,
    timeText: String,
    location: String,
    coverImageUri: String?
) {
    FormSection(title = "Vista previa") {
        LocalEventCover(
            imageUri = coverImageUri,
            label = category.label,
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
        )
        PreviewLine(label = "Titulo", value = title)
        PreviewLine(label = "Categoria", value = category.label)
        PreviewLine(label = "Descripcion", value = description)
        PreviewLine(label = "Fecha", value = dateText)
        PreviewLine(label = "Hora", value = timeText)
        PreviewLine(label = "Lugar", value = location)
    }
}

@Composable
private fun CategorySelector(
    selectedCategory: EventCategory,
    onCategoryChange: (EventCategory) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        EventCategory.entries.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { item ->
                    CategoryOption(
                        modifier = Modifier.weight(1f),
                        category = item,
                        selected = selectedCategory == item,
                        onClick = { onCategoryChange(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryOption(
    category: EventCategory,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (selected) Color.White else MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    )
            )
            Text(
                text = category.label,
                color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun PickerRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            Column {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun CoverPicker(
    coverImageUri: String?,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!coverImageUri.isNullOrBlank()) {
            LocalEventCover(
                imageUri = coverImageUri,
                label = "Portada seleccionada",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.PhotoCamera,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = if (coverImageUri.isNullOrBlank()) "Agregar portada del evento" else "Cambiar portada del evento",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Formato recomendado: JPG, PNG o WebP 16:9",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun FormSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}

@Composable
private fun EventTextField(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        minLines = minLines,
        leadingIcon = leadingIcon,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun PreviewLine(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = value.ifBlank { "Pendiente" },
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun validateStep(
    step: Int,
    title: String,
    description: String,
    selectedDateMillis: Long,
    dateTimeMillis: Long,
    location: String
): String? {
    // Devuelve un mensaje si el paso actual esta incompleto o tiene fecha invalida.
    return when {
        step == 0 && title.isBlank() -> "Ingresa el titulo del evento."
        step == 0 && description.isBlank() -> "Ingresa una descripcion del evento."
        step == 1 && selectedDateMillis <= 0L -> "Selecciona la fecha del evento."
        step == 1 && dateTimeMillis < System.currentTimeMillis() -> "No puedes programar eventos en fechas pasadas."
        step == 2 && location.isBlank() -> "Ingresa el lugar del evento."
        else -> null
    }
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(timestamp)
}

private fun formatTime(hour: Int, minute: Int): String {
    return "%02d:%02d".format(hour, minute)
}

private fun resolveDateTimeMillis(
    selectedDateMillis: Long,
    hour: Int,
    minute: Int
): Long {
    // Combina la fecha elegida y la hora elegida en un timestamp unico.
    val calendar = Calendar.getInstance().apply {
        timeInMillis = selectedDateMillis
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}
