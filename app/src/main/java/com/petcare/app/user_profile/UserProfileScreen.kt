package com.petcare.app.user_profile

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.petcare.app.R
import com.petcare.app.components.PetButton
import com.petcare.app.components.PetHeader
import com.petcare.app.components.PetImage
import com.petcare.app.components.PetImageSource
import com.petcare.app.components.PetTextField
import com.petcare.app.data.SettingsDataStore

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    name: String,
    imageUri: Uri? = null,
    onNameChanged: (String) -> Unit,
    onUserImageCLicked: (() -> Unit)? = null,
    openLocation: () -> Unit,
    settingsDataStore: SettingsDataStore
) {
    val viewModel = viewModel<UserProfileViewModel>(
        factory = UserProfileViewModel.Companion.Factory(settingsDataStore, onNameChanged)
    )

    // Collect all settings as states
    val nameInput by viewModel.nameInput.collectAsStateWithLifecycle()
    val measurementUnit by viewModel.measurementUnitInput.collectAsStateWithLifecycle()
    val sortMode by viewModel.sortModeInput.collectAsStateWithLifecycle()
    val language by viewModel.languageInput.collectAsStateWithLifecycle()
    val userImage by viewModel.userImage.collectAsStateWithLifecycle()

    // Dropdown states for better UX
    var showMeasurementDropdown by remember { mutableStateOf(false) }
    var showSortModeDropdown by remember { mutableStateOf(false) }
    var showLanguageDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        PetHeader(text = "Pet Care", Modifier)
        PetHeader(text = "Settings", Modifier)

        Text(text = "v.0.6", color = MaterialTheme.colorScheme.onBackground)
        Text(text = "Pet Care Application", color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(16.dp))

        //  USER IMAGE
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            val imageFromSettings = userImage
            val imageSource = when {
                imageUri != null -> PetImageSource.CameraUri(imageUri)
                imageFromSettings != null -> PetImageSource.CameraUri(imageFromSettings)
                else -> PetImageSource.Res(R.drawable.ic_user)
            }

            PetImage(
                modifier = Modifier
                    .clickable {
                        onUserImageCLicked?.invoke()
                    },
                source = imageSource,
                size = 200
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // USERNAME SETTING
        PetTextField(
            text = nameInput,
            title = "Username",
            keyboardType = KeyboardType.Text,
            onTextChange = { viewModel.updateNameInput(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // MEASUREMENT UNIT SETTING
        Text(
            text = "Measurement Unit",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Button(
            onClick = { showMeasurementDropdown = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = measurementUnit,
                color = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = showMeasurementDropdown,
            onDismissRequest = { showMeasurementDropdown = false }
        ) {
            listOf("kg", "lb").forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit) },
                    onClick = {
                        viewModel.updateMeasurementUnit(unit)
                        showMeasurementDropdown = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SORT MODE SETTING
        Text(
            text = "Default Sort Mode",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Button(
            onClick = { showSortModeDropdown = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = sortMode.replaceFirstChar { it.uppercase() },
                color = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = showSortModeDropdown,
            onDismissRequest = { showSortModeDropdown = false }
        ) {
            listOf("name", "age", "weight").forEach { mode ->
                DropdownMenuItem(
                    text = { Text(mode.replaceFirstChar { it.uppercase() }) },
                    onClick = {
                        viewModel.updateSortMode(mode)
                        showSortModeDropdown = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // LANGUAGE SETTING
        Text(
            text = "Language",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Button(
            onClick = { showLanguageDropdown = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            val languageLabel = when (language) {
                "uk" -> "Українська"
                "de" -> "Deutsch"
                else -> "English"
            }
            Text(
                text = languageLabel,
                color = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = showLanguageDropdown,
            onDismissRequest = { showLanguageDropdown = false }
        ) {
            listOf(
                "en" to "English",
                "uk" to "Українська",
                "de" to "Deutsch"
            ).forEach { (code, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        viewModel.updateLanguage(code)
                        showLanguageDropdown = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // ========== SAVE BUTTON ==========
        PetButton(
            title = "Save Settings",
            color = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary,
            enabled = true,
            onClicked = { viewModel.saveAllSettings(imageUri) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        PetButton(
            title = "Open location",
            color = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onPrimary,
            enabled = true,
            onClicked = { openLocation() }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
private fun UserProfileScreenPreview() {

    com.petcare.app.ui.theme.PetCareTheme {
        UserProfileScreen(
            name = "UserName",
            onNameChanged = {},
            openLocation = {},
            settingsDataStore = SettingsDataStore(androidx.compose.ui.platform.LocalContext.current)
        )
    }
}