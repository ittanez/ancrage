#!/bin/bash

# Script de test pour AncrAge
# Ce script vérifie que le projet Android compile correctement

echo "=================================="
echo "🧪 AncrAge - Script de Test"
echo "=================================="
echo ""

# Couleurs
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Vérifier que nous sommes dans le bon dossier
if [ ! -f "settings.gradle.kts" ]; then
    echo -e "${RED}❌ Erreur: Ce script doit être exécuté depuis le dossier racine du projet${NC}"
    exit 1
fi

echo "📁 Dossier du projet: $(pwd)"
echo ""

# Vérifier Java
echo "1️⃣ Vérification de Java..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1)
    echo -e "${GREEN}✓${NC} Java trouvé: $JAVA_VERSION"
else
    echo -e "${RED}❌ Java non trouvé. Veuillez installer JDK 17+${NC}"
    exit 1
fi
echo ""

# Vérifier Gradle
echo "2️⃣ Vérification de Gradle..."
if [ -f "gradlew" ]; then
    echo -e "${GREEN}✓${NC} Gradle wrapper trouvé"
    chmod +x gradlew
else
    echo -e "${RED}❌ Gradle wrapper non trouvé${NC}"
    exit 1
fi
echo ""

# Structure du projet
echo "3️⃣ Vérification de la structure du projet..."
REQUIRED_FILES=(
    "app/build.gradle.kts"
    "app/src/main/AndroidManifest.xml"
    "app/src/main/java/com/novahypnose/ancrage/MainActivity.kt"
)

ALL_PRESENT=true
for file in "${REQUIRED_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}✓${NC} $file"
    else
        echo -e "${RED}✗${NC} $file manquant"
        ALL_PRESENT=false
    fi
done

if [ "$ALL_PRESENT" = false ]; then
    echo -e "${RED}❌ Des fichiers essentiels sont manquants${NC}"
    exit 1
fi
echo ""

# Compter les fichiers Kotlin
echo "4️⃣ Statistiques du projet..."
KOTLIN_FILES=$(find app/src/main/java -name "*.kt" 2>/dev/null | wc -l)
XML_FILES=$(find app/src/main/res -name "*.xml" 2>/dev/null | wc -l)
echo -e "${GREEN}📝${NC} Fichiers Kotlin: $KOTLIN_FILES"
echo -e "${GREEN}📄${NC} Fichiers XML: $XML_FILES"
echo ""

# Test de compilation (optionnel, nécessite Android SDK)
echo "5️⃣ Test de compilation (optionnel)..."
echo -e "${YELLOW}⚠️  Ce test nécessite Android SDK installé${NC}"
read -p "Voulez-vous compiler le projet ? (y/n) " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "Compilation en cours..."
    ./gradlew assembleDebug --stacktrace

    if [ $? -eq 0 ]; then
        echo ""
        echo -e "${GREEN}✓ Compilation réussie !${NC}"

        # Vérifier si l'APK a été créé
        APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
        if [ -f "$APK_PATH" ]; then
            APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
            echo -e "${GREEN}📦 APK créé: $APK_PATH ($APK_SIZE)${NC}"
        fi
    else
        echo ""
        echo -e "${RED}❌ Erreur de compilation${NC}"
        echo "Consultez les logs ci-dessus pour plus de détails"
        exit 1
    fi
else
    echo -e "${YELLOW}⏭️  Compilation ignorée${NC}"
fi

echo ""
echo "=================================="
echo -e "${GREEN}✅ Vérifications terminées${NC}"
echo "=================================="
echo ""
echo "📖 Pour tester l'application, consultez TESTING.md"
echo ""
echo "Prochaines étapes:"
echo "  1. Ouvrir le projet dans Android Studio"
echo "  2. Synchroniser Gradle"
echo "  3. Lancer sur un émulateur ou appareil"
echo ""
