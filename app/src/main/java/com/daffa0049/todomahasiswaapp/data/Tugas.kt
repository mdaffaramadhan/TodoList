package com.daffa0049.todomahasiswaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tugas")
data class Tugas(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val deadline: String,
    val prioritas: String
)