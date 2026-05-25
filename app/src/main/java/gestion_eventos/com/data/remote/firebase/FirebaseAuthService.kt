package gestion_eventos.com.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import gestion_eventos.com.domain.model.User
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthService(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    // Maneja el acceso a Firebase Auth para correo, registro y Google.
    suspend fun login(email: String, password: String): User {
        val firebaseUser = suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        continuation.resume(user)
                    } else {
                        continuation.resumeWithException(IllegalStateException("No se encontro el usuario."))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        return firebaseUser.toDomainUser()
    }

    suspend fun register(name: String, email: String, password: String): User {
        val firebaseUser = suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        continuation.resume(user)
                    } else {
                        continuation.resumeWithException(IllegalStateException("No se pudo crear el usuario."))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        suspendCoroutine { continuation ->
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            firebaseUser.updateProfile(profileUpdates)
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
        }

        return firebaseUser.toDomainUser(fallbackName = name)
    }

    suspend fun loginWithProvider(providerName: String): User {
        throw UnsupportedOperationException("El inicio de sesion social aun no esta habilitado.")
    }

    suspend fun loginWithGoogle(idToken: String): User {
        // Valida el token de Google y crea una sesion de Firebase.
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val firebaseUser = suspendCoroutine { continuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        continuation.resume(user)
                    } else {
                        continuation.resumeWithException(IllegalStateException("No se encontro el usuario de Google."))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

        return firebaseUser.toDomainUser()
    }

    suspend fun logout() {
        firebaseAuth.signOut()
    }

    fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toDomainUser()
    }

    private fun com.google.firebase.auth.FirebaseUser.toDomainUser(
        fallbackName: String? = null
    ): User {
        // Convierte el usuario de Firebase al modelo usado por la app.
        val safeEmail = email.orEmpty()
        return User(
            id = uid,
            name = displayName ?: fallbackName ?: safeEmail.substringBefore("@").ifBlank { "Usuario" },
            email = safeEmail
        )
    }
}
