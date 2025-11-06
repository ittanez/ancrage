package com.novahypnose.ancrage.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entité représentant une réactivation d'ancrage
 */
@Entity(
    tableName = "reactivations",
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
data class Reactivation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "anchor_id")
    val anchorId: Long,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "pre_intensity")
    val preIntensity: Int? = null, // 1-10, optionnel

    @ColumnInfo(name = "post_intensity")
    val postIntensity: Int? = null, // 1-10

    @ColumnInfo(name = "duration")
    val duration: Int? = null, // Durée effective en secondes

    @ColumnInfo(name = "triggered_by")
    val triggeredBy: String = "manual" // 'manual', 'notification', 'widget'
)
