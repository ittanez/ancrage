# 🎯 Roadmap AncrAge - Améliorations post-MVP

## Phase 1.5 : Améliorations UX et pédagogie (2-3 semaines)

### 1️⃣ FAQ et Explications
- [ ] Créer écran FAQ/Aide
- [ ] Ajouter section "Qu'est-ce qu'un ancrage ?"
- [ ] Guide d'utilisation pas-à-pas
- [ ] Vidéo explicative (optionnel)
- [ ] Textes d'introduction pour chaque émotion
  - Sérénité : "Un ancrage de sérénité vous permet de..."
  - Confiance : "L'ancrage de confiance vous aide à..."
  - Énergie : "Cet ancrage d'énergie..."
  - Bienveillance : "L'ancrage de bienveillance..."
  - Calme intérieur : "Le calme intérieur vous reconnecte à..."

### 2️⃣ Audio Guidé (PRIORITÉ HAUTE)
**Étape évocation guidée :**
- [ ] Intégrer MediaPlayer Android
- [ ] Enregistrer ou générer les audios :
  - Audio 1 (10s) : "Fermez un instant les yeux, ou laissez votre regard se poser"
  - Audio 2 (15s) : "Revenez à un moment où vous avez ressenti cette émotion"
  - Audio 3 (15s) : "Laissez votre corps retrouver cette sensation... Doucement"
  - Audio final (8s) : "Lorsque vous êtes prêt, ouvrez les yeux et cliquez sur suivant"
- [ ] Boutons lecture/pause/skip
- [ ] Option voix masculine/féminine
- [ ] Volume réglable
- [ ] Alternative TTS (Text-to-Speech) si pas d'audios enregistrés

**Étape réactivation :**
- [ ] Audio de guidance pendant la réactivation
- [ ] Musique de fond douce (optionnel)
- [ ] Son de fin (gong doux)

### 3️⃣ Options d'ancrage enrichies
**Améliorer l'étape "Créer l'ancrage" :**

```kotlin
sealed class AnchorType {
    data class Keyword(val text: String) : AnchorType()  // OBLIGATOIRE
    data class Image(val description: String) : AnchorType()  // Optionnel
    data class Color(val hexColor: String) : AnchorType()  // Optionnel
    data class Gesture(val description: String) : AnchorType()  // Optionnel
    data class Place(val description: String) : AnchorType()  // Optionnel
}
```

**UI proposée :**
```
┌─────────────────────────────────────┐
│ Créer votre ancrage                 │
├─────────────────────────────────────┤
│                                     │
│ ✅ Mot-clé ou phrase (obligatoire) │
│ [Je suis confiant et serein      ] │
│                                     │
│ ☐ Ajouter une couleur              │
│   [Sélecteur de couleur]           │
│                                     │
│ ☐ Ajouter une image mentale        │
│   [Décrivez l'image que vous      ] │
│   [voyez dans votre esprit...     ] │
│                                     │
│ ☐ Ajouter un geste                 │
│   [Décrivez le geste              ] │
│   [Ex: Main sur le cœur           ] │
│                                     │
│ ☐ Ajouter un lieu ou situation     │
│   [Décrivez le lieu               ] │
│   [Ex: Plage au coucher du soleil ] │
│                                     │
│ Conseil: Plus vous ajoutez          │
│ d'éléments sensoriels, plus        │
│ l'ancrage sera puissant            │
└─────────────────────────────────────┘
```

### 4️⃣ Système de rappel amélioré
- [ ] Écran dédié "Mes rappels"
- [ ] Explication QUAND utiliser les rappels :
  ```
  "Les rappels sont utiles pour :
  - Renforcer un ancrage (3x/jour la 1ère semaine)
  - Maintenir un état ressource au quotidien
  - Préparer un événement important
  - Gérer le stress en anticipation"
  ```
- [ ] Suggestions intelligentes :
  - "Matin (8h) : Ancrez votre énergie pour la journée"
  - "Midi (12h) : Retrouvez votre calme"
  - "Soir (19h) : Renforcez votre confiance"
- [ ] Statistiques : "Vous avez réactivé cet ancrage 12 fois en 7 jours"

### 5️⃣ Écran d'accueil amélioré
- [ ] Ajouter un bouton "❓ Qu'est-ce qu'un ancrage ?"
- [ ] Tutoriel interactif au 1er lancement
- [ ] Message contextuel selon l'heure :
  - Matin : "Bonjour ! Prêt à ancrer votre énergie ?"
  - Soir : "Bonsoir ! Moment idéal pour un ancrage de calme"

---

## Phase 2 : Fonctionnalités avancées (4-6 semaines)

### 6️⃣ Journal et statistiques détaillées
- [ ] Graphiques d'évolution par ancrage
- [ ] Heatmap des moments de réactivation
- [ ] Insights : "Votre ancrage Confiance fonctionne mieux le matin"
- [ ] Export PDF du journal

