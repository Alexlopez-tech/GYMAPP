package com.example.gymapp.data.repository

import com.example.gymapp.data.local.SessionManager
import com.example.gymapp.data.remote.ApiService
import com.example.gymapp.data.remote.dto.LoginRequestDto
import com.example.gymapp.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        val response = apiService.login(LoginRequestDto(email = email, password = password))
        sessionManager.saveToken(response.token)
    }

    override suspend fun register(email: String, password: String): Result<Unit> = runCatching {
        val response = apiService.register(LoginRequestDto(email = email, password = password))
        sessionManager.saveToken(response.token)
    }

    override fun logout() {
        sessionManager.clearToken()
    }

    override fun isLoggedIn(): Boolean = !sessionManager.getToken().isNullOrBlank()
}
