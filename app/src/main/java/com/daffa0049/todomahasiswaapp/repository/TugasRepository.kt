package com.daffa0049.todomahasiswaapp.repository

import com.daffa0049.todomahasiswaapp.data.Tugas
import com.daffa0049.todomahasiswaapp.data.TugasDao
import kotlinx.coroutines.flow.Flow

class TugasRepository(private val tugasDao: TugasDao) {

    val semuaTugas: Flow<List<Tugas>> = tugasDao.getAll()

    suspend fun tambahTugas(tugas: Tugas) {
        tugasDao.insert(tugas)
    }

    suspend fun updateTugas(tugas: Tugas) {
        tugasDao.update(tugas)
    }

    suspend fun hapusTugas(tugas: Tugas) {
        tugasDao.delete(tugas)
    }
}

