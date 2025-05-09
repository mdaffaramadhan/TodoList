package com.daffa0049.todomahasiswaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daffa0049.todomahasiswaapp.data.Tugas
import com.daffa0049.todomahasiswaapp.repository.TugasRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.daffa0049.todomahasiswaapp.data.UserPreferences


class TugasViewModel(private val repository: TugasRepository) : ViewModel() {

    val semuaTugas: StateFlow<List<Tugas>> = repository.semuaTugas
        .map { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    private lateinit var userPreferences: UserPreferences

    fun setUserPreferences(prefs: UserPreferences) {
        userPreferences = prefs
    }

    fun getTugasById(id: Int): Flow<Tugas?> = repository.getTugasById(id)

    fun tambahTugas(tugas: Tugas) {
        viewModelScope.launch {
            repository.tambahTugas(tugas)
        }
    }

    fun updateTugas(tugas: Tugas) {
        viewModelScope.launch {
            repository.updateTugas(tugas)
        }
    }

    fun hapusTugas(tugas: Tugas) {
        viewModelScope.launch {
            repository.hapusTugas(tugas)
        }
    }
}


