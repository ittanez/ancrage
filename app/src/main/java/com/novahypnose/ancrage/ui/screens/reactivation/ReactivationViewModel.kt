package com.novahypnose.ancrage.ui.screens.reactivation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novahypnose.ancrage.data.database.entities.Anchor
import com.novahypnose.ancrage.data.repository.AnchorRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel pour l'écran de réactivation d'ancrage
 */
class ReactivationViewModel(
    private val anchorRepository: AnchorRepository,
    private val anchorId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReactivationUiState())
    val uiState: StateFlow<ReactivationUiState> = _uiState.asStateFlow()

    private var startTime: Long = 0

    init {
        loadAnchor()
        startReactivation()
    }

    private fun loadAnchor() {
        viewModelScope.launch {
            anchorRepository.getAnchorById(anchorId)
                .catch { exception ->
                    _uiState.update {
                        it.copy(error = exception.message)
                    }
                }
                .collect { anchor ->
                    anchor?.let {
                        _uiState.update { state ->
                            state.copy(
                                anchor = it,
                                totalDuration = it.displayDuration
                            )
                        }
                    }
                }
        }
    }

    private fun startReactivation() {
        startTime = System.currentTimeMillis()

        viewModelScope.launch {
            val duration = _uiState.value.anchor?.displayDuration ?: 30

            // Compte à rebours
            for (i in 0..duration) {
                delay(1000)
                _uiState.update {
                    it.copy(
                        elapsedTime = i,
                        remainingTime = duration - i
                    )
                }
            }

            // Fin automatique
            _uiState.update { it.copy(isFinished = true) }
        }
    }

    /**
     * Termine la réactivation manuellement
     */
    fun finishReactivation(postIntensity: Int? = null) {
        viewModelScope.launch {
            val actualDuration =
                ((System.currentTimeMillis() - startTime) / 1000).toInt()

            anchorRepository.recordReactivation(
                anchorId = anchorId,
                postIntensity = postIntensity,
                duration = actualDuration,
                triggeredBy = "manual"
            )

            _uiState.update { it.copy(isFinished = true) }
        }
    }

    /**
     * Enregistre l'évaluation post-réactivation
     */
    fun submitPostIntensity(intensity: Int) {
        finishReactivation(postIntensity = intensity)
    }
}

/**
 * État de l'UI de l'écran de réactivation
 */
data class ReactivationUiState(
    val anchor: Anchor? = null,
    val elapsedTime: Int = 0,
    val remainingTime: Int = 0,
    val totalDuration: Int = 30,
    val isFinished: Boolean = false,
    val error: String? = null
)
