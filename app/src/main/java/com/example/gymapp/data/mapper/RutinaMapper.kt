package com.example.gymapp.data.mapper

import com.example.gymapp.data.remote.dto.RutinaDto
import com.example.gymapp.domain.model.Rutina

object RutinaMapper {

    fun RutinaDto.toDomain(): Rutina = Rutina(
        id = id,
        nombre = nombre,
        grupoMuscular = grupoMuscular,
        series = series,
        repeticiones = repeticiones,
        esAvanzado = esAvanzado,
        duracionMinutos = duracionMinutos
    )

    fun Rutina.toDto(): RutinaDto = RutinaDto(
        id = id,
        nombre = nombre,
        grupoMuscular = grupoMuscular,
        series = series,
        repeticiones = repeticiones,
        esAvanzado = esAvanzado,
        duracionMinutos = duracionMinutos
    )
}
