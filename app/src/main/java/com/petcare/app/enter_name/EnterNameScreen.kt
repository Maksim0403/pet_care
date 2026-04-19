package com.petcare.app.enter_name

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.petcare.app.components.PetButton
import com.petcare.app.components.PetHeader
import com.petcare.app.components.PetTextField
import com.petcare.app.data.SettingsDataStore
import kotlinx.coroutines.launch

/**
 * Enter Name Screen
 * 
 * User enters their name here. When they click "Submit", the name is:
 * 1. Saved to DataStore (persistent storage)
 * 2. Passed back to OnBoardingScreen
 * 3. Navigation returns to Onboarding
 * 
 * Next time the app opens, DataStore will have this name,
 * so onboarding will be skipped automatically.
 */
@Composable
internal fun EnterNameScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    settingsDataStore: SettingsDataStore
) {
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        var nameInput by remember { mutableStateOf("") }

        Spacer(Modifier.height(16.dp))
        PetHeader(text = "Enter your name", Modifier)

        Spacer(modifier = Modifier.height(50.dp))

        PetTextField(
            text = nameInput,
            title = "Username",
            keyboardType = KeyboardType.Text,
            onTextChange = { nameInput = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        PetButton(
            title = "Submit",
            color = MaterialTheme.colorScheme.primary,
            disabledColor = MaterialTheme.colorScheme.onSurfaceVariant,
            textColor = MaterialTheme.colorScheme.onPrimary,
            enabled = nameInput.length > 3,
            onClicked = {
                // Save name to DataStore (persistent)
                coroutineScope.launch {
                    settingsDataStore.saveUserName(nameInput)
                }
                
                // Also pass it back via NavBackStackEntry for immediate use
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("name", nameInput)
                    
                navController.popBackStack()
            },
        )
    }
}


@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
private fun EnterNameScreenPreview() {
    com.petcare.app.ui.theme.PetCareTheme {
        EnterNameScreen(
            navController = NavController(LocalContext.current),
            settingsDataStore = SettingsDataStore(LocalContext.current)
        )
    }
}