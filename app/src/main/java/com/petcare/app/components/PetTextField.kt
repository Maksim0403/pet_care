package com.petcare.app.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.ui.theme.ButtonColor
import com.petcare.app.ui.theme.ButtonDisabledColor

@Composable
internal fun PetTextField(
    modifier: Modifier = Modifier,
    text: String,
    title: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = { onTextChange(it) },
        label = { Text(title) },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),

        colors = TextFieldDefaults.colors(
            focusedTextColor = ButtonColor,
            unfocusedTextColor = ButtonDisabledColor,

            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,

            focusedIndicatorColor = ButtonColor,
            unfocusedIndicatorColor = ButtonDisabledColor,

            cursorColor = ButtonColor,
            focusedLabelColor = ButtonColor,
            unfocusedLabelColor = ButtonDisabledColor
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}

//@Preview(showBackground = true)
//@Composable
//private fun PetTextFieldPreview() {
//    PetTextField("text", "title", keyboardType = KeyboardType.Text, {})
//}