package com.novahypnose.ancrage.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository pour gérer les paramètres de l'application via DataStore
 */
class SettingsRepository(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

        // Keys
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val FONT_SIZE = stringPreferencesKey("font_size")
        val ANIMATION_INTENSITY = stringPreferencesKey("animation_intensity")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val VIBRATION_INTENSITY = stringPreferencesKey("vibration_intensity")
        val TTS_ENABLED = booleanPreferencesKey("tts_enabled")
        val TTS_SPEED = floatPreferencesKey("tts_speed")
        val TTS_PITCH = floatPreferencesKey("tts_pitch")
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")

        // Default values
        const val DEFAULT_THEME = "auto" // "light", "dark", "auto"
        const val DEFAULT_FONT_SIZE = "medium" // "small", "medium", "large"
        const val DEFAULT_ANIMATION_INTENSITY = "medium" // "low", "medium", "high"
        const val DEFAULT_VIBRATION_INTENSITY = "medium" // "low", "medium", "high"
        const val DEFAULT_TTS_SPEED = 0.85f // 0.5f to 1.5f
        const val DEFAULT_TTS_PITCH = 0.9f // 0.7f to 1.3f
    }

    private val dataStore = context.dataStore

    // === Theme ===
    val themeMode: Flow<String> = dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: DEFAULT_THEME
    }

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode
        }
    }

    // === Font Size ===
    val fontSize: Flow<String> = dataStore.data.map { preferences ->
        preferences[FONT_SIZE] ?: DEFAULT_FONT_SIZE
    }

    suspend fun setFontSize(size: String) {
        dataStore.edit { preferences ->
            preferences[FONT_SIZE] = size
        }
    }

    // === Animation ===
    val animationIntensity: Flow<String> = dataStore.data.map { preferences ->
        preferences[ANIMATION_INTENSITY] ?: DEFAULT_ANIMATION_INTENSITY
    }

    suspend fun setAnimationIntensity(intensity: String) {
        dataStore.edit { preferences ->
            preferences[ANIMATION_INTENSITY] = intensity
        }
    }

    // === Vibration ===
    val vibrationEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[VIBRATION_ENABLED] ?: true
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[VIBRATION_ENABLED] = enabled
        }
    }

    val vibrationIntensity: Flow<String> = dataStore.data.map { preferences ->
        preferences[VIBRATION_INTENSITY] ?: DEFAULT_VIBRATION_INTENSITY
    }

    suspend fun setVibrationIntensity(intensity: String) {
        dataStore.edit { preferences ->
            preferences[VIBRATION_INTENSITY] = intensity
        }
    }

    // === TTS (Text-to-Speech) ===
    val ttsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[TTS_ENABLED] ?: true
    }

    suspend fun setTTSEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[TTS_ENABLED] = enabled
        }
    }

    val ttsSpeed: Flow<Float> = dataStore.data.map { preferences ->
        preferences[TTS_SPEED] ?: DEFAULT_TTS_SPEED
    }

    suspend fun setTTSSpeed(speed: Float) {
        dataStore.edit { preferences ->
            preferences[TTS_SPEED] = speed
        }
    }

    val ttsPitch: Flow<Float> = dataStore.data.map { preferences ->
        preferences[TTS_PITCH] ?: DEFAULT_TTS_PITCH
    }

    suspend fun setTTSPitch(pitch: Float) {
        dataStore.edit { preferences ->
            preferences[TTS_PITCH] = pitch
        }
    }

    // === First Launch ===
    val isFirstLaunch: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[FIRST_LAUNCH] ?: true
    }

    suspend fun setFirstLaunchComplete() {
        dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH] = false
        }
    }

    // === Notifications ===
    val notificationsEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED] ?: true
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    // === Reset ===
    suspend fun resetAllSettings() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
