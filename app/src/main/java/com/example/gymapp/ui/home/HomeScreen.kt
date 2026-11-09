package com.example.gymapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.domain.model.Rutina

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    factory: ViewModelProvider.Factory,
    onAddRutina: () -> Unit,
    onEditRutina: (String) -> Unit,
    onDetailRutina: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var rutinaAEliminar by remember { mutableStateOf<Rutina?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadRutinas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rutinas") },
                actions = {
                    TextButton(onClick = onAddRutina) {
                        Text("Agregar")
                    }
                    TextButton(
                        onClick = {
                            viewModel.logout()
                            onLogout()
                        }
                    ) {
                        Text("Salir")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                HomeUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                HomeUiState.Empty -> Text(
                    text = "No hay rutinas registradas",
                    modifier = Modifier.align(Alignment.Center)
                )

                is HomeUiState.Error -> Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = viewModel::loadRutinas) {
                        Text("Reintentar")
                    }
                }

                is HomeUiState.Success -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.rutinas, key = { it.id }) { rutina ->
                        RutinaCard(
                            rutina = rutina,
                            onDetail = { onDetailRutina(rutina.id) },
                            onEdit = { onEditRutina(rutina.id) },
                            onDelete = { rutinaAEliminar = rutina }
                        )
                    }
                }
            }
        }
    }

    if (rutinaAEliminar != null) {
        AlertDialog(
            onDismissRequest = { rutinaAEliminar = null },
            title = { Text("Eliminar rutina") },
            text = { Text("¿Seguro que deseas eliminar \"${rutinaAEliminar?.nombre}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        rutinaAEliminar?.let { viewModel.deleteRutina(it.id) }
                        rutinaAEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { rutinaAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun RutinaCard(
    rutina: Rutina,
    onDetail: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = rutina.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${rutina.grupoMuscular} · ${rutina.series} series x " +
                    "${rutina.repeticiones} reps · ${rutina.duracionMinutos} min",
                style = MaterialTheme.typography.bodyMedium
            )
            if (rutina.esAvanzado) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Avanzado",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onDetail) {
                    Text("Ver")
                }
                TextButton(onClick = onEdit) {
                    Text("Editar")
                }
                TextButton(onClick = onDelete) {
                    Text("Eliminar")
                }
            }
        }
    }
}
