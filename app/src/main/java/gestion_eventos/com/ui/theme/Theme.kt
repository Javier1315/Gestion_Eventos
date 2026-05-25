package gestion_eventos.com.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EventPurple,
    secondary = EventMint,
    tertiary = EventPurpleSoft,
    background = EventInk,
    surface = EventPanel,
    surfaceVariant = EventPanelSoft,
    outline = EventLine,
    onPrimary = Color.White,
    onSecondary = EventInk,
    onBackground = EventText,
    onSurface = EventText,
    onSurfaceVariant = EventMuted
)

@Composable
fun Gestion_EventosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
