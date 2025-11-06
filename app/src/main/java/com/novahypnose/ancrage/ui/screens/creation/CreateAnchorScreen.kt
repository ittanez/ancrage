package com.novahypnose.ancrage.ui.screens.creation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.novahypnose.ancrage.R
import com.novahypnose.ancrage.data.models.EmotionType
import com.novahypnose.ancrage.ui.components.IntensitySlider
import com.novahypnose.ancrage.ui.components.PulsingCircle
import com.novahypnose.ancrage.ui.theme.toColor
import kotlinx.coroutines.delay

/**
 * Écran de création d'ancrage (4 étapes)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAnchorScreen(
    viewModel: CreationViewModel,
    onNavigateBack: () -> Unit,
    onAnchorCreated: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.create_anchor)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Indicateur de progression
            LinearProgressIndicator(
                progress = uiState.currentStep.toFloat() / 4f,
                modifier = Modifier.fillMaxWidth()
            )

            // Contenu de l'étape
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (uiState.currentStep) {
                    1 -> ChooseEmotionStep(
                        selectedEmotion = uiState.selectedEmotion,
                        onEmotionSelected = { viewModel.selectEmotionType(it) }
                    )
                    2 -> GuidedEvocationStep()
                    3 -> CreateAnchorStep(
                        keywordPhrase = uiState.keywordPhrase,
                        onKeywordChange = { viewModel.setKeywordPhrase(it) },
                        kinestheticGesture = uiState.kinestheticGesture,
                        onKinestheticChange = { viewModel.setKinestheticGesture(it) },
                        color = uiState.colorHex
                    )
                    4 -> EvaluationStep(
                        intensity = uiState.initialIntensity,
                        onIntensityChange = { viewModel.setInitialIntensity(it) }
                    )
                }
            }

            // Boutons de navigation
            NavigationButtons(
                currentStep = uiState.currentStep,
                isStepValid = viewModel.isCurrentStepValid(),
                onPrevious = { viewModel.previousStep() },
                onNext = { viewModel.nextStep() },
                onSave = {
                    viewModel.saveAnchor(
                        onSuccess = onAnchorCreated,
                        onError = { errorMessage = it }
                    )
                },
                modifier = Modifier.padding(16.dp)
            )
        }

        // Afficher les erreurs
        errorMessage?.let { error ->
            AlertDialog(
                onDismissRequest = { errorMessage = null },
                title = { Text("Erreur") },
                text = { Text(error) },
                confirmButton = {
                    TextButton(onClick = { errorMessage = null }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
private fun ChooseEmotionStep(
    selectedEmotion: EmotionType?,
    onEmotionSelected: (EmotionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.step_choose_resource),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        EmotionType.entries.forEach { emotion ->
            EmotionCard(
                emotion = emotion,
                isSelected = selectedEmotion == emotion,
                onClick = { onEmotionSelected(emotion) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun EmotionCard(
    emotion: EmotionType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryColor = emotion.primaryColorHex.toColor()
    val borderColor = if (isSelected) primaryColor else Color.Transparent

    Card(
        modifier = modifier
            .border(3.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(primaryColor.copy(alpha = 0.2f))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emotion.emoji,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = emotion.displayName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun GuidedEvocationStep(
    modifier: Modifier = Modifier
) {
    val guidedTexts = listOf(
        stringResource(R.string.guided_text_1),
        stringResource(R.string.guided_text_2),
        stringResource(R.string.guided_text_3)
    )

    var currentTextIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (currentTextIndex < guidedTexts.size - 1) {
            delay(4000) // 4 secondes par texte
            currentTextIndex++
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PulsingCircle(
            color = MaterialTheme.colorScheme.primary,
            size = 100.dp,
            pulsationsPerMinute = 5
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = guidedTexts[currentTextIndex],
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun CreateAnchorStep(
    keywordPhrase: String,
    onKeywordChange: (String) -> Unit,
    kinestheticGesture: Boolean,
    onKinestheticChange: (Boolean) -> Unit,
    color: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.step_create_anchor),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = keywordPhrase,
            onValueChange = onKeywordChange,
            label = { Text("Mot-clé ou phrase") },
            placeholder = { Text("Ex: Je suis calme et serein") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            maxLines = 2
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = kinestheticGesture,
                onCheckedChange = onKinestheticChange
            )
            Text(
                text = "Ajouter un geste kinesthésique",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Text(
            text = stringResource(R.string.anchor_explanation),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun EvaluationStep(
    intensity: Int,
    onIntensityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.evaluation_question),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        IntensitySlider(
            value = intensity,
            onValueChange = onIntensityChange
        )
    }
}

@Composable
private fun NavigationButtons(
    currentStep: Int,
    isStepValid: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (currentStep > 1) {
            TextButton(onClick = onPrevious) {
                Text(stringResource(R.string.previous))
            }
        } else {
            Spacer(modifier = Modifier.width(1.dp))
        }

        if (currentStep < 4) {
            Button(
                onClick = onNext,
                enabled = isStepValid
            ) {
                Text(stringResource(R.string.next))
            }
        } else {
            Button(
                onClick = onSave,
                enabled = isStepValid
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}
