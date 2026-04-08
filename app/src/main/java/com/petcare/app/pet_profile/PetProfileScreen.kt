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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Composable
fun PetProfileScreen(modifier: Modifier = Modifier, petId: Int, onBackClicked: () -> Unit) {
    val viewModel = viewModel<PetProfileViewModel>(
        factory = PetProfileViewModel.Factory(petId)
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state) {
        is PetDetailState.Loading -> LoadingScreen(onBackClicked)
        is PetDetailState.Success -> {
            val successState = state as PetDetailState.Success
            SuccessScreen(
                modifier = modifier,
                pet = successState.pet,
                relatedPets = successState.relatedPets,
                statistics = successState.statistics,
                onBackClicked = onBackClicked
            )
        }
        is PetDetailState.Error -> {
            val errorState = state as PetDetailState.Error
            ErrorScreen(errorState.message, onBackClicked)
        }
    }
}

@Composable
private fun LoadingScreen(onBackClicked: () -> Unit) {
    Column(
        modifier = Modifier
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

        // Loading indicator
        CircularProgressIndicator()
    }
}

@Composable
private fun SuccessScreen(
    modifier: Modifier = Modifier,
    pet: Pet,
    relatedPets: List<Pet>,
    statistics: String,
    onBackClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ColorBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BackButton(
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { onBackClicked() })

        PetImage(modifier = Modifier, imageResId = pet.imageResId ?: 0)

        PetInfo(pet = pet)

        Text(
            text = "Statistics",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = statistics,
            style = MaterialTheme.typography.bodyMedium
        )

        // Related pets section
        if (relatedPets.isNotEmpty()) {
            Text(
                text = "Related Pets",
                style = MaterialTheme.typography.headlineSmall
            )
            relatedPets.forEach { relatedPet ->
                Text(
                    text = relatedPet.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun ErrorScreen(message: String, onBackClicked: () -> Unit) {
    Column(
        modifier = Modifier
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

        Text(
            text = "Error: $message",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Red
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PetProfileScreenPreview() {
    PetProfileScreen(petId = PetRepository.getAllPets()[0].id, onBackClicked = {})
}