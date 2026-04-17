package com.petcare.app.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.petcare.app.R
import com.petcare.app.components.PetButton
import com.petcare.app.components.PetHeader
import com.petcare.app.components.PetImage
import com.petcare.app.navigation.Screen


@Composable
internal fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onNameChanged: (String) -> Unit,
) {

    var name by remember { mutableStateOf("") }

    val currentEntry = navController.currentBackStackEntry

    LaunchedEffect(Unit) {
        currentEntry?.savedStateHandle
            ?.getStateFlow("name", "")
            ?.collect { result ->
                if (result.isNotEmpty()) {
                    name = result
                    onNameChanged(name)
                }
            }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PetImage(
                modifier = Modifier.padding(horizontal = 10.dp),
                R.drawable.dog_default,
                size = 120
            )
            Spacer(Modifier.height(20.dp))
            PetHeader(
                "Pet Care",
                modifier = Modifier
            )
            PetButton(
                title = "Enter name",
                color = MaterialTheme.colorScheme.surface,
                textColor = MaterialTheme.colorScheme.primary,
                onClicked = { navController.navigate(Screen.EnterName.route) }
            )
            PetButton(
                title = if (name.isEmpty()) "Continue" else "Continue as $name",
                color = MaterialTheme.colorScheme.surface,
                disabledColor = MaterialTheme.colorScheme.primaryContainer,
                textColor = MaterialTheme.colorScheme.primary,
                enabled = name.isNotEmpty(),
                onClicked = {
                    navController.navigate(Screen.PetList.route) {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
private fun OnBoardingScreenPreview() {
    com.petcare.app.ui.theme.PetCareTheme {
        OnBoardingScreen(navController = NavController(LocalContext.current), onNameChanged = {})
    }
}