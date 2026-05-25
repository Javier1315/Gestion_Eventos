package gestion_eventos.com.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    // Boton reutilizable para acciones principales de la app.
    Button(
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text)
    }
}
