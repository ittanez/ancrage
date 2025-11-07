# 🎙️ Spécifications TTS (Text-to-Speech) pour AncrAge

## Vue d'ensemble

Implémentation du guidage vocal avec **Android TextToSpeech API** pour la phase d'évocation guidée et la réactivation.

---

## 📋 Textes à vocaliser

### Étape 2 : Évocation guidée (4 audios)

**Audio 1 (10 secondes)**
```
Fermez un instant les yeux, ou laissez votre regard se poser.
```

**Audio 2 (15 secondes)**
```
Revenez à un moment où vous avez ressenti cette émotion.
```

**Audio 3 (15 secondes)**
```
Laissez votre corps retrouver cette sensation... Doucement.
```

**Audio 4 (8 secondes)**
```
Lorsque vous êtes prêt, ouvrez les yeux et cliquez sur suivant pour créer votre ancrage.
```

### Écran de réactivation (2 audios)

**Audio début (8 secondes)**
```
Prenez une profonde inspiration. Reconnectez-vous à cet état ressource.
```

**Audio fin (5 secondes)**
```
Très bien. Lorsque vous êtes prêt, vous pouvez ouvrir les yeux.
```

---

## 🔧 Implémentation technique

### 1. Classe TTS Helper

```kotlin
package com.novahypnose.ancrage.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

/**
 * Helper pour gérer la synthèse vocale (TTS)
 */
class TTSHelper(private val context: Context) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    /**
     * Initialise le TTS avec une voix française douce
     */
    suspend fun initialize(): Boolean = suspendCancellableCoroutine { continuation ->
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.let { engine ->
                    // Configurer la voix française
                    val result = engine.setLanguage(Locale.FRENCH)

                    if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Fallback sur anglais si français pas disponible
                        engine.setLanguage(Locale.US)
                    }

                    // Vitesse légèrement ralentie pour effet apaisant
                    engine.setSpeechRate(0.85f)

                    // Pitch légèrement plus grave pour effet calmant
                    engine.setPitch(0.9f)

                    isInitialized = true
                    continuation.resume(true)
                }
            } else {
                continuation.resume(false)
            }
        }

        continuation.invokeOnCancellation {
            shutdown()
        }
    }

    /**
     * Parle un texte avec callback de fin
     */
    suspend fun speak(
        text: String,
        utteranceId: String = UUID.randomUUID().toString()
    ): Boolean = suspendCancellableCoroutine { continuation ->

        if (!isInitialized || tts == null) {
            continuation.resume(false)
            return@suspendCancellableCoroutine
        }

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}

            override fun onDone(utteranceId: String?) {
                continuation.resume(true)
            }

            override fun onError(utteranceId: String?) {
                continuation.resume(false)
            }
        })

        val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)

        if (result == TextToSpeech.ERROR) {
            continuation.resume(false)
        }
    }

    /**
     * Arrête la lecture en cours
     */
    fun stop() {
        tts?.stop()
    }

    /**
     * Libère les ressources
     */
    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }

    /**
     * Vérifie si le TTS est prêt
     */
    fun isReady(): Boolean = isInitialized && tts != null
}
```

### 2. Mise à jour GuidedEvocationScreen

```kotlin
@Composable
private fun GuidedEvocationStep(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val ttsHelper = remember { TTSHelper(context) }

    val guidedTexts = listOf(
        stringResource(R.string.guided_text_1),
        stringResource(R.string.guided_text_2),
        stringResource(R.string.guided_text_3),
        stringResource(R.string.guided_text_4)
    )

    var currentTextIndex by remember { mutableIntStateOf(0) }
    var isSpeaking by remember { mutableStateOf(false) }

    // Initialiser TTS
    LaunchedEffect(Unit) {
        ttsHelper.initialize()
    }

    // Jouer les textes en séquence
    LaunchedEffect(currentTextIndex) {
        if (currentTextIndex < guidedTexts.size) {
            isSpeaking = true
            ttsHelper.speak(guidedTexts[currentTextIndex])
            delay(2000) // Pause entre les textes
            isSpeaking = false

            if (currentTextIndex < guidedTexts.size - 1) {
                delay(1000)
                currentTextIndex++
            }
        }
    }

    // Nettoyer à la sortie
    DisposableEffect(Unit) {
        onDispose {
            ttsHelper.shutdown()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animation pulsante
        PulsingCircle(
            color = MaterialTheme.colorScheme.primary,
            size = 100.dp,
            pulsationsPerMinute = 5
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Texte affiché
        Text(
            text = guidedTexts[currentTextIndex],
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Indicateur de progression
        LinearProgressIndicator(
            progress = (currentTextIndex + 1).toFloat() / guidedTexts.size,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        // Boutons de contrôle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Bouton Skip
            TextButton(
                onClick = {
                    ttsHelper.stop()
                    currentTextIndex = guidedTexts.size - 1
                }
            ) {
                Icon(Icons.Default.SkipNext, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Passer")
            }

            // Bouton Pause/Reprendre
            IconButton(
                onClick = {
                    if (isSpeaking) {
                        ttsHelper.stop()
                        isSpeaking = false
                    } else {
                        // Relire le texte actuel
                        // TODO: Implémenter
                    }
                }
            ) {
                Icon(
                    if (isSpeaking) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }
        }
    }
}
```

