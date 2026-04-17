package com.petcare.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.ui.theme.AppTypography

@Composable
fun PetInfoItem(modifier: Modifier = Modifier, title: String, text: String, backgroundColor : Color = MaterialTheme.colorScheme.primaryContainer) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
            .background(color = backgroundColor)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, style = AppTypography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = text,
            style = AppTypography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight(500),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PetInfoItemPreview() {
    PetInfoItem(title = "title", text = "text")
}