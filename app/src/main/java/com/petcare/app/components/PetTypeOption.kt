package com.petcare.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.ui.theme.ButtonColor
import com.petcare.app.ui.theme.ColorBackground
import com.petcare.app.ui.theme.ColorPrimary

@Composable
fun PetTypeOption(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val color = if (isSelected) ColorPrimary else ColorBackground
    Box(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(10.dp))
            .border(1.dp, ButtonColor, shape = RoundedCornerShape(10.dp))
            .clickable { onClick.invoke() }
    ) {
        Text(text = text, modifier = Modifier.padding(10.dp), color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
private fun PetTypeOptionPreview() {
    PetTypeOption(text = "text", isSelected = false, onClick = {})
}

@Preview(showBackground = true)
@Composable
private fun PetTypeOptionPreviewSelected() {
    PetTypeOption(text = "text", isSelected = true, onClick = {})
}