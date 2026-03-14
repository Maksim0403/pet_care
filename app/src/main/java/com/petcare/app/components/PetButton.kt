package com.petcare.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.ui.theme.ButtonColor
import com.petcare.app.ui.theme.Typography

@Composable
internal fun PetButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = ButtonColor, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Contact",
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            textAlign = TextAlign.Center,
            style = Typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PetButtonPreview() {
    PetButton()
}
