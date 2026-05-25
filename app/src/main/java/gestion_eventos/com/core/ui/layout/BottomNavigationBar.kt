package gestion_eventos.com.core.ui.layout

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Text(text = "E") },
            label = { Text(text = "Eventos") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Text(text = "H") },
            label = { Text(text = "Historial") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Text(text = "P") },
            label = { Text(text = "Perfil") }
        )
    }
}
