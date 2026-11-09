package com.example.gymapp.data.remote

import com.example.gymapp.data.remote.dto.AuthResponseDto
import com.example.gymapp.data.remote.dto.LoginRequestDto
import com.example.gymapp.data.remote.dto.RutinaDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: LoginRequestDto): AuthResponseDto

    @GET("rutinas")
    suspend fun getRutinas(): List<RutinaDto>

    @GET("rutinas/{id}")
    suspend fun getRutina(@Path("id") id: String): RutinaDto

    @POST("rutinas")
    suspend fun createRutina(@Body rutina: RutinaDto): RutinaDto

    @PUT("rutinas/{id}")
    suspend fun updateRutina(@Path("id") id: String, @Body rutina: RutinaDto): RutinaDto

    @DELETE("rutinas/{id}")
    suspend fun deleteRutina(@Path("id") id: String)
}
