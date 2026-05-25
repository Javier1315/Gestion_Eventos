package gestion_eventos.com.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EventColorScheme = lightColorScheme(
    primary = EventPrimary,
    secondary = EventSecondary,
    background = EventBackground,
    surface = EventSurface,
    onPrimary = Color.White
)

@Composable
fun EventAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EventColorScheme,
        typography = EventTypography,
        content = content
    )
}
