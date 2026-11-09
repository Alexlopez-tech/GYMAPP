package com.example.gymapp.domain.model

data class Rutina(
    val id: String,
    val nombre: String,
    val grupoMuscular: String,
    val series: Int,
    val repeticiones: Int,
    val esAvanzado: Boolean,
    val duracionMinutos: Int
)
