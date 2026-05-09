package com.petcare.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val MEASUREMENT_UNIT_KEY = stringPreferencesKey("measurement_unit")
        private val SORT_MODE_KEY = stringPreferencesKey("sort_mode")
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val USER_IMAGE_KEY = stringPreferencesKey("user_image")
    }


    val userName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME_KEY] ?: ""
        }


    val measurementUnit: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[MEASUREMENT_UNIT_KEY] ?: "kg"
        }


    val sortMode: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[SORT_MODE_KEY] ?: "name"
        }


    val language: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_KEY] ?: "en"
        }

    val userImage: Flow<String?> = context.dataStore.data.map {
        val value = it[USER_IMAGE_KEY]
        if (value == "null" || value.isNullOrBlank()) null else value
    }


    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }


    suspend fun saveMeasurementUnit(unit: String) {
        context.dataStore.edit { preferences ->
            preferences[MEASUREMENT_UNIT_KEY] = unit
        }
    }


    suspend fun saveSortMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[SORT_MODE_KEY] = mode
        }
    }


    suspend fun saveLanguage(lang: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = lang
        }
    }

    suspend fun saveUserImage(image: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_IMAGE_KEY] = image
        }
    }


    val hasUserName: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME_KEY]?.isNotEmpty() ?: false
        }
}
