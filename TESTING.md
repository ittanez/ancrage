# 🧪 Guide de Test - AncrAge MVP

Ce guide vous explique comment tester l'application AncrAge étape par étape.

---

## 📋 Prérequis

### Logiciels nécessaires
- **Android Studio** : Hedgehog (2023.1.1) ou plus récent
  - Télécharger : https://developer.android.com/studio
- **JDK 17** : Inclus avec Android Studio
- **Android SDK** : API 26 minimum, API 34 recommandé

### Matériel nécessaire
**Option A : Émulateur Android (recommandé pour les tests)**
- Processeur avec support de virtualisation (Intel VT-x ou AMD-V)
- 8 GB RAM minimum (16 GB recommandé)
- 10 GB d'espace disque libre

**Option B : Appareil Android physique**
- Android 8.0 (Oreo) ou supérieur
- Mode développeur activé
- Débogage USB activé

---

## 🚀 Méthode 1 : Test avec Android Studio (Recommandé)

### Étape 1 : Ouvrir le projet

1. **Lancer Android Studio**
2. **Cliquer sur "Open"** ou **File → Open**
3. **Naviguer vers** `/home/user/ancrage` (ou l'emplacement de votre clone)
4. **Sélectionner le dossier** et cliquer sur **OK**

### Étape 2 : Synchronisation Gradle

Android Studio va automatiquement :
- Télécharger les dépendances Gradle
- Configurer le projet
- Indexer les fichiers

⏱️ **Temps estimé** : 2-5 minutes (première fois)

**Si la synchronisation échoue** :
- Cliquer sur **File → Sync Project with Gradle Files**
- Vérifier votre connexion internet
- Vérifier que JDK 17 est configuré : **File → Project Structure → SDK Location**

### Étape 3 : Configurer un émulateur

1. **Ouvrir le Device Manager** : Icône de téléphone dans la barre d'outils
2. **Créer un nouveau device** : Bouton **"+"** ou **Create Virtual Device**
3. **Choisir un appareil** :
   - Recommandé : **Pixel 6** ou **Pixel 7**
   - Taille écran : 6.0" minimum
4. **Sélectionner l'image système** :
   - Recommandé : **API 34 (Android 14)** avec Google Play
   - Minimum : **API 26 (Android 8.0)**
5. **Configurer l'AVD** :
   - RAM : 2048 MB minimum
   - Stockage : 2 GB
6. **Finish** pour créer l'émulateur

### Étape 4 : Lancer l'application

1. **Sélectionner votre émulateur** dans le menu déroulant en haut
2. **Cliquer sur le bouton Run** (▶️) ou **Shift + F10**
3. **Attendre** que l'émulateur démarre (30-60 secondes)
4. L'application **AncrAge** devrait se lancer automatiquement

---

## 🖥️ Méthode 2 : Test avec appareil physique

### Étape 1 : Activer le mode développeur

**Sur votre appareil Android** :

1. Aller dans **Paramètres → À propos du téléphone**
2. Taper **7 fois** sur **Numéro de build**
3. Un message "Vous êtes maintenant développeur" apparaît

### Étape 2 : Activer le débogage USB

1. Aller dans **Paramètres → Système → Options de développement**
2. Activer **Débogage USB**
3. Activer **Installation via USB** (si disponible)

### Étape 3 : Connecter l'appareil

1. **Connecter** votre appareil à l'ordinateur via USB
2. Sur l'appareil, **autoriser** le débogage USB (popup)
3. Dans Android Studio, votre appareil devrait apparaître dans le menu déroulant

### Étape 4 : Lancer l'application

1. **Sélectionner votre appareil** dans le menu
2. **Cliquer sur Run** (▶️)
3. L'application s'installe et se lance sur votre appareil

---

## 🛠️ Méthode 3 : Compilation en ligne de commande

### Vérifier que Gradle fonctionne

```bash
cd /home/user/ancrage
./gradlew --version
```

### Compiler le projet

```bash
# Compiler en mode debug
./gradlew assembleDebug

# Compiler en mode release (non signé)
./gradlew assembleRelease
```

### Localiser l'APK

L'APK sera généré dans :
```
app/build/outputs/apk/debug/app-debug.apk
```

### Installer l'APK sur un appareil

```bash
# Via ADB (Android Debug Bridge)
adb install app/build/outputs/apk/debug/app-debug.apk

# Ou copier l'APK sur l'appareil et l'installer manuellement
```

---

## ✅ Checklist de Test du MVP

### 🏠 Écran d'accueil

- [ ] L'application démarre sans crash
- [ ] Le message d'accueil s'affiche correctement
- [ ] Le bouton "Créer un ancrage" est visible et fonctionnel
- [ ] La grille d'ancrages est vide au premier lancement
- [ ] Le bouton paramètres (⚙️) est accessible

### ➕ Création d'ancrage

#### Étape 1 : Choix de l'émotion
- [ ] Les 5 émotions s'affichent avec leurs couleurs et emojis :
  - 🌞 Sérénité (bleu ciel)
  - 💪 Confiance (orange)
  - 💫 Énergie (jaune)
  - ❤️ Bienveillance (rose)
  - 🌙 Calme intérieur (violet)
- [ ] La sélection d'une émotion met en surbrillance la carte
- [ ] Le bouton "Suivant" est activé après sélection

#### Étape 2 : Évocation guidée
- [ ] Les textes guidés s'affichent progressivement (3 textes)
- [ ] L'animation pulsante fonctionne (cercle qui pulse doucement)
- [ ] Le bouton "Suivant" permet de passer cette étape
- [ ] La barre de progression indique 50% (2/4)

#### Étape 3 : Création de l'ancrage
- [ ] Le champ texte accepte un mot-clé ou phrase (max 50 caractères)
- [ ] La checkbox "Geste kinesthésique" est fonctionnelle
- [ ] Le texte explicatif s'affiche en bas
- [ ] Le bouton "Suivant" est désactivé si le champ est vide

#### Étape 4 : Évaluation
- [ ] Le slider d'intensité fonctionne (1-10)
- [ ] La valeur numérique s'affiche clairement
- [ ] La description textuelle change selon la valeur
- [ ] Le bouton "Enregistrer" sauvegarde l'ancrage
- [ ] Retour à l'écran d'accueil après sauvegarde

### 🔄 Réactivation d'ancrage

- [ ] Cliquer sur une carte d'ancrage ouvre l'écran de réactivation
- [ ] La couleur de fond correspond à l'émotion de l'ancrage
- [ ] L'animation pulsante démarre automatiquement
- [ ] Le mot-clé/phrase s'affiche en grand
- [ ] Les messages d'accompagnement sont visibles
- [ ] Le compte à rebours fonctionne (ex: 30 secondes)
- [ ] Le bouton "Terminer maintenant" fonctionne
- [ ] L'écran d'évaluation post-réactivation s'affiche
- [ ] Le slider d'évaluation fonctionne
- [ ] Le bouton "Enregistrer" sauvegarde et retourne à l'accueil

### 📊 Gestion des ancrages

- [ ] Les ancrages créés s'affichent dans la grille
- [ ] Chaque carte affiche :
  - Emoji de l'émotion
  - Mot-clé/phrase
  - Date de création
  - Nombre de réactivations
  - Jauge d'intensité
- [ ] Long press sur une carte ouvre le menu d'options (TODO Phase 2)
- [ ] Les ancrages favoris affichent une étoile ⭐

### ⚙️ Paramètres

#### Apparence
- [ ] Changement de thème fonctionne (Clair/Sombre/Auto)
- [ ] Changement de taille de police fonctionne (Petit/Moyen/Grand)
- [ ] Changement d'intensité d'animation fonctionne

#### Sons et vibrations
- [ ] Toggle "Activer les vibrations" fonctionne
- [ ] Sélection de l'intensité de vibration fonctionne (Faible/Moyenne/Forte)
- [ ] Les vibrations se déclenchent pendant la réactivation (si activées)

#### Données
- [ ] Les boutons Export/Import sont visibles (TODO Phase 2)
- [ ] Le bouton Réinitialiser affiche une confirmation
- [ ] La réinitialisation efface tous les paramètres

### 📱 Tests généraux

- [ ] Rotation de l'écran fonctionne (si supportée)
- [ ] Retour arrière (bouton back) fonctionne correctement
- [ ] Aucun crash lors de la navigation
- [ ] Les animations sont fluides (60 FPS)
- [ ] Pas de lag perceptible
- [ ] La consommation de batterie est raisonnable

### 🔐 Confidentialité et sécurité

- [ ] Aucune connexion internet n'est requise
- [ ] L'application fonctionne en mode avion
- [ ] Les données sont stockées localement
- [ ] Aucune demande de permissions invasives

---

## 🐛 Tests de cas limites

### Comportements à vérifier

1. **Création sans réseau** : L'application fonctionne-t-elle hors ligne ?
2. **Base de données vide** : Message approprié quand aucun ancrage ?
3. **Nombre important d'ancrages** : Performance avec 20+ ancrages ?
4. **Texte très long** : Troncature correcte des phrases longues ?
5. **Rotation pendant création** : L'état est-il préservé ?
6. **Interruption (appel téléphonique)** : L'app reprend correctement ?
7. **Batterie faible** : Aucun crash en mode économie d'énergie ?

---

## 📝 Rapport de bugs

Si vous trouvez un bug, notez :

1. **Appareil/Émulateur** : Modèle et version Android
2. **Étapes de reproduction** : Comment reproduire le bug
3. **Comportement attendu** : Ce qui devrait se passer
4. **Comportement observé** : Ce qui se passe réellement
5. **Captures d'écran** : Si possible
6. **Logs** : Via Logcat dans Android Studio

### Récupérer les logs

```bash
# Via ADB
adb logcat | grep "AncrAge"

# Ou dans Android Studio
View → Tool Windows → Logcat
```

---

## ✨ Tests de l'expérience utilisateur

### Points à évaluer

1. **Première impression** : L'interface est-elle accueillante ?
2. **Intuitivité** : Peut-on créer un ancrage sans instructions ?
3. **Fluidité** : Les animations sont-elles apaisantes ?
4. **Clarté** : Les textes sont-ils compréhensibles ?
5. **Utilité** : L'application répond-elle au besoin ?
6. **Performance** : Y a-t-il des lenteurs ?
7. **Accessibilité** : TalkBack fonctionne-t-il ?

---

## 🎯 Critères de validation du MVP

Le MVP est validé si :

- ✅ Toutes les fonctionnalités de la checklist fonctionnent
- ✅ Aucun crash lors de l'utilisation normale
- ✅ Les animations sont fluides (> 45 FPS)
- ✅ L'application fonctionne hors ligne
- ✅ Les données sont persistées entre les sessions
- ✅ L'interface est claire et intuitive
- ✅ Les vibrations fonctionnent (si activées)

---

## 📞 Support

Pour toute question ou problème :
- Ouvrir une issue sur GitHub
- Consulter la documentation : `README.md`
- Vérifier les logs Logcat pour les erreurs

---

**Bon test ! 🚀**
