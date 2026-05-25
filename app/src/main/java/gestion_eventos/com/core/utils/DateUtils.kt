package gestion_eventos.com.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    // Centraliza formatos de fecha y hora usados en la interfaz.
    private const val DISPLAY_PATTERN = "dd/MM/yyyy HH:mm"

    fun formatTimestamp(timestamp: Long): String {
        return SimpleDateFormat(DISPLAY_PATTERN, Locale.getDefault()).format(Date(timestamp))
    }
}
