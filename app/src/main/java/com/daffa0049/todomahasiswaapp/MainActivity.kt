package com.daffa0049.todomahasiswaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.daffa0049.todomahasiswaapp.data.TugasDatabase
import com.daffa0049.todomahasiswaapp.data.UserPreferences
import com.daffa0049.todomahasiswaapp.repository.TugasRepository
import com.daffa0049.todomahasiswaapp.ui.nav.TodoNavGraph
import com.daffa0049.todomahasiswaapp.ui.theme.TodoMahasiswaAppTheme
import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi UserPreferences, Database, dan Repository
        val prefs = UserPreferences(this)
        val database = TugasDatabase.getDatabase(this)
        val repository = TugasRepository(database.tugasDao())
        val viewModelFactory = TugasViewModelFactory(repository)

        // Set konten untuk UI
        setContent {
            TodoMahasiswaAppTheme {
                val navController = rememberNavController()

                // Kirim ViewModel dan UserPreferences ke NavGraph
                TodoNavGraph(
                    navController = navController,
                    viewModelFactory = viewModelFactory,
                    prefs = prefs  // Kirim prefs ke NavGraph
                )
            }
        }
    }
}

