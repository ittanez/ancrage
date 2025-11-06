# AncrAge - Application Android

Application mobile Android de création et réactivation d'ancrages positifs sensoriels basée sur les principes de la PNL (Programmation Neuro-Linguistique).

## 📱 Description

**AncrAge** est une application Android permettant aux utilisateurs de créer, renforcer et réactiver des ancrages positifs (états internes ressources) basés sur des souvenirs agréables, sensations corporelles ou images mentales.

L'application s'appuie sur les principes de la PNL et constitue un journal sensoriel interactif où l'utilisateur encode consciemment ses états positifs dans un stimulus (geste, mot, image) réactivable à volonté.

### Public cible

- Particuliers souhaitant renforcer confiance, sérénité, motivation
- Pratiquants d'auto-hypnose ou de PNL
- Clients de thérapeutes cherchant à prolonger le travail de séance

## ✨ Fonctionnalités principales (Phase 1 - MVP)

### Création d'ancrages
- Choix parmi 5 états émotionnels prédéfinis : Sérénité, Confiance, Énergie, Bienveillance, Calme intérieur
- Parcours guidé en 4 étapes :
  1. Sélection de la ressource émotionnelle
  2. Évocation guidée (méditation courte)
  3. Création de l'ancrage sensoriel (visuel/verbal/kinesthésique)
  4. Évaluation de l'intensité émotionnelle (échelle 1-10)

### Réactivation d'ancrages
- Interface immersive avec animations pulsantes
- Durée personnalisable (20-60 secondes)
- Messages d'accompagnement apaisants
- Vibrations tactiles douces (optionnelles)
- Évaluation post-réactivation

### Gestion des ancrages
- Vue en grille avec cartes visuelles colorées
- Affichage des statistiques (nombre de réactivations, date de création)
- Système de favoris
- Archivage des ancrages

### Paramètres
- Thème clair/sombre/automatique
- Taille de police ajustable
- Intensité des animations
- Activation/désactivation des vibrations
- Gestion des données (export/import JSON)

## 🛠️ Technologies utilisées

- **Langage** : Kotlin
- **Architecture** : MVVM (Model-View-ViewModel)
- **UI** : Jetpack Compose + Material Design 3
- **Base de données** : Room (SQLite)
- **Gestion d'état** : StateFlow / LiveData
- **Navigation** : Jetpack Navigation Compose
- **Stockage** : DataStore (préférences)
- **API Android** : Vibrator, NotificationManager

## 📋 Prérequis

- **Android Studio** : Hedgehog (2023.1.1) ou supérieur
- **JDK** : 17
- **Android SDK** : API 26 (Android 8.0) minimum, API 34 (Android 14) cible
- **Gradle** : 8.2.0+

## 🚀 Installation et lancement

### 1. Cloner le dépôt

```bash
git clone https://github.com/votre-repo/ancrage.git
cd ancrage
```

### 2. Ouvrir le projet dans Android Studio

1. Ouvrir Android Studio
2. Sélectionner "Open an existing project"
3. Naviguer vers le dossier cloné et l'ouvrir

### 3. Synchroniser Gradle

Android Studio devrait automatiquement synchroniser les dépendances Gradle. Si ce n'est pas le cas :
- Cliquer sur **File > Sync Project with Gradle Files**

### 4. Configurer un émulateur ou connecter un appareil

**Option A : Émulateur**
- Ouvrir le **Device Manager** dans Android Studio
- Créer un AVD (Android Virtual Device) avec API 26 ou supérieur

**Option B : Appareil physique**
- Activer le mode développeur sur votre appareil Android
- Activer le débogage USB
- Connecter l'appareil via USB

### 5. Lancer l'application

- Cliquer sur le bouton **Run** (▶️) dans Android Studio
- Ou utiliser le raccourci : `Shift + F10` (Windows/Linux) ou `Ctrl + R` (Mac)

## 📁 Structure du projet

