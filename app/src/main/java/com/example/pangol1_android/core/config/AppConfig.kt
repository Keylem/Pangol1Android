package com.example.pangol1_android.core.config

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Configuration management using DataStore
 */
class AppConfig(context: Context) {
    
    private val dataStore: DataStore<Preferences> = context.preferencesDataStore
    
    companion object {
        const val DATASTORE_NAME = "pangol1_config"
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val THEME_KEY = stringPreferencesKey("theme")
        val DEFAULT_TABLE_URL = stringPreferencesKey("default_table_url")
    }
    
    fun getLanguageFlow(): Flow<String> = dataStore.data
        .map { preferences -> preferences[LANGUAGE_KEY] ?: "en" }
    
    fun getThemeFlow(): Flow<String> = dataStore.data
        .map { preferences -> preferences[THEME_KEY] ?: "light" }
    
    fun getDefaultTableUrlFlow(): Flow<String> = dataStore.data
        .map { preferences -> preferences[DEFAULT_TABLE_URL] ?: "" }
    
    suspend fun setLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }
    
    suspend fun setTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }
    
    suspend fun setDefaultTableUrl(url: String) {
        dataStore.edit { preferences ->
            preferences[DEFAULT_TABLE_URL] = url
        }
    }
}

private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = AppConfig.DATASTORE_NAME
)
