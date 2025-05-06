package com.daffa0049.todomahasiswaapp.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.ImageButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daffa0049.todomahasiswaapp.R
import com.daffa0049.todomahasiswaapp.data.Tugas
import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModel
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
    var prioritas by remember { mutableStateOf("Urgent") } // Default pilihan prioritas
    val prioritasList = listOf("Urgent", "Biasa", "Santai") // Daftar prioritas

    // Menggunakan LocalContext di dalam fungsi Composable
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Fungsi untuk membuka dialog pemilih tanggal
    val openDatePicker = {
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
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Input Nama Tugas
            OutlinedTextField(
                value = namaTugas,
                onValueChange = { namaTugas = it },
                label = { Text("Nama Tugas") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = deadline,
                    onValueChange = {},
                    label = { Text("Deadline") },
                    modifier = Modifier.weight(1f),
                    readOnly = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Tombol kalender dengan latar belakang hijau muda
                IconButton(
                    onClick = { openDatePicker() },
                    modifier = Modifier
                        .size(40.dp) // Ukuran ikon tombol
                        .background(Color(0xFFB2FF59), shape = CircleShape) // Warna latar belakang hijau muda dan bentuk lingkaran
                        .padding(8.dp) // Padding di dalam tombol
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo_kalender), // Ganti dengan gambar ikon kalender
                        contentDescription = "Pilih Tanggal",
                        tint = Color.White // Warna ikon, bisa disesuaikan
                    )
                }
            }


            Spacer(modifier = Modifier.height(8.dp))

            // Pilihan Prioritas menggunakan RadioButton
            Text("Prioritas")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                prioritasList.forEach { option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = prioritas == option,
                            onClick = { prioritas = option }
                        )
                        Text(option)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol simpan atau tambah tugas
            Button(
                onClick = {
                    if (namaTugas.isNotBlank() && deadline.isNotBlank()) {
                        if (tugasId != null) {
                            // Update tugas lama
                            viewModel.updateTugas(
                                Tugas(
                                    id = tugasId,
                                    nama = namaTugas,
                                    deadline = deadline,
                                    prioritas = prioritas
                                )
                            )
                        } else {
                            // Tambah tugas baru
                            viewModel.tambahTugas(
                                Tugas(nama = namaTugas, deadline = deadline, prioritas = prioritas)
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
        }
    }
}
