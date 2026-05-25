package gestion_eventos.com.presentation.social

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import gestion_eventos.com.core.ui.components.RatingStars

@Composable
fun RatingSection(
    rating: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = "Calificacion", style = MaterialTheme.typography.titleMedium)
        RatingStars(rating = rating)
    }
}
