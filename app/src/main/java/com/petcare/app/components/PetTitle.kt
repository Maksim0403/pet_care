package com.petcare.app.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.petcare.app.ui.theme.Typography

@Composable
internal fun PetTitle(text: String, modifier: Modifier,textAlign:TextAlign = TextAlign.Start,
) {
    Text(
        text = text,
        style = Typography.titleLarge,
        color = Color.Black,
        fontWeight = FontWeight(500),
        textAlign = textAlign,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
private fun PetTitlePreview() {
    PetTitle("Title", Modifier)
}