### 7️⃣ Personnalisation avancée
- [ ] Upload photos pour les ancrages visuels
- [ ] Enregistrement audio personnel (sa propre voix)
- [ ] Musiques personnalisées
- [ ] Durée de réactivation ajustable par ancrage

### 8️⃣ Widgets et raccourcis
- [ ] Widget 2x2 : 1 ancrage favori
- [ ] Widget 4x2 : 4 ancrages
- [ ] Quick Tile (Android) pour réactivation rapide
- [ ] Shortcuts dynamiques

---

## Phase 3 : Monétisation et fonctionnalités premium (6-8 semaines)

### 9️⃣ Modèle Freemium
**Gratuit :**
- ✅ 1 ancrage
- ✅ Réactivations illimitées
- ✅ Fonctionnalités de base
- ✅ FAQ et tutoriels

**Premium (Abonnement) :**
- 🌟 Ancrages illimités
- 🌟 Audio guidé avec voix professionnelles
- 🌟 Upload d'images personnelles
- 🌟 Enregistrement audio personnel
- 🌟 Statistiques avancées
- 🌟 Export données
- 🌟 Thèmes personnalisés
- 🌟 Support prioritaire

**Prix suggérés :**
- 4,99€/mois
- 39,99€/an (économie 33%)
- Essai gratuit 7 jours

### 🔟 Intégration paiement
- [ ] Google Play Billing Library
- [ ] Écran de souscription
- [ ] Gestion des abonnements
- [ ] Restauration des achats
- [ ] Codes promo

---

## Phase 4 : Fonctionnalités communautaires (Futur)

### 1️⃣1️⃣ Partage et communauté
- [ ] Partage d'ancrages (anonyme)
- [ ] Bibliothèque d'ancrages publics
- [ ] Notation et commentaires
- [ ] Ancrages par thématiques :
  - Performance sportive
  - Examens/études
  - Gestion du stress
  - Sommeil
  - Relations

### 1️⃣2️⃣ Intégration avec thérapeutes
- [ ] Compte thérapeute
- [ ] Création d'ancrages pour clients
- [ ] Suivi des progrès clients
- [ ] Messagerie sécurisée
- [ ] Tableau de bord thérapeute

---

## Priorités immédiates (après compilation)

1. **FAQ et explications** (1 semaine)
2. **Audio guidé** (2 semaines)
3. **Options d'ancrage enrichies** (1 semaine)
4. **Textes d'introduction** (2 jours)
5. **Système de rappel clarifié** (1 semaine)

**Total Phase 1.5 : 3-4 semaines**

---

## Technologies nécessaires

### Audio :
- **MediaPlayer** (Android natif)
- **ExoPlayer** (pour audio avancé)
- **TextToSpeech** (TTS Android pour génération vocale)
- **Enregistrement** : AudioRecord (pour voix personnelle)

### Paiements :
- **Google Play Billing Library 6.0+**
- **RevenueCat** (optionnel, simplifie la gestion)

### Stockage images :
- **Coil** (déjà implémenté)
- **Glide** (alternative)
- **CameraX** (pour prise de photos)

---

## Ressources nécessaires

### Audio professionnel :
- **Voix off** : 200-500€ (enregistrement professionnel)
- **Alternative TTS** : Gratuit (Google TTS, voix naturelles)
- **Musique** : Epidemic Sound, AudioJungle (50-200€)

### Design :
- **Icônes custom** : 100-300€
- **Illustrations** : 200-500€
- **Animations Lottie** : 150-400€

### Légal (Premium) :
- **CGU/CGV** : 500-1000€
- **Politique de confidentialité** : Inclus
- **RGPD compliance** : 300-800€

---

## Budget estimé (Phase 1.5 + 2 + 3)

| Poste | Coût |
|-------|------|
| Développement (120h) | 6 000 - 12 000€ |
| Audio professionnel | 500 - 1 000€ |
| Design & illustrations | 500 - 1 500€ |
| Légal & conformité | 1 000 - 2 000€ |
| Marketing (lancement) | 1 000 - 3 000€ |
| **TOTAL** | **9 000 - 19 500€** |

**Alternative économique** : TTS + design DIY = 2 000 - 5 000€

---

## Métriques de succès

### KPIs gratuits :
- 1 000 installations (3 mois)
- Rétention J+7 : > 40%
- Note Play Store : > 4.5/5
- Taux de conversion premium : > 5%

### KPIs premium :
- 50 abonnés payants (6 mois)
- MRR (Monthly Recurring Revenue) : 200€+
- Churn rate : < 10%/mois
- LTV (Lifetime Value) : > 50€

---

**Cette roadmap vous convient-elle ? Voulez-vous qu'on commence par certaines fonctionnalités en particulier une fois la compilation réussie ? 🎯**
