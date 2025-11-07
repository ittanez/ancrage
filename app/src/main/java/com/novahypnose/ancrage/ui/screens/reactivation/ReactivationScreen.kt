package com.novahypnose.ancrage.ui.screens.reactivation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.novahypnose.ancrage.R
import com.novahypnose.ancrage.ui.components.IntensitySlider
import com.novahypnose.ancrage.ui.components.PulsingCircle
import com.novahypnose.ancrage.ui.theme.AnchorKeywordStyle
import com.novahypnose.ancrage.ui.theme.toColor
import com.novahypnose.ancrage.utils.TTSHelper
import kotlinx.coroutines.delay

/**
 * Écran de réactivation d'un ancrage
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReactivationScreen(
    viewModel: ReactivationViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val ttsHelper = remember { TTSHelper(context) }
    val uiState by viewModel.uiState.collectAsState()
    var showPostEvaluation by remember { mutableStateOf(false) }
    var postIntensity by remember { mutableIntStateOf(5) }

    // Initialiser TTS et parler au début
    LaunchedEffect(Unit) {
        ttsHelper.initialize()
        delay(1000)
        ttsHelper.speak(context.getString(R.string.reactivation_intro))
    }

    // Parler à la fin
    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished && !showPostEvaluation) {
            ttsHelper.speak(context.getString(R.string.reactivation_outro))
            delay(500)
            showPostEvaluation = true
        }
    }

    // Nettoyer TTS
    DisposableEffect(Unit) {
        onDispose {
            ttsHelper.shutdown()
        }
    }

    val anchor = uiState.anchor

    if (anchor == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val primaryColor = anchor.colorHex.toColor()
    val secondaryColor = anchor.getEmotionTypeEnum().secondaryColorHex.toColor()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(primaryColor, secondaryColor)
                )
            )
    ) {
        // Bouton retour
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Retour",
                tint = Color.White
            )
        }

        if (showPostEvaluation) {
            // Écran d'évaluation post-réactivation
            PostEvaluationContent(
                postIntensity = postIntensity,
                onIntensityChange = { postIntensity = it },
                onSubmit = {
                    viewModel.submitPostIntensity(postIntensity)
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            )
        } else {
            // Écran de réactivation principal
            ReactivationContent(
                anchor = anchor,
                elapsedTime = uiState.elapsedTime,
                remainingTime = uiState.remainingTime,
                onFinish = { viewModel.finishReactivation() },
                primaryColor = primaryColor,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun ReactivationContent(
    anchor: com.novahypnose.ancrage.data.database.entities.Anchor,
    elapsedTime: Int,
    remainingTime: Int,
    onFinish: () -> Unit,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Animation pulsante
        PulsingCircle(
            color = Color.White,
            size = 120.dp,
            pulsationsPerMinute = 5
        )

        // Mot-clé / phrase
        anchor.keywordPhrase?.let {
            Text(
                text = it,
                style = AnchorKeywordStyle,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 32.dp)
            )
        }

        // Message d'accompagnement
        Text(
            text = stringResource(R.string.reactivation_message_1),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.reactivation_message_2),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        // Temps restant
        Text(
            text = "${remainingTime}s",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White.copy(alpha = 0.7f),
            fontWeight = FontWeight.Light
        )

        // Bouton terminer
        TextButton(
            onClick = onFinish,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.finish_now),
                color = Color.White
            )
        }
    }
}

@Composable
private fun PostEvaluationContent(
    postIntensity: Int,
    onIntensityChange: (Int) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.evaluation_post_question),
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = Color.White.copy(alpha = 0.9f)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IntensitySlider(
                    value = postIntensity,
                    onValueChange = onIntensityChange,
                    label = "Comment vous sentez-vous ?"
                )

                Button(
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text("Enregistrer")
                }
            }
        }
    }
}
