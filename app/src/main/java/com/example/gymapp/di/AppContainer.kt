package com.example.gymapp.di

import android.content.Context
import com.example.gymapp.data.local.SessionManager
import com.example.gymapp.data.remote.ApiService
import com.example.gymapp.data.remote.AuthInterceptor
import com.example.gymapp.data.repository.AuthRepositoryImpl
import com.example.gymapp.data.repository.RutinaRepositoryImpl
import com.example.gymapp.domain.repository.AuthRepository
import com.example.gymapp.domain.repository.RutinaRepository
import com.example.gymapp.data.remote.KotlinxSerializationConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class AppContainer(context: Context) {

    val sessionManager = SessionManager(context)

    private val json = Json { ignoreUnknownKeys = true }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(sessionManager))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/")
        .client(okHttpClient)
        .addConverterFactory(KotlinxSerializationConverterFactory.create(json))
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    val authRepository: AuthRepository = AuthRepositoryImpl(apiService, sessionManager)
    val rutinaRepository: RutinaRepository = RutinaRepositoryImpl(apiService)
}
