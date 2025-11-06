package com.novahypnose.ancrage.ui.screens.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novahypnose.ancrage.data.database.entities.Anchor
import com.novahypnose.ancrage.data.models.EmotionType
import com.novahypnose.ancrage.data.repository.AnchorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel pour le parcours de création d'ancrage
 */
class CreationViewModel(
    private val anchorRepository: AnchorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreationUiState())
    val uiState: StateFlow<CreationUiState> = _uiState.asStateFlow()

    /**
     * Sélectionne un type d'émotion
     */
    fun selectEmotionType(emotionType: EmotionType) {
        _uiState.update {
            it.copy(
                selectedEmotion = emotionType,
                colorHex = emotionType.primaryColorHex
            )
        }
    }

    /**
     * Définit un nom personnalisé pour l'émotion "Autre"
     */
    fun setCustomEmotionName(name: String) {
        _uiState.update { it.copy(customEmotionName = name) }
    }

    /**
     * Définit la couleur de l'ancrage
     */
    fun setColor(hexColor: String) {
        _uiState.update { it.copy(colorHex = hexColor) }
    }

    /**
     * Définit le mot-clé ou la phrase
     */
    fun setKeywordPhrase(text: String) {
        _uiState.update { it.copy(keywordPhrase = text) }
    }

    /**
     * Active/désactive l'ancrage kinesthésique
     */
    fun setKinestheticGesture(enabled: Boolean) {
        _uiState.update { it.copy(kinestheticGesture = enabled) }
    }

    /**
     * Définit l'intensité initiale
     */
    fun setInitialIntensity(intensity: Int) {
        _uiState.update { it.copy(initialIntensity = intensity) }
    }

    /**
     * Passe à l'étape suivante
     */
    fun nextStep() {
        val currentStep = _uiState.value.currentStep
        if (currentStep < 4) {
            _uiState.update { it.copy(currentStep = currentStep + 1) }
        }
    }

    /**
     * Revient à l'étape précédente
     */
    fun previousStep() {
        val currentStep = _uiState.value.currentStep
        if (currentStep > 1) {
            _uiState.update { it.copy(currentStep = currentStep - 1) }
        }
    }

    /**
     * Sauvegarde l'ancrage
     */
    fun saveAnchor(onSuccess: (Long) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val state = _uiState.value

                if (state.selectedEmotion == null) {
                    onError("Veuillez sélectionner une émotion")
                    return@launch
                }

                if (state.keywordPhrase.isBlank()) {
                    onError("Veuillez entrer un mot-clé ou une phrase")
                    return@launch
                }

                val anchor = Anchor(
                    emotionType = state.selectedEmotion.name,
                    customEmotionName = if (state.selectedEmotion == EmotionType.OTHER) {
                        state.customEmotionName
                    } else null,
                    colorHex = state.colorHex,
                    keywordPhrase = state.keywordPhrase,
                    kinestheticGesture = state.kinestheticGesture,
                    initialIntensity = state.initialIntensity,
                    currentIntensity = state.initialIntensity
                )

                val anchorId = anchorRepository.insertAnchor(anchor)
                onSuccess(anchorId)

            } catch (e: Exception) {
                onError(e.message ?: "Erreur lors de la sauvegarde")
            }
        }
    }

    /**
     * Vérifie si l'étape actuelle est valide
     */
    fun isCurrentStepValid(): Boolean {
        return when (_uiState.value.currentStep) {
            1 -> _uiState.value.selectedEmotion != null
            2 -> true // Évocation guidée, toujours valide
            3 -> _uiState.value.keywordPhrase.isNotBlank()
            4 -> true // Évaluation, toujours valide
            else -> false
        }
    }
}

/**
 * État de l'UI du parcours de création
 */
data class CreationUiState(
    val currentStep: Int = 1,
    val selectedEmotion: EmotionType? = null,
    val customEmotionName: String = "",
    val colorHex: String = "#9370DB",
    val keywordPhrase: String = "",
    val kinestheticGesture: Boolean = false,
    val initialIntensity: Int = 5
)
