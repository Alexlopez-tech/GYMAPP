package com.example.gymapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.domain.model.Rutina
import com.example.gymapp.domain.repository.RutinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = true,
    val rutina: Rutina? = null,
    val error: String? = null
)

class DetailViewModel(
    private val rutinaRepository: RutinaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var loadedId: String? = null

    fun load(id: String) {
        if (loadedId == id) return
        loadedId = id
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            rutinaRepository.getRutina(id)
                .onSuccess { rutina ->
                    _uiState.update { it.copy(isLoading = false, rutina = rutina) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "No se pudo cargar la rutina"
                        )
                    }
                }
        }
    }
}
