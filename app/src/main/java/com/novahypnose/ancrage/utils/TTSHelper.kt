package com.novahypnose.ancrage.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

/**
 * Helper pour gérer la synthèse vocale (TTS)
 * Gère l'initialisation, la lecture et l'arrêt de la synthèse vocale
 */
class TTSHelper(private val context: Context) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    /**
     * Initialise le TTS avec une voix française douce
     * @return true si l'initialisation réussit, false sinon
     */
    suspend fun initialize(): Boolean = suspendCancellableCoroutine { continuation ->
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.let { engine ->
                    // Configurer la voix française
                    val result = engine.setLanguage(Locale.FRENCH)

                    if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Fallback sur anglais si français pas disponible
                        engine.setLanguage(Locale.US)
                    }

                    // Vitesse légèrement ralentie pour effet apaisant
                    engine.setSpeechRate(0.85f)

                    // Pitch légèrement plus grave pour effet calmant
                    engine.setPitch(0.9f)

                    isInitialized = true
                    continuation.resume(true)
                }
            } else {
                continuation.resume(false)
            }
        }

        continuation.invokeOnCancellation {
            shutdown()
        }
    }

    /**
     * Parle un texte avec callback de fin
     * @param text Le texte à vocaliser
     * @param utteranceId Identifiant unique pour cette vocalisation
     * @return true si la lecture réussit, false sinon
     */
    suspend fun speak(
        text: String,
        utteranceId: String = UUID.randomUUID().toString()
    ): Boolean = suspendCancellableCoroutine { continuation ->

        if (!isInitialized || tts == null) {
            continuation.resume(false)
            return@suspendCancellableCoroutine
        }

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                continuation.resume(true)
            }

            override fun onError(utteranceId: String?) {
                continuation.resume(false)
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?, errorCode: Int) {
                continuation.resume(false)
            }
        })

        val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)

        if (result == TextToSpeech.ERROR) {
            continuation.resume(false)
        }
    }

    /**
     * Configure la vitesse de parole
     * @param rate Vitesse (0.5f à 2.0f, défaut: 1.0f)
     */
    fun setSpeechRate(rate: Float) {
        tts?.setSpeechRate(rate)
    }

    /**
     * Configure la tonalité de la voix
     * @param pitch Tonalité (0.5f à 2.0f, défaut: 1.0f)
     */
    fun setPitch(pitch: Float) {
        tts?.setPitch(pitch)
    }

    /**
     * Arrête la lecture en cours
     */
    fun stop() {
        tts?.stop()
    }

    /**
     * Libère les ressources
     */
    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }

    /**
     * Vérifie si le TTS est prêt
     * @return true si initialisé et prêt
     */
    fun isReady(): Boolean = isInitialized && tts != null
}
