package com.petcare.app.user_profile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.petcare.app.components.PetButton
import com.petcare.app.components.PetHeader
import com.petcare.app.components.PetTextField
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    name: String,
    onNameChanged: (String) -> Unit
) {
    val viewModel = viewModel<UserProfileViewModel>(
        factory = UserProfileViewModel.Companion.Factory(name, onNameChanged)
    )
    val nameInput by viewModel.nameInput.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        PetHeader(text = "Pet Care", Modifier)
        PetHeader(text = "User profile", Modifier)

        Text(text = "v.0.5", color = MaterialTheme.colorScheme.onBackground)
        Text(text = "Maksym Didychuk", color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(50.dp))

        PetTextField(
            text = nameInput,
            title = "Username",
            keyboardType = KeyboardType.Text,
            onTextChange = { viewModel.updateNameInput(it) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        PetButton(
            title = "Update username",
            color = MaterialTheme.colorScheme.primary,
            disabledColor = MaterialTheme.colorScheme.onSurfaceVariant,
            textColor = MaterialTheme.colorScheme.onPrimary,
            enabled = true,
            onClicked = { viewModel.updateUsername() }
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
private fun UserProfileScreenPreview() {
    com.petcare.app.ui.theme.PetCareTheme {
        UserProfileScreen(name = "UserName", onNameChanged = {})
    }
}