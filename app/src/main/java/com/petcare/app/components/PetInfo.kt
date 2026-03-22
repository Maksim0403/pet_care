package com.petcare.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.R
import com.petcare.app.models.Cat
import com.petcare.app.models.Pet
import com.petcare.app.ui.theme.Typography


@Composable
fun PetInfo(modifier: Modifier = Modifier, pet: Pet) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(color = Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(
            modifier = Modifier
                .width(32.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Gray)
        )

        Spacer(Modifier.height(16.dp))
        PetTitle(pet.name, Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PetInfoItem(modifier = Modifier.weight(1f), title = "Age", text = "${pet.age} years")
            PetInfoItem(modifier = Modifier.weight(1f), title = "Weight", text = "${pet.weight} kg")
            PetInfoItem(modifier = Modifier.weight(1f), title = "Breed", text = "${pet.breed}")
        }

        Spacer(Modifier.height(16.dp))
        PetTitle(text = "Summary", Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Text(text = pet.summary, style = Typography.bodyMedium, color = Color.DarkGray)
        Spacer(Modifier.height(16.dp))

        PetButton(
            title = "Contact",
            onCloseClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PetInfoPreview() {
    PetInfo(
        pet = Cat(
            id = 1,
            name = "Tom",
            age = 5,
            weight = 3.5,
            breed = "Tabby",
            imageResId = R.drawable.cat,
            summary = "Tabby cats are common domestic cats known for their striped coat patterns and playful personality."
        ),
    )
}