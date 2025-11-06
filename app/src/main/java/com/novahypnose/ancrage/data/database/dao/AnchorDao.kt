package com.novahypnose.ancrage.data.database.dao

import androidx.room.*
import com.novahypnose.ancrage.data.database.entities.Anchor
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur les ancrages
 */
@Dao
interface AnchorDao {

    @Query("SELECT * FROM anchors WHERE is_archived = 0 ORDER BY created_at DESC")
    fun getAllActiveAnchorsFlow(): Flow<List<Anchor>>

    @Query("SELECT * FROM anchors WHERE is_archived = 0 ORDER BY created_at DESC")
    suspend fun getAllActiveAnchors(): List<Anchor>

    @Query("SELECT * FROM anchors WHERE is_archived = 1 ORDER BY created_at DESC")
    fun getArchivedAnchorsFlow(): Flow<List<Anchor>>

    @Query("SELECT * FROM anchors WHERE id = :anchorId")
    suspend fun getAnchorById(anchorId: Long): Anchor?

    @Query("SELECT * FROM anchors WHERE id = :anchorId")
    fun getAnchorByIdFlow(anchorId: Long): Flow<Anchor?>

    @Query("SELECT * FROM anchors WHERE is_favorite = 1 AND is_archived = 0 ORDER BY created_at DESC")
    fun getFavoriteAnchorsFlow(): Flow<List<Anchor>>

    @Query("SELECT * FROM anchors WHERE emotion_type = :emotionType AND is_archived = 0")
    fun getAnchorsByEmotionType(emotionType: String): Flow<List<Anchor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnchor(anchor: Anchor): Long

    @Update
    suspend fun updateAnchor(anchor: Anchor)

    @Delete
    suspend fun deleteAnchor(anchor: Anchor)

    @Query("DELETE FROM anchors WHERE id = :anchorId")
    suspend fun deleteAnchorById(anchorId: Long)

    @Query("UPDATE anchors SET is_archived = 1 WHERE id = :anchorId")
    suspend fun archiveAnchor(anchorId: Long)

    @Query("UPDATE anchors SET is_archived = 0 WHERE id = :anchorId")
    suspend fun unarchiveAnchor(anchorId: Long)

    @Query("UPDATE anchors SET is_favorite = :isFavorite WHERE id = :anchorId")
    suspend fun setFavorite(anchorId: Long, isFavorite: Boolean)

    @Query("""
        UPDATE anchors
        SET last_used_at = :timestamp,
            use_count = use_count + 1,
            current_intensity = :newIntensity
        WHERE id = :anchorId
    """)
    suspend fun updateUsage(anchorId: Long, timestamp: Long, newIntensity: Int)

    @Query("SELECT COUNT(*) FROM anchors WHERE is_archived = 0")
    suspend fun getActiveAnchorsCount(): Int

    @Query("SELECT * FROM anchors ORDER BY use_count DESC LIMIT 1")
    suspend fun getMostUsedAnchor(): Anchor?
}
