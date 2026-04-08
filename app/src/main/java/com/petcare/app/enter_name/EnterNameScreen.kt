package com.petcare.app.enter_name

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.petcare.app.ui.theme.ButtonColor
import com.petcare.app.ui.theme.ButtonDisabledColor
import com.petcare.app.ui.theme.ColorBackground
import com.petcare.app.ui.theme.ColorPrimaryLight

@Composable
internal fun EnterNameScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ColorBackground)
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
            onTextChange = { nameInput = it })

        Spacer(modifier = Modifier.height(32.dp))

        PetButton(
            title = "Submit",
            color = ButtonColor,
            disabledColor = ButtonDisabledColor,
            textColor = ColorPrimaryLight,
            enabled = nameInput.length > 3,
            onClicked = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("name", nameInput)
                navController.popBackStack()
            },
        )
    }
}


@Preview
@Composable
private fun EnterNameScreenPreview() {
    EnterNameScreen(navController = NavController(LocalContext.current))
}