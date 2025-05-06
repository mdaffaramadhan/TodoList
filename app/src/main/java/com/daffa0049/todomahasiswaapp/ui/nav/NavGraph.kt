package com.daffa0049.todomahasiswaapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.daffa0049.todomahasiswaapp.ui.screens.DaftarTugasScreen
import com.daffa0049.todomahasiswaapp.ui.screens.FormTugasScreen
import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModel

sealed class Screen(val route: String) {
    object DaftarTugas : Screen("daftar_tugas")
    object FormTugas : Screen("form_tugas?id={id}") {
        fun createRoute(id: Int?) = if (id == null) "form_tugas" else "form_tugas?id=$id"
    }
}

@Composable
fun TodoNavGraph(
    navController: NavHostController,
    viewModel: TugasViewModel,
    modifier: Modifier = Modifier
)
 {
    NavHost(
        navController = navController,
        startDestination = Screen.DaftarTugas.route,
        modifier = modifier
    ) {
        composable(Screen.DaftarTugas.route) {
            DaftarTugasScreen(navController)
        }

        composable(
            route = Screen.FormTugas.route,
            arguments = listOf(navArgument("id") {
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            FormTugasScreen(navController, viewModel, id)
        }


        composable(Screen.FormTugas.route) {
            FormTugasScreen(
                navController = navController,
                viewModel = viewModel()
            )
        }
    }
}

