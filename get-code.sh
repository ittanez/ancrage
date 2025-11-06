#!/bin/bash

# 🚀 Script pour récupérer le code de l'application AncrAge
# Exécutez ce script sur votre PC

echo "=================================="
echo "📱 Récupération du code AncrAge"
echo "=================================="
echo ""

# Vérifier qu'on est dans un dépôt git
if [ ! -d ".git" ]; then
    echo "❌ Erreur: Vous n'êtes pas dans un dépôt git"
    echo "Naviguez vers le dossier 'ancrage' d'abord"
    exit 1
fi

echo "📍 Dossier actuel: $(pwd)"
echo ""

# Afficher la branche actuelle
CURRENT_BRANCH=$(git branch --show-current)
echo "🔹 Branche actuelle: $CURRENT_BRANCH"
echo ""

# Récupérer toutes les branches du serveur
echo "📥 Récupération des branches distantes..."
git fetch origin

echo ""
echo "📋 Branches disponibles:"
git branch -r | grep "claude/ancrage-android-app"

echo ""
echo "🔄 Changement vers la branche de développement..."
git checkout claude/ancrage-android-app-011CUrm8E33K6TXx1BHBByrH

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Succès ! Vous êtes maintenant sur la branche de développement"
    echo ""
    echo "📊 Fichiers récupérés:"
    echo "   - $(find app/src/main/java -name '*.kt' 2>/dev/null | wc -l) fichiers Kotlin"
    echo "   - $(find app/src/main/res -name '*.xml' 2>/dev/null | wc -l) fichiers XML"
    echo ""
    echo "🎉 Le code de l'application est maintenant sur votre PC !"
    echo ""
    echo "Prochaines étapes:"
    echo "  1. Ouvrir le projet dans Android Studio"
    echo "  2. Laisser Gradle synchroniser"
    echo "  3. Lancer l'application"
else
    echo ""
    echo "❌ Erreur lors du changement de branche"
    echo "Consultez les messages d'erreur ci-dessus"
fi
