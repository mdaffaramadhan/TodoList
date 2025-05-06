package com.daffa0049.todomahasiswaapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.daffa0049.todomahasiswaapp.data.Tugas
import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModel

@Composable
fun DaftarTugasScreen(
    navController: NavController,
    viewModel: TugasViewModel = viewModel()
) {
    val daftarTugas by viewModel.semuaTugas.collectAsState()
    var tugasUntukDihapus by remember { mutableStateOf<Tugas?>(null) }

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
                            Row(modifier = Modifier.padding(top = 8.dp)) {
                                Button(
                                    onClick = {
                                        tugasUntukDihapus = tugas
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Hapus", color = MaterialTheme.colorScheme.onError)
                                }
                            }
                        }
                    }
                }
            }

            if (tugasUntukDihapus != null) {
                AlertDialog(
                    onDismissRequest = { tugasUntukDihapus = null },
                    title = { Text("Konfirmasi") },
                    text = { Text("Apakah yakin ingin menghapus tugas ini?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.hapusTugas(tugasUntukDihapus!!)
                            tugasUntukDihapus = null
                        }) {
                            Text("Ya")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            tugasUntukDihapus = null
                        }) {
                            Text("Batal")
                        }
                    }
                )
            }
        }
    }
}
