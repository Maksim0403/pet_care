package com.petcare.app.user_profile

import android.R.attr.textColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.components.PetButton
import com.petcare.app.components.PetHeader
import com.petcare.app.components.PetTextField
import com.petcare.app.ui.theme.ButtonColor
import com.petcare.app.ui.theme.ButtonDisabledColor
import com.petcare.app.ui.theme.ColorBackground
import com.petcare.app.ui.theme.ColorPrimaryLight

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    name: String,
    onNameChanged: (String) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ColorBackground)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        var nameInput by remember { mutableStateOf(name) }

        Spacer(Modifier.height(16.dp))
        PetHeader(text = "Pet Care", Modifier)
        PetHeader(text = "User profile", Modifier)

        Text(text = "v.0.5", color = Color.Black)
        Text(text = "Maksym Didychuk", color = Color.Black)

        Spacer(modifier = Modifier.height(50.dp))

        PetTextField(
            text = nameInput,
            title = "Username",
            keyboardType = KeyboardType.Text,
            onTextChange = { nameInput = it })

        Spacer(modifier = Modifier.height(32.dp))

        PetButton(
            title = "Update username",
            color = ButtonColor,
            disabledColor = ButtonDisabledColor,
            textColor = ColorPrimaryLight,
            enabled = true,
            onClicked = {
                onNameChanged(nameInput)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserProfileScreenPreview() {
    UserProfileScreen(name = "UserName", onNameChanged = {})
}