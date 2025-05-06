package com.daffa0049.todomahasiswaapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TugasDao {
    @Query("SELECT * FROM tugas")
    fun getAll(): Flow<List<Tugas>>

    @Query("SELECT * FROM tugas WHERE id = :id")
    fun getById(id: Int): Flow<Tugas?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tugas: Tugas)

    @Update
    suspend fun update(tugas: Tugas)

    @Delete
    suspend fun delete(tugas: Tugas)
}