package com.daffa0049.todomahasiswaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModel

@Composable
fun DaftarTugasScreen(
    navController: NavController,
    viewModel: TugasViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val daftarTugas by viewModel.semuaTugas.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("form_tugas")
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Daftar Tugas", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            if (daftarTugas.isEmpty()) {
                Text("Belum ada tugas")
            } else {
                daftarTugas.forEach { tugas ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(tugas.nama, style = MaterialTheme.typography.bodyLarge)
                            Text("Deadline: ${tugas.deadline}")
                            Text("Status: ${if (tugas.selesai) "Selesai" else "Belum"}")
                        }
                    }
                }
            }
        }
    }
}
