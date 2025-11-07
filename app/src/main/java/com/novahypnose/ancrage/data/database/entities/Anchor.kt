package com.novahypnose.ancrage.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.novahypnose.ancrage.data.models.EmotionType

/**
 * Entité représentant un ancrage positif
 */
@Entity(tableName = "anchors")
data class Anchor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Type d'émotion
    @ColumnInfo(name = "emotion_type")
    val emotionType: String, // Stocké comme String, converti en EmotionType

    @ColumnInfo(name = "custom_emotion_name")
    val customEmotionName: String? = null,

    // Ancrages sensoriels
    @ColumnInfo(name = "color_hex")
    val colorHex: String, // Format #RRGGBB

    @ColumnInfo(name = "keyword_phrase")
    val keywordPhrase: String? = null, // OBLIGATOIRE - Max 50 caractères

    @ColumnInfo(name = "mental_image_description")
    val mentalImageDescription: String? = null, // OPTIONNEL - Description de l'image mentale

    @ColumnInfo(name = "image_path")
    val imagePath: String? = null, // OPTIONNEL - Chemin vers image personnalisée (Phase 2)

    @ColumnInfo(name = "gesture_description")
    val gestureDescription: String? = null, // OPTIONNEL - Description du geste kinesthésique

    @ColumnInfo(name = "place_description")
    val placeDescription: String? = null, // OPTIONNEL - Description du lieu/situation

    // Métadonnées
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "last_used_at")
    val lastUsedAt: Long? = null,

    @ColumnInfo(name = "use_count")
    val useCount: Int = 0,

    // États émotionnels (1-10)
    @ColumnInfo(name = "initial_intensity")
    val initialIntensity: Int = 5,

    @ColumnInfo(name = "current_intensity")
    val currentIntensity: Int = 5,

    // Préférences
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "is_archived")
    val isArchived: Boolean = false,

    // Paramètres de réactivation
    @ColumnInfo(name = "vibration_enabled")
    val vibrationEnabled: Boolean = true,

    @ColumnInfo(name = "vibration_duration")
    val vibrationDuration: Int = 500, // millisecondes

    @ColumnInfo(name = "display_duration")
    val displayDuration: Int = 30 // secondes
) {
    /**
     * Retourne le type d'émotion comme enum
     */
    fun getEmotionTypeEnum(): EmotionType {
        return EmotionType.fromString(emotionType)
    }

    /**
     * Retourne le nom d'affichage de l'émotion
     */
    fun getDisplayName(): String {
        return if (emotionType == EmotionType.OTHER.name && !customEmotionName.isNullOrBlank()) {
            customEmotionName
        } else {
            getEmotionTypeEnum().displayName
        }
    }
}
