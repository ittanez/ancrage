package com.novahypnose.ancrage.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.novahypnose.ancrage.R

/**
 * Écran de paramètres
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val fontSize by viewModel.fontSize.collectAsState()
    val animationIntensity by viewModel.animationIntensity.collectAsState()
    val vibrationEnabled by viewModel.vibrationEnabled.collectAsState()
    val vibrationIntensity by viewModel.vibrationIntensity.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()

    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_settings)) },
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
                .verticalScroll(rememberScrollState())
        ) {
            // Apparence
            SettingsSection(title = stringResource(R.string.settings_appearance)) {
                SettingsDropdown(
                    label = stringResource(R.string.settings_theme),
                    value = themeMode,
                    options = mapOf(
                        "light" to stringResource(R.string.settings_theme_light),
                        "dark" to stringResource(R.string.settings_theme_dark),
                        "auto" to stringResource(R.string.settings_theme_auto)
                    ),
                    onValueChange = { viewModel.setThemeMode(it) }
                )

                SettingsDropdown(
                    label = stringResource(R.string.settings_font_size),
                    value = fontSize,
                    options = mapOf(
                        "small" to "Petit",
                        "medium" to "Moyen",
                        "large" to "Grand"
                    ),
                    onValueChange = { viewModel.setFontSize(it) }
                )

                SettingsDropdown(
                    label = stringResource(R.string.settings_animations),
                    value = animationIntensity,
                    options = mapOf(
                        "low" to "Faible",
                        "medium" to "Moyenne",
                        "high" to "Forte"
                    ),
                    onValueChange = { viewModel.setAnimationIntensity(it) }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Sons et vibrations
            SettingsSection(title = stringResource(R.string.settings_sounds_vibrations)) {
                SettingsSwitch(
                    label = stringResource(R.string.settings_enable_vibrations),
                    checked = vibrationEnabled,
                    onCheckedChange = { viewModel.setVibrationEnabled(it) }
                )

                if (vibrationEnabled) {
                    SettingsDropdown(
                        label = stringResource(R.string.settings_vibration_intensity),
                        value = vibrationIntensity,
                        options = mapOf(
                            "low" to "Faible",
                            "medium" to "Moyenne",
                            "high" to "Forte"
                        ),
                        onValueChange = { viewModel.setVibrationIntensity(it) }
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Données
            SettingsSection(title = stringResource(R.string.settings_data)) {
                SettingsButton(
                    label = stringResource(R.string.settings_export),
                    onClick = { /* TODO: Implémenter l'export */ }
                )

                SettingsButton(
                    label = stringResource(R.string.settings_import),
                    onClick = { /* TODO: Implémenter l'import */ }
                )

                SettingsButton(
                    label = stringResource(R.string.settings_reset),
                    onClick = { showResetDialog = true },
                    isDestructive = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Disclaimer
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = stringResource(R.string.disclaimer),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    // Dialog de confirmation de réinitialisation
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Réinitialiser les paramètres ?") },
            text = { Text("Cette action est irréversible.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetSettings()
                        showResetDialog = false
                    }
                ) {
                    Text("Confirmer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
private fun SettingsSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDropdown(
    label: String,
    value: String,
    options: Map<String, String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = options[value] ?: value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (key, displayValue) ->
                DropdownMenuItem(
                    text = { Text(displayValue) },
                    onClick = {
                        onValueChange(key)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsButton(
    label: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = if (isDestructive) {
            ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        } else {
            ButtonDefaults.outlinedButtonColors()
        }
    ) {
        Text(label)
    }
}
