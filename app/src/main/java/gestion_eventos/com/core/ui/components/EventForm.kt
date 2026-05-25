package gestion_eventos.com.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EventForm(
    title: String,
    description: String,
    location: String,
    modifier: Modifier = Modifier,
    onTitleChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onLocationChange: (String) -> Unit = {}
) {
    Column(modifier = modifier) {
        CustomTextField(value = title, label = "Titulo", onValueChange = onTitleChange)
        Spacer(modifier = Modifier.height(12.dp))
        CustomTextField(value = description, label = "Descripcion", onValueChange = onDescriptionChange)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = location,
            onValueChange = onLocationChange,
            label = { Text(text = "Ubicacion") }
        )
    }
}
