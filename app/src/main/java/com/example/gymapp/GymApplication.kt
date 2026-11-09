package com.example.gymapp

import android.app.Application
import com.example.gymapp.di.AppContainer

class GymApplication : Application() {
    val appContainer: AppContainer by lazy { AppContainer(this) }
}
