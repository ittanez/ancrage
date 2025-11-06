package com.novahypnose.ancrage.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.novahypnose.ancrage.data.database.entities.Anchor
import com.novahypnose.ancrage.ui.theme.toColor
import com.novahypnose.ancrage.utils.DateTimeUtils

/**
 * Composant de carte d'ancrage
 */
@Composable
fun AnchorCard(
    anchor: Anchor,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val primaryColor = anchor.colorHex.toColor()
    val secondaryColor = anchor.getEmotionTypeEnum().secondaryColorHex.toColor()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryColor, secondaryColor)
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // En-tête avec emoji et nom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = anchor.getEmotionTypeEnum().emoji,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    if (anchor.isFavorite) {
                        Text(
                            text = "⭐",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Mot-clé / phrase
                anchor.keywordPhrase?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // Footer avec date et stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Créé le ${DateTimeUtils.formatDate(anchor.createdAt)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "${anchor.useCount} réactivations",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    // Jauge d'intensité
                    IntensityIndicator(
                        intensity = anchor.currentIntensity,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun IntensityIndicator(
    intensity: Int,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(10) { index ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(if (index < intensity) (8 + index * 2).dp else 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (index < intensity) color else color.copy(alpha = 0.3f)
                    )
            )
        }
    }
}
