package com.novahypnose.ancrage.data.database.dao

import androidx.room.*
import com.novahypnose.ancrage.data.database.entities.Reminder
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur les rappels
 */
@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders WHERE is_active = 1 ORDER BY next_trigger_at ASC")
    fun getActiveRemindersFlow(): Flow<List<Reminder>>

    @Query("SELECT * FROM reminders WHERE is_active = 1 ORDER BY next_trigger_at ASC")
    suspend fun getActiveReminders(): List<Reminder>

    @Query("SELECT * FROM reminders WHERE anchor_id = :anchorId")
    suspend fun getRemindersByAnchor(anchorId: Long): List<Reminder>

    @Query("SELECT * FROM reminders WHERE id = :reminderId")
    suspend fun getReminderById(reminderId: Long): Reminder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder): Long

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("DELETE FROM reminders WHERE id = :reminderId")
    suspend fun deleteReminderById(reminderId: Long)

    @Query("UPDATE reminders SET is_active = :isActive WHERE id = :reminderId")
    suspend fun setReminderActive(reminderId: Long, isActive: Boolean)

    @Query("""
        UPDATE reminders
        SET last_triggered_at = :timestamp,
            next_trigger_at = :nextTrigger
        WHERE id = :reminderId
    """)
    suspend fun updateTriggerTimes(reminderId: Long, timestamp: Long, nextTrigger: Long)
}
