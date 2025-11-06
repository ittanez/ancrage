package com.novahypnose.ancrage.data.repository

import com.novahypnose.ancrage.data.database.dao.AnchorDao
import com.novahypnose.ancrage.data.database.dao.ReactivationDao
import com.novahypnose.ancrage.data.database.entities.Anchor
import com.novahypnose.ancrage.data.database.entities.Reactivation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Repository pour gérer les ancrages et leurs réactivations
 */
class AnchorRepository(
    private val anchorDao: AnchorDao,
    private val reactivationDao: ReactivationDao
) {

    // === Anchors ===

    fun getAllActiveAnchors(): Flow<List<Anchor>> {
        return anchorDao.getAllActiveAnchorsFlow()
    }

    fun getArchivedAnchors(): Flow<List<Anchor>> {
        return anchorDao.getArchivedAnchorsFlow()
    }

    fun getAnchorById(anchorId: Long): Flow<Anchor?> {
        return anchorDao.getAnchorByIdFlow(anchorId)
    }

    suspend fun getAnchorByIdSync(anchorId: Long): Anchor? {
        return anchorDao.getAnchorById(anchorId)
    }

    fun getFavoriteAnchors(): Flow<List<Anchor>> {
        return anchorDao.getFavoriteAnchorsFlow()
    }

    fun getAnchorsByEmotionType(emotionType: String): Flow<List<Anchor>> {
        return anchorDao.getAnchorsByEmotionType(emotionType)
    }

    suspend fun insertAnchor(anchor: Anchor): Long {
        return anchorDao.insertAnchor(anchor)
    }

    suspend fun updateAnchor(anchor: Anchor) {
        anchorDao.updateAnchor(anchor)
    }

    suspend fun deleteAnchor(anchor: Anchor) {
        anchorDao.deleteAnchor(anchor)
    }

    suspend fun archiveAnchor(anchorId: Long) {
        anchorDao.archiveAnchor(anchorId)
    }

    suspend fun unarchiveAnchor(anchorId: Long) {
        anchorDao.unarchiveAnchor(anchorId)
    }

    suspend fun toggleFavorite(anchorId: Long) {
        val anchor = anchorDao.getAnchorById(anchorId)
        anchor?.let {
            anchorDao.setFavorite(anchorId, !it.isFavorite)
        }
    }

    // === Reactivations ===

    suspend fun recordReactivation(
        anchorId: Long,
        preIntensity: Int? = null,
        postIntensity: Int? = null,
        duration: Int? = null,
        triggeredBy: String = "manual"
    ): Long {
        val timestamp = System.currentTimeMillis()

        // Créer la réactivation
        val reactivation = Reactivation(
            anchorId = anchorId,
            timestamp = timestamp,
            preIntensity = preIntensity,
            postIntensity = postIntensity,
            duration = duration,
            triggeredBy = triggeredBy
        )

        val reactivationId = reactivationDao.insertReactivation(reactivation)

        // Mettre à jour l'ancrage
        postIntensity?.let {
            anchorDao.updateUsage(anchorId, timestamp, it)
        } ?: run {
            // Si pas d'évaluation post, incrémenter juste le compteur
            val anchor = anchorDao.getAnchorById(anchorId)
            anchor?.let {
                anchorDao.updateAnchor(
                    it.copy(
                        lastUsedAt = timestamp,
                        useCount = it.useCount + 1
                    )
                )
            }
        }

        return reactivationId
    }

    fun getReactivationsByAnchor(anchorId: Long): Flow<List<Reactivation>> {
        return reactivationDao.getReactivationsByAnchorFlow(anchorId)
    }

    fun getAllReactivations(): Flow<List<Reactivation>> {
        return reactivationDao.getAllReactivationsFlow()
    }

    suspend fun getReactivationCountForAnchor(anchorId: Long): Int {
        return reactivationDao.getReactivationCount(anchorId)
    }

    // === Statistics ===

    suspend fun getActiveAnchorsCount(): Int {
        return anchorDao.getActiveAnchorsCount()
    }

    suspend fun getMostUsedAnchor(): Anchor? {
        return anchorDao.getMostUsedAnchor()
    }

    suspend fun getAverageIntensityLastDays(days: Int): Double? {
        val startTimestamp = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L)
        return reactivationDao.getAverageIntensitySince(startTimestamp)
    }

    suspend fun getConsecutiveDaysStreak(): Int {
        // TODO: Implémenter le calcul de série de jours consécutifs
        // Pour le MVP, retourner 0
        return 0
    }
}
