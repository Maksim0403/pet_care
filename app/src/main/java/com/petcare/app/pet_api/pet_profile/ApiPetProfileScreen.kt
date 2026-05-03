package com.petcare.app.pet_api.pet_profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.petcare.app.components.BackButton
import com.petcare.app.components.PetImage
import com.petcare.app.models.Pet

@Composable
fun ApiPetProfileScreen(
    modifier: Modifier = Modifier,
    petIdentifier: String,
    onBackClicked: () -> Unit
) {
    val viewModel = viewModel<ApiPetProfileViewModel>(
        factory = ApiPetProfileViewModel.Factory(petIdentifier)
    )

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state) {
        is ApiPetDetailState.Loading -> ApiLoadingScreen(onBackClicked)
        is ApiPetDetailState.Success -> {
            val successState = state as ApiPetDetailState.Success
            ApiSuccessScreen(
                modifier = modifier,
                pet = successState.pet,
                statistics = successState.statistics,
                onBackClicked = onBackClicked
            )
        }
        is ApiPetDetailState.Error -> {
            val errorState = state as ApiPetDetailState.Error
            ApiErrorScreen(
                errorMessage = errorState.message,
                onBackClicked = onBackClicked,
                onRetry = { viewModel.retry() }
            )
        }
    }
}

@Composable
private fun ApiLoadingScreen(onBackClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BackButton(modifier = Modifier)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator()
                Text("Loading animal details from API...")
            }
        }
    }
}

@Composable
private fun ApiSuccessScreen(
    modifier: Modifier = Modifier,
    pet: Pet,
    statistics: String,
    onBackClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BackButton(modifier = Modifier.clickable{
            onBackClicked()
        })

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PetImage(
                modifier = Modifier.size(200.dp),
                imageResId = pet.imageResId ?: 0
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Type: ${pet.type}",
                    style = MaterialTheme.typography.bodyLarge
                )

                pet.breed?.let { breed ->
                    Text(
                        text = "Breed: $breed",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Physical Characteristics",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoItem("Age", "${pet.age} years")
                    InfoItem("Weight", "${pet.weight} kg")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = statistics,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = "Data provided by API Ninjas Animals API",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ApiErrorScreen(
    errorMessage: String,
    onBackClicked: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        BackButton(modifier = Modifier)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Error: $errorMessage",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error
                )
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ApiPetProfileScreenPreview() {
    MaterialTheme {
        ApiPetProfileScreen(
            petIdentifier = "Lion",
            onBackClicked = {}
        )
    }
}
