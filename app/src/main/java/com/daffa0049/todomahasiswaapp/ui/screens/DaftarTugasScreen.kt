package com.daffa0049.todomahasiswaapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daffa0049.todomahasiswaapp.ui.nav.Screen
import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModel

@Composable
fun DaftarTugasScreen(
    navController: NavController,
    viewModel: TugasViewModel
) {
    val daftarTugas by viewModel.semuaTugas.collectAsState()

    // Menentukan warna berdasarkan prioritas
    fun getPrioritasColor(prioritas: String): Color {
        return when (prioritas) {
            "Urgent" -> Color.Red
            "Biasa" -> Color.Yellow
            "Santai" -> Color.Green
            else -> Color.Gray // Default warna
        }
    }

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
                            .clickable {
                                navController.navigate(Screen.FormTugas.createRoute(tugas.id))
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(tugas.nama, style = MaterialTheme.typography.bodyLarge)

                            // Menampilkan deadline tugas
                            Text("Deadline: ${tugas.deadline}")

                            // Menampilkan prioritas dengan warna yang sesuai
                            Text(
                                text = "Prioritas: ${tugas.prioritas}",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = getPrioritasColor(tugas.prioritas) // Mengatur warna berdasarkan prioritas
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
