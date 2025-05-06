package com.daffa0049.todomahasiswaapp.ui.screens

import android.graphics.Paint.Align
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.daffa0049.todomahasiswaapp.data.Tugas
import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTugasScreen(
    navController: NavController,
    viewModel: TugasViewModel
) {
    var namaTugas by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tambah Tugas") })
        }
    ) { padding ->
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
            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Deadline (cth: 2025-05-10)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (namaTugas.isNotBlank() && deadline.isNotBlank()) {
                        val tugasBaru = Tugas(nama = namaTugas, deadline = deadline)
                        viewModel.tambahTugas(tugasBaru)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Simpan")
            }
        }
    }
}
