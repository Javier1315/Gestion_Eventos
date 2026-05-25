package gestion_eventos.com.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RatingStars(
    rating: Int,
    modifier: Modifier = Modifier,
    maxRating: Int = 5
) {
    Row(modifier = modifier) {
        repeat(maxRating) { index ->
            Text(text = if (index < rating) "*" else "-")
        }
    }
}