### 3. Mise à jour ReactivationScreen avec TTS

```kotlin
@Composable
fun ReactivationScreen(
    viewModel: ReactivationViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val ttsHelper = remember { TTSHelper(context) }
    val uiState by viewModel.uiState.collectAsState()

    // Initialiser TTS
    LaunchedEffect(Unit) {
        ttsHelper.initialize()

        // Parler au début
        delay(1000)
        ttsHelper.speak(
            "Prenez une profonde inspiration. Reconnectez-vous à cet état ressource."
        )
    }

    // Parler à la fin
    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            ttsHelper.speak(
                "Très bien. Lorsque vous êtes prêt, vous pouvez ouvrir les yeux."
            )
        }
    }

    // Nettoyer
    DisposableEffect(Unit) {
        onDispose {
            ttsHelper.shutdown()
        }
    }

    // ... reste du code ...
}
```

### 4. Ajout de strings

```xml
<!-- strings.xml -->
<string name="guided_text_1">Fermez un instant les yeux, ou laissez votre regard se poser.</string>
<string name="guided_text_2">Revenez à un moment où vous avez ressenti cette émotion.</string>
<string name="guided_text_3">Laissez votre corps retrouver cette sensation... Doucement.</string>
<string name="guided_text_4">Lorsque vous êtes prêt, ouvrez les yeux et cliquez sur suivant pour créer votre ancrage.</string>

<string name="reactivation_intro">Prenez une profonde inspiration. Reconnectez-vous à cet état ressource.</string>
<string name="reactivation_outro">Très bien. Lorsque vous êtes prêt, vous pouvez ouvrir les yeux.</string>
```

### 5. Paramètres utilisateur pour le TTS

```kotlin
// Dans SettingsScreen
Switch(
    checked = ttsEnabled,
    onCheckedChange = { viewModel.setTTSEnabled(it) }
)
Text("Guidage vocal")

// Sélecteur de vitesse
Slider(
    value = ttsSpeed,
    onValueChange = { viewModel.setTTSSpeed(it) },
    valueRange = 0.5f..1.5f,
    steps = 10
)
Text("Vitesse de la voix: ${String.format("%.1f", ttsSpeed)}x")

// Sélecteur de pitch
Slider(
    value = ttsPitch,
    onValueChange = { viewModel.setTTSPitch(it) },
    valueRange = 0.7f..1.3f,
    steps = 6
)
Text("Tonalité de la voix")
```

---

## 📦 Dépendances

Aucune dépendance externe nécessaire ! TextToSpeech est inclus dans Android SDK.

---

## ⚙️ Configuration requise

- **Permission** : Aucune (TTS ne nécessite pas de permission spéciale)
- **API Level** : 21+ (déjà compatible avec notre minSdk 26)
- **Données vocales** : L'utilisateur doit avoir les voix françaises installées
  - Si non disponible : Fallback sur anglais
  - Possibilité de proposer le téléchargement

---

## 🧪 Tests à effectuer

1. **Test voix française disponible**
   - Sur émulateur avec voix FR installées
   - Sur appareil physique

2. **Test fallback anglais**
   - Désinstaller voix FR
   - Vérifier que l'app fonctionne en anglais

3. **Test interruption**
   - Appel téléphonique pendant TTS
   - Changement d'écran
   - Fermeture app

4. **Test performances**
   - Latence au démarrage
   - Utilisation mémoire
   - Batterie

---

## 🎨 Améliorations futures (v2.0+)

1. **Voix enregistrée personnelle**
   - Enregistrement avec MediaRecorder
   - Stockage local des audios
   - Lecture avec MediaPlayer

2. **Voix professionnelle**
   - Téléchargement d'audios pré-enregistrés
   - Qualité studio
   - Plusieurs voix au choix (homme/femme)

3. **Musique de fond**
   - Musique douce pendant évocation
   - Fondu enchaîné avec voix
   - Volume ajustable

---

## 📝 Notes d'implémentation

- **Ordre d'implémentation** : Après résolution problème Gradle
- **Durée estimée** : 2-3 jours
- **Complexité** : Moyenne
- **Impact utilisateur** : ⭐⭐⭐⭐⭐ (très élevé)

---

## ✅ Checklist d'implémentation

- [ ] Créer `TTSHelper.kt`
- [ ] Ajouter les strings audio
- [ ] Modifier `GuidedEvocationStep`
- [ ] Modifier `ReactivationScreen`
- [ ] Ajouter paramètres TTS dans Settings
- [ ] Tester sur émulateur
- [ ] Tester sur appareil physique
- [ ] Optimiser latence
- [ ] Gérer les erreurs
- [ ] Documentation utilisateur
