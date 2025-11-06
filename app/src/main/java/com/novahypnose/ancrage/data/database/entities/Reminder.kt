package com.novahypnose.ancrage.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entité représentant un rappel automatique
 */
@Entity(
    tableName = "reminders",
    foreignKeys = [
        ForeignKey(
            entity = Anchor::class,
            parentColumns = ["id"],
            childColumns = ["anchor_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["anchor_id"])]
)
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "anchor_id")
    val anchorId: Long? = null, // NULL = rappel global rotatif

    // Configuration
    @ColumnInfo(name = "frequency")
    val frequency: String, // 'daily', 'twice_weekly', 'weekly', 'custom'

    @ColumnInfo(name = "time_of_day")
    val timeOfDay: String?, // 'morning', 'noon', 'afternoon', 'evening', 'random'

    @ColumnInfo(name = "specific_time")
    val specificTime: String? = null, // HH:MM si custom

    @ColumnInfo(name = "days_of_week")
    val daysOfWeek: String? = null, // JSON array [0-6], 0=dimanche

    // État
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,

    @ColumnInfo(name = "next_trigger_at")
    val nextTriggerAt: Long? = null,

    @ColumnInfo(name = "last_triggered_at")
    val lastTriggeredAt: Long? = null
)
