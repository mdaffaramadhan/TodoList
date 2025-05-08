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
        val IS_GRID = booleanPreferencesKey("is_grid")
    }

    val filterSelesai: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[FILTER_SELESAI] ?: false
        }

    val isGridView: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_GRID] ?: false }

    suspend fun setFilterSelesai(showOnlyUnfinished: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[FILTER_SELESAI] = showOnlyUnfinished
        }
    }

    suspend fun setGridView(isGrid: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_GRID] = isGrid
        }
    }


}
