package com.petcare.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.R
import com.petcare.app.models.Cat
import com.petcare.app.models.Pet

@Composable
internal fun PetListItem(animal: Pet, onRemoveClicked: () -> Unit, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable{onClick()}) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceBright)
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PetImage(
                modifier = Modifier.padding(horizontal = 10.dp),
                animal.imageResId ?: 0,
                size = 120
            )
            Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PetTitle(
                        animal.name,
                        modifier = Modifier
                    )
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    PetInfoItem(
                        modifier = Modifier.weight(1f),
                        title = "Age",
                        text = "${animal.age} years",
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer
                    )
                    PetInfoItem(
                        modifier = Modifier.weight(1f),
                        title = "Weight",
                        text = "${animal.weight} kg",
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer
                    )
                    PetInfoItem(
                        modifier = Modifier.weight(1f),
                        title = "Breed",
                        text = "${animal.breed}",
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }

            }


        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.height(2.dp))
    }

}

@Preview
@Composable
private fun PetListItemPreview() {
    PetListItem(
        animal = Cat(
            id = 1,
            name = "Tom",
            age = 5,
            weight = 3.5,
            breed = "Tabby",
            imageResId = R.drawable.cat,
            summary = "Tabby cats are common domestic cats known for their striped coat patterns and playful personality."
        ), {},{}
    )
}