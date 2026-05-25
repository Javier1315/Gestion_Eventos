package gestion_eventos.com.presentation.social

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import gestion_eventos.com.core.ui.components.CommentItem
import gestion_eventos.com.domain.model.Comment

@Composable
fun CommentsSection(
    comments: List<Comment>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = "Comentarios", style = MaterialTheme.typography.titleMedium)
        comments.forEach { comment ->
            CommentItem(comment = comment)
        }
    }
}
