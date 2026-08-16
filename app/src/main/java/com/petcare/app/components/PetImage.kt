package com.petcare.app.components

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.petcare.app.R
import com.petcare.app.ui.theme.ColorPrimary
import com.petcare.app.ui.theme.ColorPrimaryLight


sealed class PetImageSource {
    data class Res(@DrawableRes val resId: Int) : PetImageSource()
    data class CameraUri(val uri: Uri) : PetImageSource()
    data object Default : PetImageSource()
}

@Composable
fun PetImage(
    modifier: Modifier = Modifier,
    source: PetImageSource = PetImageSource.Default,
    size: Int = 350,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size((size * 0.98f).dp)
                .background(color = ColorPrimaryLight, shape = CircleShape)
        )
        when (source) {
            // Uri з камери — використовуємо Coil AsyncImage
            is PetImageSource.CameraUri -> Box(
                modifier = Modifier
                    .size(size.dp)
                    .background(color = ColorPrimary, shape = CircleShape)
            ) {
                AsyncImage(
                    model = source.uri,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            is PetImageSource.Res ->
                Box(
                    modifier = Modifier
                        .size((size * 0.65f).dp)
                        .background(color = ColorPrimary, shape = CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = if (source.resId != 0) source.resId else R.drawable.cat_default),
                        contentDescription = contentDescription,
                        modifier = Modifier
                            .size((size * 0.65f).dp)
                    )
                }

            // Заглушка за замовчуванням

            PetImageSource.Default -> Box(
                modifier = Modifier
                    .size((size * 0.65f).dp)
                    .background(color = ColorPrimary, shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cat_default),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size((size * 0.65f).dp)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun PetImagePreview() {
    PetImage(source = PetImageSource.Res(R.drawable.cat))
}

@Preview(showBackground = true)
@Composable
private fun PetImage150Preview() {
    PetImage(modifier = Modifier, source = PetImageSource.Res(R.drawable.cat), size = 150)
}
