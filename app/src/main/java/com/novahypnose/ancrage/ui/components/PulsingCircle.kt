package com.novahypnose.ancrage.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Composant de cercle pulsant pour la réactivation
 * Animation à 4-6 pulsations par minute (calme et apaisant)
 */
@Composable
fun PulsingCircle(
    color: Color = Color.White,
    size: Dp = 120.dp,
    pulsationsPerMinute: Int = 5,
    modifier: Modifier = Modifier
) {
    // Durée d'une pulsation en millisecondes
    val pulsationDuration = (60000 / pulsationsPerMinute)

    val infiniteTransition = rememberInfiniteTransition(label = "pulsing")

    // Animation de l'échelle
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = pulsationDuration,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Animation de l'alpha
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = pulsationDuration,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Canvas(
        modifier = modifier.size(size)
    ) {
        val radius = this.size.minDimension / 2 * scale

        // Cercle extérieur (plus transparent)
        drawCircle(
            color = color.copy(alpha = alpha * 0.3f),
            radius = radius * 1.3f
        )

        // Cercle intermédiaire
        drawCircle(
            color = color.copy(alpha = alpha * 0.5f),
            radius = radius * 1.1f
        )

        // Cercle principal
        drawCircle(
            color = color.copy(alpha = alpha),
            radius = radius
        )

        // Cercle intérieur (contour)
        drawCircle(
            color = color.copy(alpha = alpha),
            radius = radius * 0.8f,
            style = Stroke(width = 3f)
        )
    }
}

/**
 * Variante avec plusieurs cercles concentriques
 */
@Composable
fun PulsingRings(
    color: Color = Color.White,
    size: Dp = 120.dp,
    ringCount: Int = 3,
    pulsationsPerMinute: Int = 5,
    modifier: Modifier = Modifier
) {
    val pulsationDuration = (60000 / pulsationsPerMinute)

    val infiniteTransition = rememberInfiniteTransition(label = "pulsing_rings")

    val scales = List(ringCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = pulsationDuration,
                    easing = FastOutSlowInEasing,
                    delayMillis = index * 200
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale_$index"
        )
    }

    Canvas(
        modifier = modifier.size(size)
    ) {
        val baseRadius = this.size.minDimension / 2

        scales.forEachIndexed { index, scaleState ->
            val scale by scaleState
            val radius = baseRadius * (0.3f + (index * 0.25f)) * scale
            val alpha = 0.8f - (index * 0.2f)

            drawCircle(
                color = color.copy(alpha = alpha),
                radius = radius,
                style = Stroke(width = 2f + (ringCount - index) * 2f)
            )
        }
    }
}
