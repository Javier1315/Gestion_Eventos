package gestion_eventos.com.core.utils

import android.content.Context
import android.net.Uri
import java.io.File

object LocalImageStorage {
    // Guarda portadas seleccionadas en almacenamiento local de la app.
    fun saveEventCover(context: Context, sourceUri: Uri): String? {
        val coversDir = File(context.filesDir, "event_covers").apply {
            if (!exists()) mkdirs()
        }
        val targetFile = File(coversDir, "cover_${System.currentTimeMillis()}.jpg")

        return runCatching {
            context.contentResolver.openInputStream(sourceUri)?.use { input ->
                targetFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            } ?: return null

            Uri.fromFile(targetFile).toString()
        }.getOrNull()
    }
}
