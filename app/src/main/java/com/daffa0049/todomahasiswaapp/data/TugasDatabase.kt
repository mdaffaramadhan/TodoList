package com.daffa0049.todomahasiswaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Tugas::class], version = 2, exportSchema = false)
abstract class TugasDatabase : RoomDatabase() {
    abstract fun tugasDao(): TugasDao

    companion object {
        @Volatile
        private var INSTANCE: TugasDatabase? = null


        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE tugas_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nama TEXT NOT NULL,
                        deadline TEXT NOT NULL,
                        prioritas TEXT NOT NULL DEFAULT 'Medium'
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO tugas_new (id, nama, deadline, prioritas)
                    SELECT id, nama, deadline, 'Medium' FROM tugas
                    """.trimIndent()
                )

                db.execSQL("DROP TABLE tugas")

                db.execSQL("ALTER TABLE tugas_new RENAME TO tugas")
            }
        }

        fun getDatabase(context: Context): TugasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TugasDatabase::class.java,
                    "tugas_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


