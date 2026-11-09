package com.example.gymapp.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    rutinaId: String,
    factory: ViewModelProvider.Factory,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(rutinaId) {
        viewModel.load(rutinaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de rutina") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                uiState.error != null -> Text(
                    text = uiState.error.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> uiState.rutina?.let { rutina ->
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = rutina.nombre,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        DetailRow("Grupo muscular", rutina.grupoMuscular)
                        DetailRow("Series", rutina.series.toString())
                        DetailRow("Repeticiones", rutina.repeticiones.toString())
                        DetailRow("Duración", "${rutina.duracionMinutos} minutos")
                        DetailRow(
                            "Nivel",
                            if (rutina.esAvanzado) "Avanzado" else "Principiante"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
