package com.example.gymapp.data.repository

import com.example.gymapp.data.mapper.RutinaMapper.toDomain
import com.example.gymapp.data.mapper.RutinaMapper.toDto
import com.example.gymapp.data.remote.ApiService
import com.example.gymapp.domain.model.Rutina
import com.example.gymapp.domain.repository.RutinaRepository

class RutinaRepositoryImpl(
    private val apiService: ApiService
) : RutinaRepository {

    override suspend fun getRutinas(): Result<List<Rutina>> = runCatching {
        apiService.getRutinas().map { it.toDomain() }
    }

    override suspend fun getRutina(id: String): Result<Rutina> = runCatching {
        apiService.getRutina(id).toDomain()
    }

    override suspend fun createRutina(rutina: Rutina): Result<Rutina> = runCatching {
        apiService.createRutina(rutina.toDto()).toDomain()
    }

    override suspend fun updateRutina(rutina: Rutina): Result<Rutina> = runCatching {
        apiService.updateRutina(rutina.id, rutina.toDto()).toDomain()
    }

    override suspend fun deleteRutina(id: String): Result<Unit> = runCatching {
        apiService.deleteRutina(id)
    }
}
