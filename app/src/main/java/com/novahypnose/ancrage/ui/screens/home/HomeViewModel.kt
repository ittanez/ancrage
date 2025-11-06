package com.novahypnose.ancrage.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novahypnose.ancrage.data.database.entities.Anchor
import com.novahypnose.ancrage.data.repository.AnchorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel pour l'écran d'accueil
 */
class HomeViewModel(
    private val anchorRepository: AnchorRepository
) : ViewModel() {

    // État de l'UI
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadAnchors()
    }

    /**
     * Charge tous les ancrages actifs
     */
    private fun loadAnchors() {
        viewModelScope.launch {
            anchorRepository.getAllActiveAnchors()
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
                .collect { anchors ->
                    _uiState.update {
                        it.copy(
                            anchors = anchors,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    /**
     * Bascule le favori d'un ancrage
     */
    fun toggleFavorite(anchorId: Long) {
        viewModelScope.launch {
            try {
                anchorRepository.toggleFavorite(anchorId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    /**
     * Archive un ancrage
     */
    fun archiveAnchor(anchorId: Long) {
        viewModelScope.launch {
            try {
                anchorRepository.archiveAnchor(anchorId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    /**
     * Supprime un ancrage
     */
    fun deleteAnchor(anchor: Anchor) {
        viewModelScope.launch {
            try {
                anchorRepository.deleteAnchor(anchor)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    /**
     * Efface l'erreur
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * État de l'UI de l'écran d'accueil
 */
data class HomeUiState(
    val anchors: List<Anchor> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
