    package com.daffa0049.todomahasiswaapp.ui.nav

    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavHostController
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.navArgument
    import com.daffa0049.todomahasiswaapp.data.UserPreferences
    import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModel
    import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModelFactory
    import com.daffa0049.todomahasiswaapp.ui.screens.DaftarTugasScreen
    import com.daffa0049.todomahasiswaapp.ui.screens.FormTugasScreen

    sealed class Screen(val route: String) {
        object DaftarTugas : Screen("daftar_tugas")
        object FormTugas : Screen("form_tugas?id={id}") {
            fun createRoute(id: Int?) = if (id == null) "form_tugas" else "form_tugas?id=$id"
        }
    }

    @Composable
    fun TodoNavGraph(
        navController: NavHostController,
        viewModelFactory: TugasViewModelFactory,
        prefs: UserPreferences, // Tambahkan parameter prefs
        modifier: Modifier = Modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.DaftarTugas.route,
            modifier = modifier
        ) {
            composable(Screen.DaftarTugas.route) {
                val viewModel: TugasViewModel = viewModel(factory = viewModelFactory)
                viewModel.setUserPreferences(prefs)  // Pastikan ini dipanggil
                DaftarTugasScreen(navController = navController, viewModel = viewModel, userPreferences = prefs)
            }

            composable(
                route = Screen.FormTugas.route,
                arguments = listOf(navArgument("id") {
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                val viewModel: TugasViewModel = viewModel(factory = viewModelFactory)
                viewModel.setUserPreferences(prefs)  // Pastikan ini dipanggil
                FormTugasScreen(navController, viewModel, id)
            }
        }
    }

