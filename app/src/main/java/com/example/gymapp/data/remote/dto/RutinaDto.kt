package com.example.gymapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RutinaDto(
    val id: String,
    val nombre: String,
    val grupoMuscular: String,
    val series: Int,
    val repeticiones: Int,
    val esAvanzado: Boolean,
    val duracionMinutos: Int
)
