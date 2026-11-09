package com.example.gymapp.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gymapp.ui.auth.LoginViewModel
import com.example.gymapp.ui.auth.RegisterViewModel
import com.example.gymapp.ui.detail.DetailViewModel
import com.example.gymapp.ui.form.RutinaFormViewModel
import com.example.gymapp.ui.home.HomeViewModel

fun createViewModelFactory(container: AppContainer): ViewModelProvider.Factory = viewModelFactory {
    initializer<LoginViewModel> { LoginViewModel(container.authRepository) }
    initializer<RegisterViewModel> { RegisterViewModel(container.authRepository) }
    initializer<HomeViewModel> {
        HomeViewModel(container.rutinaRepository, container.authRepository)
    }
    initializer<DetailViewModel> { DetailViewModel(container.rutinaRepository) }
    initializer<RutinaFormViewModel> { RutinaFormViewModel(container.rutinaRepository) }
}
