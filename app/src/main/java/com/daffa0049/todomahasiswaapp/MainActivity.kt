package com.daffa0049.todomahasiswaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.daffa0049.todomahasiswaapp.data.TugasDatabase
import com.daffa0049.todomahasiswaapp.repository.TugasRepository
import com.daffa0049.todomahasiswaapp.ui.nav.TodoNavGraph
import com.daffa0049.todomahasiswaapp.ui.theme.TodoMahasiswaAppTheme
import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModel
import com.daffa0049.todomahasiswaapp.viewmodel.TugasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = TugasDatabase.getDatabase(this)
        val repository = TugasRepository(database.tugasDao())
        val viewModelFactory = TugasViewModelFactory(repository)

        setContent {
            TodoMahasiswaAppTheme {
                val navController = rememberNavController()
                val viewModel: TugasViewModel = viewModel(factory = viewModelFactory)
                TodoNavGraph(navController = navController)
            }
        }
    }
}
