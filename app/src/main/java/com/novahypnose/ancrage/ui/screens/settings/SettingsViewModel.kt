package com.novahypnose.ancrage.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novahypnose.ancrage.data.repository.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel pour l'écran de paramètres
 */
class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val themeMode: StateFlow<String> = settingsRepository.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, SettingsRepository.DEFAULT_THEME)

    val fontSize: StateFlow<String> = settingsRepository.fontSize
        .stateIn(viewModelScope, SharingStarted.Eagerly, SettingsRepository.DEFAULT_FONT_SIZE)

    val animationIntensity: StateFlow<String> = settingsRepository.animationIntensity
        .stateIn(viewModelScope, SharingStarted.Eagerly, SettingsRepository.DEFAULT_ANIMATION_INTENSITY)

    val vibrationEnabled: StateFlow<Boolean> = settingsRepository.vibrationEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val vibrationIntensity: StateFlow<String> = settingsRepository.vibrationIntensity
        .stateIn(viewModelScope, SharingStarted.Eagerly, SettingsRepository.DEFAULT_VIBRATION_INTENSITY)

    val ttsEnabled: StateFlow<Boolean> = settingsRepository.ttsEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val ttsSpeed: StateFlow<Float> = settingsRepository.ttsSpeed
        .stateIn(viewModelScope, SharingStarted.Eagerly, SettingsRepository.DEFAULT_TTS_SPEED)

    val ttsPitch: StateFlow<Float> = settingsRepository.ttsPitch
        .stateIn(viewModelScope, SharingStarted.Eagerly, SettingsRepository.DEFAULT_TTS_PITCH)

    val notificationsEnabled: StateFlow<Boolean> = settingsRepository.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(mode)
        }
    }

    fun setFontSize(size: String) {
        viewModelScope.launch {
            settingsRepository.setFontSize(size)
        }
    }

    fun setAnimationIntensity(intensity: String) {
        viewModelScope.launch {
            settingsRepository.setAnimationIntensity(intensity)
        }
    }

    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setVibrationEnabled(enabled)
        }
    }

    fun setVibrationIntensity(intensity: String) {
        viewModelScope.launch {
            settingsRepository.setVibrationIntensity(intensity)
        }
    }

    fun setTTSEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setTTSEnabled(enabled)
        }
    }

    fun setTTSSpeed(speed: Float) {
        viewModelScope.launch {
            settingsRepository.setTTSSpeed(speed)
        }
    }

    fun setTTSPitch(pitch: Float) {
        viewModelScope.launch {
            settingsRepository.setTTSPitch(pitch)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(enabled)
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            settingsRepository.resetAllSettings()
        }
    }
}
