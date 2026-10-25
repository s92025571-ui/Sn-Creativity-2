package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class AppThemeMode(val storageKey: String) {
    NEON_DARK("neon_dark"),
    CREATIVE_LIGHT("creative_light");

    companion object {
        fun fromKey(key: String?): AppThemeMode {
            return entries.firstOrNull { it.storageKey == key } ?: NEON_DARK
        }
    }
}

private val Context.snDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "sn_creativity_preferences"
)

class ThemePreferencesRepository(private val context: Context) {

    private val themeModeKey = stringPreferencesKey("selected_theme_mode")

    val themeModeFlow: Flow<AppThemeMode> = context.snDataStore.data.map { preferences ->
        AppThemeMode.fromKey(preferences[themeModeKey])
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.snDataStore.edit { preferences ->
            preferences[themeModeKey] = mode.storageKey
        }
    }
}
