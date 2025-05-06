package com.daffa0049.todomahasiswaapp.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

val Context.dataStore by preferencesDataStore(name = "settings")

class UserPreferences(private val context: Context) {

    companion object {
        val FILTER_SELESAI = booleanPreferencesKey("filter_selesai")
    }

    val filterSelesai: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[FILTER_SELESAI] ?: false
        }

    suspend fun setFilterSelesai(showOnlyUnfinished: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[FILTER_SELESAI] = showOnlyUnfinished
        }
    }
}
