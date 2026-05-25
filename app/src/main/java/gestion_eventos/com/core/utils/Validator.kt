package gestion_eventos.com.core.utils

object Validator {
    fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isNotBlank(value: String): Boolean {
        return value.isNotBlank()
    }
}
