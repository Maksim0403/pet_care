package com.petcare.app.components

import androidx.benchmark.traceprocessor.Row
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.R
import com.petcare.app.models.Cat
import com.petcare.app.models.Pet
import com.petcare.app.ui.theme.ColorPrimaryLight

@Composable
internal fun PetGridItem(
    animal: Pet,
    onRemoveClicked: () -> Unit,
    onClick: () -> Unit,
    onFavoriteClicked: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceBright, shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = RoundedCornerShape(10.dp))
            .padding(10.dp)
            .clickable{onClick()},
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onFavoriteClicked != null) {
                val favoriteIcon = if (animal.isFavorite)
                    R.drawable.ic_favorite_filled
                else
                    R.drawable.ic_favorite_outline
                val favoritePainter = painterResource(favoriteIcon)

                Image(
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { onFavoriteClicked() },
                    painter = favoritePainter,
                    contentDescription = if (animal.isFavorite) "Remove from favorites" else "Add to favorites",
                    contentScale = ContentScale.Fit,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            val painter = painterResource(R.drawable.ic_remove)

            Image(
                modifier = Modifier
                    .size(36.dp)
                    .padding(end = 5.dp)
                    .clickable { onRemoveClicked() },
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
            )
        }

        PetImage(
            modifier = Modifier.padding(vertical = 10.dp),
            animal.imageResId ?: 0,
            size = 150
        )

        Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PetTitle(
                animal.name,
                modifier = Modifier
            )

            Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PetInfoItem(
                    modifier = Modifier,
                    title = "Age",
                    text = "${animal.age} years",
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                )
                PetInfoItem(
                    modifier = Modifier,
                    title = "Weight",
                    text = "${animal.weight} kg",
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                )
                PetInfoItem(
                    modifier = Modifier,
                    title = "Breed",
                    text = "${animal.breed}",
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }

    }
}

@Preview
@Composable
private fun PetGridItemPreview() {
    PetGridItem(
        animal = Cat(
            id = 1,
            name = "Tom",
            age = 5,
            weight = 3.5,
            breed = "Tabby",
            imageResId = R.drawable.cat,
            summary = "Tabby cats are common domestic cats known for their striped coat patterns and playful personality."
        ), onRemoveClicked = {}, onClick = {}
    )
}
