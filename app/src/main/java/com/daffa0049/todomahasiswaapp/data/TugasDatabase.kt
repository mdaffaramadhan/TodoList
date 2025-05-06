package com.daffa0049.todomahasiswaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Tugas::class], version = 2, exportSchema = false) // Ubah versi menjadi 2
abstract class TugasDatabase : RoomDatabase() {
    abstract fun tugasDao(): TugasDao

    companion object {
        @Volatile
        private var INSTANCE: TugasDatabase? = null

        // Migrasi dari versi 1 ke 2 untuk menghapus kolom 'selesai' dan menambah kolom 'prioritas'
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Buat tabel baru tanpa kolom 'selesai', tetapi dengan kolom 'prioritas'
                database.execSQL(
                    """
                    CREATE TABLE tugas_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nama TEXT NOT NULL,
                        deadline TEXT NOT NULL,
                        prioritas TEXT NOT NULL DEFAULT 'Medium'
                    )
                    """.trimIndent()
                )

                // Salin data dari tabel lama ke tabel baru, kecuali kolom 'selesai'
                database.execSQL(
                    """
                    INSERT INTO tugas_new (id, nama, deadline, prioritas)
                    SELECT id, nama, deadline, 'Medium' FROM tugas
                    """.trimIndent()
                )

                // Hapus tabel lama
                database.execSQL("DROP TABLE tugas")

                // Ganti nama tabel baru menjadi 'tugas'
                database.execSQL("ALTER TABLE tugas_new RENAME TO tugas")
            }
        }

        // Fungsi untuk mendapatkan instance database
        fun getDatabase(context: Context): TugasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TugasDatabase::class.java,
                    "tugas_database"
                )
                    .addMigrations(MIGRATION_1_2) // Menambahkan migrasi ke builder
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


