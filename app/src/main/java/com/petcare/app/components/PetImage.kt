package com.petcare.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.R
import com.petcare.app.ui.theme.ColorPrimary
import com.petcare.app.ui.theme.ColorPrimaryLight

@Composable
fun PetImage(modifier: Modifier = Modifier, imageResId: Int, size: Int = 350) {

    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size((size * 0.98f).dp)
                .background(
                    color = ColorPrimaryLight,
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size((size * 0.65f).dp)
                .background(
                    color = ColorPrimary,
                    shape = CircleShape
                )
        )
        val imageSize = size.dp
        //val painter = painterResource(imageResId)

        val painter = if (imageResId != 0) {
            painterResource(id = imageResId)
        } else {
            painterResource(R.drawable.cat_default)
        }

        Image(
            painter = painter,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PetImagePreview() {
    PetImage(imageResId = R.drawable.cat)
}

@Preview(showBackground = true)
@Composable
private fun PetImage150Preview() {
    PetImage(modifier = Modifier, imageResId = R.drawable.cat, size = 150)
}
