package com.petcare.app.components

import androidx.compose.foundation.layout.fillMaxWidth
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
import com.petcare.app.ui.theme.ButtonDisabledColor

@Composable
internal fun PetButton(modifier: Modifier = Modifier, title: String, color: Color = ButtonColor,disabledColor: Color = ButtonDisabledColor, textColor: Color = Color.White, enabled: Boolean = true, onClicked: () -> Unit) {
    Button(
        enabled = enabled,
        onClick = { onClicked() },
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            disabledContainerColor=disabledColor
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = textColor
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun PetButtonPreview() {
//    PetButton("Title", onClicked = {})
//}
