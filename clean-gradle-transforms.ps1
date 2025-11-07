# Script pour supprimer les caches transforms-3 problématiques
# Utilise robocopy pour contourner les limitations de chemins Windows

Write-Host "=== Nettoyage des caches Gradle transforms ===" -ForegroundColor Cyan

# Arrêter tous les processus Gradle/Java
Write-Host "`n1. Arrêt des processus Gradle/Java..." -ForegroundColor Yellow
Get-Process -Name "*gradle*","*java*" -ErrorAction SilentlyContinue | Stop-Process -Force
Start-Sleep -Seconds 2

# Chemin vers le dossier transforms-3
$transformsPath = "$env:USERPROFILE\.gradle\caches\transforms-3"

if (Test-Path $transformsPath) {
    Write-Host "`n2. Suppression du dossier transforms-3..." -ForegroundColor Yellow

    # Créer un dossier vide temporaire
    $emptyDir = "$env:TEMP\empty_gradle_temp"
    if (Test-Path $emptyDir) {
        Remove-Item $emptyDir -Force -Recurse -ErrorAction SilentlyContinue
    }
    New-Item -ItemType Directory -Path $emptyDir -Force | Out-Null

    # Utiliser robocopy pour "vider" le dossier (contourne les limitations de chemins)
    Write-Host "   Utilisation de robocopy pour gérer les chemins longs..." -ForegroundColor Gray
    robocopy $emptyDir $transformsPath /MIR /R:0 /W:0 /NFL /NDL /NJH /NJS | Out-Null

    # Supprimer le dossier maintenant vide
    Start-Sleep -Seconds 1
    Remove-Item $transformsPath -Force -Recurse -ErrorAction SilentlyContinue

    # Nettoyer le dossier temporaire
    Remove-Item $emptyDir -Force -Recurse -ErrorAction SilentlyContinue

    Write-Host "   ✓ Dossier transforms-3 supprimé" -ForegroundColor Green
} else {
    Write-Host "`n2. Le dossier transforms-3 n'existe pas (déjà propre)" -ForegroundColor Green
}

# Supprimer aussi le dossier daemon
$daemonPath = "$env:USERPROFILE\.gradle\daemon"
if (Test-Path $daemonPath) {
    Write-Host "`n3. Suppression du dossier daemon..." -ForegroundColor Yellow
    Remove-Item $daemonPath -Force -Recurse -ErrorAction SilentlyContinue
    Write-Host "   ✓ Dossier daemon supprimé" -ForegroundColor Green
}

Write-Host "`n=== Nettoyage terminé avec succès ===" -ForegroundColor Green
Write-Host "`nProchaines étapes dans Android Studio:" -ForegroundColor Cyan
Write-Host "1. File → Invalidate Caches → Invalidate and Restart" -ForegroundColor White
Write-Host "2. Après redémarrage: Build → Rebuild Project" -ForegroundColor White
Write-Host "`nAppuyez sur une touche pour fermer..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
