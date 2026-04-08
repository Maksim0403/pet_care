package com.petcare.app.pet_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.components.BackButton
import com.petcare.app.components.PetImage
import com.petcare.app.components.PetInfo
import com.petcare.app.data.PetRepository
import com.petcare.app.models.Pet
import com.petcare.app.ui.theme.ColorBackground

@Composable
fun PetProfileScreen(modifier: Modifier = Modifier, pet: Pet, onBackClicked: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ColorBackground),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackButton(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp, start = 16.dp)
                .clickable { onBackClicked() })

        PetImage(modifier = Modifier, imageResId = pet.imageResId ?: 0)

        PetInfo(pet = pet)
    }
}

@Preview(showBackground = true)
@Composable
private fun PetProfileScreenPreview() {
    PetProfileScreen(pet = PetRepository.getAllPets()[0], onBackClicked = {})
}