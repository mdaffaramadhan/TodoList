package com.daffa0049.todomahasiswaapp.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.daffa0049.todomahasiswaapp.data.Tugas
import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModel
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTugasScreen(
    navController: NavController,
    viewModel: TugasViewModel,
    tugasId: Int? = null
) {
    var namaTugas by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var isDialogOpen by remember { mutableStateOf(false) }

    // Mengambil tugas lama jika ada
    val tugasLama by remember(tugasId) {
        if (tugasId != null) {
            viewModel.getTugasById(tugasId)
        } else {
            flowOf(null)
        }
    }.collectAsState(initial = null)

    // Mengupdate UI dengan data tugas lama
    LaunchedEffect(tugasLama) {
        tugasLama?.let {
            namaTugas = it.nama
            deadline = it.deadline
        }
    }

    // Fungsi untuk membuka dialog pemilih tanggal
    val openDatePicker = { context: Context ->
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val date = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.time
                deadline = SimpleDateFormat("yyyy-MM-dd").format(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    // Scaffold dengan tombol simpan dan hapus
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (tugasId != null) "Edit Tugas" else "Tambah Tugas")
                }
            )
        }
    ) { padding ->
        val context = LocalContext.current // Ambil context di dalam composable

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = namaTugas,
                onValueChange = { namaTugas = it },
                label = { Text("Nama Tugas") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Hapus Input untuk Deadline Manual
            // Tampilkan hanya tanggal yang dipilih
            OutlinedTextField(
                value = deadline,
                onValueChange = {},
                label = { Text("Deadline") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tombol Pilih Tanggal
            Button(
                onClick = { openDatePicker(context) },  // Panggil openDatePicker dengan context
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pilih Tanggal")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol simpan atau tambah tugas
            Button(
                onClick = {
                    if (namaTugas.isNotBlank() && deadline.isNotBlank()) {
                        if (tugasId != null) {
                            // Update tugas lama
                            viewModel.updateTugas(
                                tugasLama!!.copy(
                                    nama = namaTugas,
                                    deadline = deadline
                                )
                            )
                        } else {
                            // Tambah tugas baru
                            viewModel.tambahTugas(
                                Tugas(nama = namaTugas, deadline = deadline)
                            )
                        }
                        navController.popBackStack() // Navigasi kembali setelah simpan
                    }
                },
                modifier = Modifier.align(Alignment.End),
                enabled = namaTugas.isNotBlank() && deadline.isNotBlank()
            ) {
                Text("Simpan")
            }

            // Tombol hapus tugas
            if (tugasId != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { isDialogOpen = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus Tugas", color = MaterialTheme.colorScheme.onError)
                }
            }

            // Dialog konfirmasi hapus
            if (isDialogOpen) {
                AlertDialog(
                    onDismissRequest = { isDialogOpen = false },
                    title = { Text("Konfirmasi") },
                    text = { Text("Apakah yakin ingin menghapus tugas ini?") },
                    confirmButton = {
                        TextButton(onClick = {
                            tugasLama?.let {
                                viewModel.hapusTugas(it)
                            }
                            isDialogOpen = false
                        }) {
                            Text("Ya")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { isDialogOpen = false }) {
                            Text("Batal")
                        }
                    }
                )
            }
        }
    }
}