```
app/
├── src/main/
│   ├── java/com/novahypnose/ancrage/
│   │   ├── data/
│   │   │   ├── database/
│   │   │   │   ├── entities/         # Entités Room (Anchor, Reactivation, Reminder)
│   │   │   │   ├── dao/              # Data Access Objects
│   │   │   │   └── AncrageDatabase.kt
│   │   │   ├── repository/            # Couche Repository
│   │   │   └── models/                # Modèles de données (EmotionType)
│   │   │
│   │   ├── ui/
│   │   │   ├── theme/                 # Thème Material Design 3
│   │   │   ├── components/            # Composants réutilisables
│   │   │   └── screens/               # Écrans de l'application
│   │   │       ├── home/              # Écran d'accueil
│   │   │       ├── creation/          # Parcours de création
│   │   │       ├── reactivation/      # Écran de réactivation
│   │   │       └── settings/          # Paramètres
│   │   │
│   │   ├── utils/                     # Utilitaires (Vibration, Notifications, Date)
│   │   ├── navigation/                # Configuration de la navigation
│   │   └── MainActivity.kt            # Activité principale
│   │
│   ├── res/
│   │   ├── values/
│   │   │   ├── strings.xml            # Textes de l'application
│   │   │   ├── colors.xml             # Palette de couleurs
│   │   │   └── themes.xml
│   │   └── drawable/                  # Ressources graphiques
│   │
│   └── AndroidManifest.xml
│
└── build.gradle.kts
```

## 🎨 Design et UX

### Charte graphique

**Couleurs émotionnelles :**
- 🌞 Sérénité : Bleu ciel (#87CEEB)
- 💪 Confiance : Orange chaleureux (#FF8C42)
- 💫 Énergie : Jaune lumineux (#FFD700)
- ❤️ Bienveillance : Rose doux (#FFB6C1)
- 🌙 Calme intérieur : Violet apaisant (#9370DB)

### Principes de design

- **Minimalisme** : Interface épurée avec espaces blancs généreux
- **Fluidité** : Animations douces et organiques (300-500ms)
- **Accessibilité** : Contraste WCAG AA, support TalkBack, tailles tactiles ≥ 48dp

## 🗄️ Base de données

### Tables principales

#### `anchors`
Stocke les ancrages créés par l'utilisateur
- `id`, `emotion_type`, `color_hex`, `keyword_phrase`, `initial_intensity`, `use_count`, etc.

#### `reactivations`
Historique des réactivations
- `id`, `anchor_id`, `timestamp`, `pre_intensity`, `post_intensity`, `duration`

#### `reminders`
Configuration des rappels (Phase 2)
- `id`, `anchor_id`, `frequency`, `time_of_day`, `is_active`

## 🔐 Confidentialité et sécurité

- ✅ **100% local** : Aucune donnée transmise à un serveur
- ✅ **Pas de connexion internet requise**
- ✅ **Pas d'analytics tierces**
- ✅ **Conforme RGPD** (pas de collecte de données)
- ✅ **Export/import** : Contrôle total de vos données

## 🧪 Tests

### Lancer les tests unitaires

```bash
./gradlew test
```

### Lancer les tests instrumentés (nécessite un émulateur ou appareil)

```bash
./gradlew connectedAndroidTest
```

## 📦 Build de production

### Créer un APK de debug

```bash
./gradlew assembleDebug
```

L'APK sera généré dans : `app/build/outputs/apk/debug/`

### Créer un APK de release

```bash
./gradlew assembleRelease
```

**Note** : Pour signer l'APK de release, configurez d'abord un keystore dans `app/build.gradle.kts`

## 🔮 Roadmap

### Phase 2 (Fonctionnalités étendues)
- Rappels automatiques (notifications)
- Journal de progression avec statistiques
- Graphiques d'évolution
- Export/import JSON fonctionnel
- Tutoriel interactif

### Phase 3 (Optimisation)
- Widgets Android
- Animations Lottie avancées
- Personnalisation couleurs custom
- Support mode paysage (tablettes)

### Phase 4 (Publication)
- Tests qualité complets
- Publication sur Google Play Store

## 📄 Licence

Ce projet est sous licence MIT.

## 👥 Contributeurs

- **Alain** - Hypnothérapeute, créateur du concept
- **Équipe de développement** - Implémentation Android

## ⚠️ Disclaimer

AncrAge est un outil de bien-être personnel. Il ne remplace pas un suivi médical ou psychologique professionnel. En cas de détresse ou troubles psychologiques, consultez un professionnel de santé.

---

**Version** : 1.0.0-MVP
**Date** : Novembre 2025
**Plateforme** : Android 8.0+ (API 26+)