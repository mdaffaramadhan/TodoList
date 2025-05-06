    package com.daffa0049.todomahasiswaapp.ui.screens

    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.lazy.grid.GridCells
    import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
    import androidx.compose.foundation.lazy.grid.items
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.ArrowDropDown
    import androidx.compose.material3.Card
    import androidx.compose.material3.ExposedDropdownMenuBox
    import androidx.compose.material3.DropdownMenuItem
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.FloatingActionButton
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextField
    import androidx.compose.material3.TopAppBar
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.unit.dp
    import androidx.navigation.NavController
    import com.daffa0049.todomahasiswaapp.R
    import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DaftarTugasScreen(
        navController: NavController,
        viewModel: TugasViewModel
    ) {
        val daftarTugas by viewModel.semuaTugas.collectAsState()
        var filterTugas by remember { mutableStateOf("Semua") }
        var expanded by remember { mutableStateOf(false) }

        val filterOptions = listOf("Semua", "Urgent", "Biasa", "Santai")

        val filteredTugas = when (filterTugas) {
            "Urgent" -> daftarTugas.filter { it.prioritas == "Urgent" }
            "Biasa" -> daftarTugas.filter { it.prioritas == "Biasa" }
            "Santai" -> daftarTugas.filter { it.prioritas == "Santai" }
            else -> daftarTugas
        }

        // Variabel untuk kontrol tampilan grid/list
        var isGrid by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Daftar Tugas") },
                    actions = {
                        Row {
                            // Tombol untuk mengubah tampilan grid/list
                            IconButton(
                                onClick = { isGrid = !isGrid },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                // Menampilkan ikon list/grid
                                Icon(
                                    painter = painterResource(id = if (isGrid) R.drawable.baseline_list_24 else R.drawable.baseline_grid_view_24),
                                    contentDescription = "Tampilan Grid/List",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                )
            },
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
                // Dropdown filter untuk memilih prioritas
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = filterTugas,
                        onValueChange = {},
                        label = { Text("Filter Tugas") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Dropdown"
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        filterOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    filterTugas = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (filteredTugas.isEmpty()) {
                    Text("Belum ada tugas")
                } else {
                    if (isGrid) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxWidth(),
                            content = {
                                items(filteredTugas) { tugas ->
                                    val prioritasColor = when (tugas.prioritas) {
                                        "Urgent" -> Color.Red
                                        "Biasa" -> Color.Yellow
                                        "Santai" -> Color.Green
                                        else -> Color.Gray
                                    }

                                    Card(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                navController.navigate("form_tugas?id=${tugas.id}")
                                            }
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(tugas.nama, style = MaterialTheme.typography.bodyLarge)
                                            Text("Deadline: ${tugas.deadline}")
                                            Text("Prioritas: ${tugas.prioritas}", color = prioritasColor)
                                        }
                                    }
                                }
                            }
                        )
                    } else {
                        filteredTugas.forEach { tugas ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        navController.navigate("form_tugas?id=${tugas.id}")
                                    }
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(tugas.nama, style = MaterialTheme.typography.bodyLarge)
                                    Text("Deadline: ${tugas.deadline}")
                                    val prioritasColor = when (tugas.prioritas) {
                                        "Urgent" -> Color.Red
                                        "Biasa" -> Color.Yellow
                                        "Santai" -> Color.Green
                                        else -> Color.Gray
                                    }

                                    Text(
                                        "Prioritas: ${tugas.prioritas}",
                                        color = prioritasColor
                                    )

                                }
                            }
                        }
                    }
                }
            }
        }
    }
