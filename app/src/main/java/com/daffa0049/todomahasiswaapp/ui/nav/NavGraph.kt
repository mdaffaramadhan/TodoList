package com.daffa0049.todomahasiswaapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.daffa0049.todomahasiswaapp.ui.screens.DaftarTugasScreen
import com.daffa0049.todomahasiswaapp.ui.screens.FormTugasScreen

sealed class Screen(val route: String) {
    object DaftarTugas : Screen("daftar_tugas")
    object FormTugas : Screen("form_tugas")
}

@Composable
fun TodoNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.DaftarTugas.route,
        modifier = modifier
    ) {
        composable(Screen.DaftarTugas.route) {
            DaftarTugasScreen(navController)
        }

        composable(Screen.FormTugas.route) {
            // Akan ditambahkan nanti
        }

        composable(Screen.FormTugas.route) {
            FormTugasScreen(
                navController = navController,
                viewModel = viewModel()
            )
        }
    }
}

