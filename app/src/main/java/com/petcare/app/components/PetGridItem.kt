package com.petcare.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
internal fun PetGridItem(animal: Pet, onRemoveClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val painter = painterResource(R.drawable.ic_remove)

        Image(
            modifier = Modifier
                .size(36.dp)
                .padding(end = 5.dp)
                .clickable { onRemoveClicked() }
                .align(Alignment.End),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )

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
                    backgroundColor = ColorPrimaryLight
                )
                PetInfoItem(
                    modifier = Modifier,
                    title = "Weight",
                    text = "${animal.weight} kg",
                    backgroundColor = ColorPrimaryLight
                )
                PetInfoItem(
                    modifier = Modifier,
                    title = "Breed",
                    text = "${animal.breed}",
                    backgroundColor = ColorPrimaryLight
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
        ), onRemoveClicked = {}
    )
}
