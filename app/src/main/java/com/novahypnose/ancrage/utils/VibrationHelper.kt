package com.novahypnose.ancrage.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Helper pour gérer les vibrations tactiles
 */
class VibrationHelper(private val context: Context) {

    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    /**
     * Vibre avec une durée spécifique
     * @param durationMs Durée en millisecondes
     * @param intensity Intensité: "low", "medium", "high"
     */
    fun vibrate(durationMs: Long = 500, intensity: String = "medium") {
        if (!vibrator.hasVibrator()) return

        val amplitude = when (intensity) {
            "low" -> 50
            "medium" -> 128
            "high" -> 255
            else -> 128
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(durationMs, amplitude)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(durationMs)
        }
    }

    /**
     * Vibre avec un pattern (pour la réactivation)
     * Pattern: 0 = attendre, 1 = vibrer
     */
    fun vibratePattern(pattern: LongArray = longArrayOf(0, 200, 100, 200)) {
        if (!vibrator.hasVibrator()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(pattern, -1)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, -1)
        }
    }

    /**
     * Vibre doucement (pour le feedback tactile)
     */
    fun vibrateGentle() {
        vibrate(durationMs = 50, intensity = "low")
    }

    /**
     * Vibre pour confirmer une action
     */
    fun vibrateConfirm() {
        vibratePattern(longArrayOf(0, 30, 50, 30))
    }

    /**
     * Annule toute vibration en cours
     */
    fun cancel() {
        vibrator.cancel()
    }
}
