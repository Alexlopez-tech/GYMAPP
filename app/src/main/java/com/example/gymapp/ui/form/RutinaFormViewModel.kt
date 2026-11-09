package com.example.gymapp.ui.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.domain.model.Rutina
import com.example.gymapp.domain.repository.RutinaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RutinaFormUiState(
    val nombre: String = "",
    val grupoMuscular: String = "",
    val series: String = "",
    val repeticiones: String = "",
    val duracionMinutos: String = "",
    val esAvanzado: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)

class RutinaFormViewModel(
    private val rutinaRepository: RutinaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RutinaFormUiState())
    val uiState: StateFlow<RutinaFormUiState> = _uiState.asStateFlow()

    private var loadedId: String? = null

    fun load(id: String) {
        if (loadedId == id) return
        loadedId = id
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            rutinaRepository.getRutina(id)
                .onSuccess { rutina ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            nombre = rutina.nombre,
                            grupoMuscular = rutina.grupoMuscular,
                            series = rutina.series.toString(),
                            repeticiones = rutina.repeticiones.toString(),
                            duracionMinutos = rutina.duracionMinutos.toString(),
                            esAvanzado = rutina.esAvanzado
                        )
                    }
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

    fun onNombreChange(value: String) = _uiState.update { it.copy(nombre = value, error = null) }

    fun onGrupoMuscularChange(value: String) =
        _uiState.update { it.copy(grupoMuscular = value, error = null) }

    fun onSeriesChange(value: String) = _uiState.update { it.copy(series = value, error = null) }

    fun onRepeticionesChange(value: String) =
        _uiState.update { it.copy(repeticiones = value, error = null) }

    fun onDuracionChange(value: String) =
        _uiState.update { it.copy(duracionMinutos = value, error = null) }

    fun onEsAvanzadoChange(value: Boolean) = _uiState.update { it.copy(esAvanzado = value) }

    fun save(id: String?) {
        val state = _uiState.value
        val series = state.series.toIntOrNull()
        val repeticiones = state.repeticiones.toIntOrNull()
        val duracion = state.duracionMinutos.toIntOrNull()

        val validationError = when {
            state.nombre.isBlank() -> "El nombre es obligatorio"
            state.grupoMuscular.isBlank() -> "El grupo muscular es obligatorio"
            series == null || series <= 0 -> "Las series deben ser un número mayor a 0"
            repeticiones == null || repeticiones <= 0 ->
                "Las repeticiones deben ser un número mayor a 0"
            duracion == null || duracion <= 0 -> "La duración debe ser un número mayor a 0"
            else -> null
        }

        if (validationError != null || series == null || repeticiones == null || duracion == null) {
            _uiState.update { it.copy(error = validationError) }
            return
        }

        val rutina = Rutina(
            id = id ?: "",
            nombre = state.nombre.trim(),
            grupoMuscular = state.grupoMuscular.trim(),
            series = series,
            repeticiones = repeticiones,
            esAvanzado = state.esAvanzado,
            duracionMinutos = duracion
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            val result = if (id == null) {
                rutinaRepository.createRutina(rutina)
            } else {
                rutinaRepository.updateRutina(rutina)
            }
            result
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false, isSaved = true) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            error = error.message ?: "No se pudo guardar la rutina"
                        )
                    }
                }
        }
    }
}
