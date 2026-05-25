package gestion_eventos.com

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import gestion_eventos.com.core.navigation.AppNavigation
import gestion_eventos.com.ui.theme.Gestion_EventosTheme

class MainActivity : ComponentActivity() {
    // Inicia la aplicacion Compose y aplica el tema principal.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Gestion_EventosTheme {
                AppNavigation()
            }
        }
    }
}
