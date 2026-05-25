package gestion_eventos.com.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    state: AuthState,
    modifier: Modifier = Modifier,
    onRegisterClick: (String, String, String) -> Unit,
    onLoginClick: () -> Unit
) {
    // Pantalla para crear una cuenta con nombre, correo y contrasena.
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AuthBackground(modifier = modifier) {
        AuthHeader(
            title = "Crea tu cuenta",
            subtitle = "Usa correo y contrasena para empezar a publicar eventos."
        )

        AuthCard {
            AuthTextField(
                value = name,
                label = "Nombre",
                leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null) },
                onValueChange = { name = it }
            )
            AuthTextField(
                value = email,
                label = "Correo",
                leadingIcon = { Icon(Icons.Rounded.AlternateEmail, contentDescription = null) },
                keyboardType = KeyboardType.Email,
                onValueChange = { email = it }
            )
            AuthTextField(
                value = password,
                label = "Contrasena",
                leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
                isPassword = true,
                keyboardType = KeyboardType.Password,
                onValueChange = { password = it }
            )

            ErrorText(message = state.error)

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                onClick = { onRegisterClick(name, email, password) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(imageVector = Icons.Rounded.PersonAdd, contentDescription = null)
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "Crear cuenta"
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes una cuenta?",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(onClick = onLoginClick) {
                    Text(text = "Inicia sesion")
                }
            }
        }
    }
}
