package com.novahypnose.ancrage.ui.theme

import androidx.compose.ui.graphics.Color

// Couleurs émotionnelles (ancrages)
val SerenityPrimary = Color(0xFF87CEEB)
val SerenitySecondary = Color(0xFFB0E0E6)

val ConfidencePrimary = Color(0xFFFF8C42)
val ConfidenceSecondary = Color(0xFFFFB347)

val EnergyPrimary = Color(0xFFFFD700)
val EnergySecondary = Color(0xFFFFF44F)

val KindnessPrimary = Color(0xFFFFB6C1)
val KindnessSecondary = Color(0xFFFFC0CB)

val CalmPrimary = Color(0xFF9370DB)
val CalmSecondary = Color(0xFFB19CD9)

val OtherPrimary = Color(0xFFB0B0B0)
val OtherSecondary = Color(0xFFD0D0D0)

// Light Theme Colors
val BackgroundLight = Color(0xFFFAFAFA)
val SurfaceLight = Color(0xFFFFFFFF)
val TextPrimaryLight = Color(0xFF212121)
val TextSecondaryLight = Color(0xFF757575)

// Dark Theme Colors
val BackgroundDark = Color(0xFF121212)
val SurfaceDark = Color(0xFF1E1E1E)
val TextPrimaryDark = Color(0xFFE0E0E0)
val TextSecondaryDark = Color(0xFFB0B0B0)

// Material Design 3 Colors
val md_theme_light_primary = CalmPrimary
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFE8DEF8)
val md_theme_light_onPrimaryContainer = Color(0xFF21005E)
val md_theme_light_secondary = SerenityPrimary
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFE8DEF8)
val md_theme_light_onSecondaryContainer = Color(0xFF1D192B)
val md_theme_light_tertiary = Color(0xFF7D5260)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_error = Color(0xFFB3261E)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_background = BackgroundLight
val md_theme_light_onBackground = TextPrimaryLight
val md_theme_light_surface = SurfaceLight
val md_theme_light_onSurface = TextPrimaryLight
val md_theme_light_surfaceVariant = Color(0xFFE7E0EC)
val md_theme_light_onSurfaceVariant = Color(0xFF49454F)
val md_theme_light_outline = Color(0xFF79747E)

val md_theme_dark_primary = CalmSecondary
val md_theme_dark_onPrimary = Color(0xFF371E73)
val md_theme_dark_primaryContainer = Color(0xFF4F378B)
val md_theme_dark_onPrimaryContainer = Color(0xFFE8DEF8)
val md_theme_dark_secondary = SerenitySecondary
val md_theme_dark_onSecondary = Color(0xFF332D41)
val md_theme_dark_secondaryContainer = Color(0xFF4A4458)
val md_theme_dark_onSecondaryContainer = Color(0xFFE8DEF8)
val md_theme_dark_tertiary = Color(0xFFEFB8C8)
val md_theme_dark_onTertiary = Color(0xFF492532)
val md_theme_dark_error = Color(0xFFF2B8B5)
val md_theme_dark_onError = Color(0xFF601410)
val md_theme_dark_background = BackgroundDark
val md_theme_dark_onBackground = TextPrimaryDark
val md_theme_dark_surface = SurfaceDark
val md_theme_dark_onSurface = TextPrimaryDark
val md_theme_dark_surfaceVariant = Color(0xFF49454F)
val md_theme_dark_onSurfaceVariant = Color(0xFFCAC4D0)
val md_theme_dark_outline = Color(0xFF938F99)

/**
 * Convertit un code couleur hex en Color
 */
fun String.toColor(): Color {
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        Color.Gray
    }
}
