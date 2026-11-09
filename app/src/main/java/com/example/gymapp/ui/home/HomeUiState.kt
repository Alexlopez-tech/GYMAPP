package com.example.gymapp.ui.home

import com.example.gymapp.domain.model.Rutina

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val rutinas: List<Rutina>) : HomeUiState
    data object Empty : HomeUiState
    data class Error(val message: String) : HomeUiState
}
