package com.example.gymapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.domain.repository.AuthRepository
import com.example.gymapp.domain.repository.RutinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val rutinaRepository: RutinaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadRutinas() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            rutinaRepository.getRutinas()
                .onSuccess { rutinas ->
                    _uiState.value = if (rutinas.isEmpty()) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(rutinas)
                    }
                }
                .onFailure { error ->
                    _uiState.value = HomeUiState.Error(
                        error.message ?: "No se pudieron cargar las rutinas"
                    )
                }
        }
    }

    fun deleteRutina(id: String) {
        viewModelScope.launch {
            rutinaRepository.deleteRutina(id)
                .onSuccess { loadRutinas() }
                .onFailure { error ->
                    _uiState.value = HomeUiState.Error(
                        error.message ?: "No se pudo eliminar la rutina"
                    )
                }
        }
    }

    fun logout() {
        authRepository.logout()
    }
}
