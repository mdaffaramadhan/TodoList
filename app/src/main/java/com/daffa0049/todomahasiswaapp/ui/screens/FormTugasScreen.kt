package com.daffa0049.todomahasiswaapp.ui.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.util.Calendar

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTugasScreen(
    navController: NavController,
    viewModel: TugasViewModel,
    tugasId: Int? = null
) {

    val tugas by viewModel.getTugasById(tugasId ?: -1).collectAsState(initial = null)

    var namaTugas by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var prioritas by remember { mutableStateOf("Urgent") }
    val prioritasList = listOf("Urgent", "Biasa", "Santai")

    var errorMessage by remember { mutableStateOf("") }


    if (tugasId != null && tugas != null) {

        namaTugas = tugas?.nama ?: ""
        deadline = tugas?.deadline ?: ""
        prioritas = tugas?.prioritas ?: "Urgent"
    }

    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

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

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "Kembali"
                        )
                    }
                },

                title = {
                    Text(if (tugasId != null) "Edit Tugas" else "Tambah Tugas")
                },
                actions = {
                    if (tugasId != null && tugas != null) {
                        IconButton(onClick = {
                            showDialog = true
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24),
                                contentDescription = "Hapus Tugas",
                                tint = Color.Red
                            )
                        }
                    }
                }
            )
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = namaTugas,
                onValueChange = {
                    namaTugas = it
                    errorMessage = ""
                },
                label = { Text("Nama Tugas") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage.isNotEmpty()
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

                IconButton(
                    onClick = { openDatePicker() },
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_edit_calendar_24),
                        contentDescription = "Pilih Tanggal",
                        tint = Color.White
                    )
                }
            }
            if (showDialog && tugas != null) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Konfirmasi Hapus") },
                    text = { Text("Yakin ingin menghapus tugas ini?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.hapusTugas(tugas!!)
                                showDialog = false
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Hapus", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text("Batal")
                        }
                    }
                )
            }



            Spacer(modifier = Modifier.height(8.dp))

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

            Button(
                onClick = {
                    if (namaTugas.isBlank() || deadline.isBlank()) {
                        errorMessage = "Nama Tugas dan Deadline harus diisi"
                        showDialog = true
                    } else {
                        errorMessage = ""
                        if (tugasId != null) {
                            viewModel.updateTugas(
                                Tugas(
                                    id = tugasId,
                                    nama = namaTugas,
                                    deadline = deadline,
                                    prioritas = prioritas
                                )
                            )
                        } else {
                            viewModel.tambahTugas(
                                Tugas(nama = namaTugas, deadline = deadline, prioritas = prioritas)
                            )
                        }
                        navController.popBackStack()
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
