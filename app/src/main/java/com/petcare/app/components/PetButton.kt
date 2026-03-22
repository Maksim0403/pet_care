package com.petcare.app.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.ui.theme.ButtonColor

@Composable
internal fun PetButton(title: String, color: Color = ButtonColor, onCloseClicked: () -> Unit) {
    Button(
        onClick = { onCloseClicked() },
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PetButtonPreview() {
    PetButton("Title", onCloseClicked = {})
}
