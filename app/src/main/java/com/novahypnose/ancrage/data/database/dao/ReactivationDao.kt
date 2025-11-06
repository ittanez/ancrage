package com.novahypnose.ancrage.data.database.dao

import androidx.room.*
import com.novahypnose.ancrage.data.database.entities.Reactivation
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur les réactivations
 */
@Dao
interface ReactivationDao {

    @Query("SELECT * FROM reactivations ORDER BY timestamp DESC")
    fun getAllReactivationsFlow(): Flow<List<Reactivation>>

    @Query("SELECT * FROM reactivations WHERE anchor_id = :anchorId ORDER BY timestamp DESC")
    fun getReactivationsByAnchorFlow(anchorId: Long): Flow<List<Reactivation>>

    @Query("SELECT * FROM reactivations WHERE anchor_id = :anchorId ORDER BY timestamp DESC")
    suspend fun getReactivationsByAnchor(anchorId: Long): List<Reactivation>

    @Query("""
        SELECT * FROM reactivations
        WHERE timestamp >= :startTimestamp
        ORDER BY timestamp DESC
    """)
    suspend fun getReactivationsSince(startTimestamp: Long): List<Reactivation>

    @Query("SELECT COUNT(*) FROM reactivations WHERE anchor_id = :anchorId")
    suspend fun getReactivationCount(anchorId: Long): Int

    @Query("SELECT COUNT(*) FROM reactivations")
    suspend fun getTotalReactivationCount(): Int

    @Query("""
        SELECT AVG(post_intensity)
        FROM reactivations
        WHERE post_intensity IS NOT NULL
        AND timestamp >= :startTimestamp
    """)
    suspend fun getAverageIntensitySince(startTimestamp: Long): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReactivation(reactivation: Reactivation): Long

    @Delete
    suspend fun deleteReactivation(reactivation: Reactivation)

    @Query("DELETE FROM reactivations WHERE anchor_id = :anchorId")
    suspend fun deleteReactivationsByAnchor(anchorId: Long)

    @Query("DELETE FROM reactivations WHERE timestamp < :timestamp")
    suspend fun deleteReactivationsOlderThan(timestamp: Long)
}
