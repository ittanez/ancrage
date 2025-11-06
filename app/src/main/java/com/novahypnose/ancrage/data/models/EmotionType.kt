package com.novahypnose.ancrage.data.models

/**
 * Énumération des types d'émotions disponibles pour les ancrages
 */
enum class EmotionType(
    val displayName: String,
    val primaryColorHex: String,
    val secondaryColorHex: String,
    val emoji: String
) {
    SERENITY(
        displayName = "Sérénité",
        primaryColorHex = "#87CEEB",
        secondaryColorHex = "#B0E0E6",
        emoji = "🌞"
    ),
    CONFIDENCE(
        displayName = "Confiance",
        primaryColorHex = "#FF8C42",
        secondaryColorHex = "#FFB347",
        emoji = "💪"
    ),
    ENERGY(
        displayName = "Énergie",
        primaryColorHex = "#FFD700",
        secondaryColorHex = "#FFF44F",
        emoji = "💫"
    ),
    KINDNESS(
        displayName = "Bienveillance",
        primaryColorHex = "#FFB6C1",
        secondaryColorHex = "#FFC0CB",
        emoji = "❤️"
    ),
    CALM(
        displayName = "Calme intérieur",
        primaryColorHex = "#9370DB",
        secondaryColorHex = "#B19CD9",
        emoji = "🌙"
    ),
    OTHER(
        displayName = "Autre",
        primaryColorHex = "#B0B0B0",
        secondaryColorHex = "#D0D0D0",
        emoji = "✨"
    );

    companion object {
        fun fromString(value: String): EmotionType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: OTHER
        }
    }
}
