package gestion_eventos.com.presentation.social

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import gestion_eventos.com.core.ui.components.PrimaryButton
import gestion_eventos.com.domain.model.Shareable

@Composable
fun ShareEventButton(
    shareable: Shareable,
    modifier: Modifier = Modifier,
    onShare: (String) -> Unit = {}
) {
    PrimaryButton(
        text = "Compartir",
        modifier = modifier,
        onClick = { onShare(shareable.shareMessage()) }
    )
}
