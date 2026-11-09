package com.example.gymapp.ui.auth

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

internal fun validateEmail(email: String): String? = when {
    email.isBlank() -> "El correo es obligatorio"
    !EMAIL_REGEX.matches(email.trim()) -> "Ingresa un correo válido"
    else -> null
}

internal fun validatePassword(password: String): String? = when {
    password.isBlank() -> "La contraseña es obligatoria"
    password.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
    else -> null
}
