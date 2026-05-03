package com.petcare.app.pet_api.add_pet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.petcare.app.components.BackButton
import com.petcare.app.models.PetType
@Composable
fun ApiPetAddScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onPetAdded: (() -> Unit)? = null
) {
    val viewModel = viewModel<ApiPetAddViewModel>()
    val petName by viewModel.petName.collectAsStateWithLifecycle()
    val petAge by viewModel.petAge.collectAsStateWithLifecycle()
    val petWeight by viewModel.petWeight.collectAsStateWithLifecycle()
    val petBreed by viewModel.petBreed.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(modifier = Modifier)
            Text(
                text = "Add Animal",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = "✓ Connected to MockAPI Pet Service. Your data will be persisted in the API.",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        when (state) {
            is ApiPetAddState.Success -> {
                SuccessState(onBackClicked, onPetAdded)
            }
            is ApiPetAddState.Error -> {
                ErrorMessage((state as ApiPetAddState.Error).message)
            }
            is ApiPetAddState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            ApiPetAddState.Idle -> {
                FormContent(
                    petName = petName,
                    onNameChange = { viewModel.updateName(it) },
                    petAge = petAge,
                    onAgeChange = { viewModel.updateAge(it) },
                    petWeight = petWeight,
                    onWeightChange = { viewModel.updateWeight(it) },
                    petBreed = petBreed,
                    onBreedChange = { viewModel.updateBreed(it) },
                    selectedType = selectedType,
                    onTypeChange = { viewModel.updateType(it) },
                    onSubmit = { viewModel.submitForm() },
                    isSubmitting = state == ApiPetAddState.Loading
                )
            }
        }
    }
}

@Composable
private fun FormContent(
    petName: String,
    onNameChange: (String) -> Unit,
    petAge: String,
    onAgeChange: (String) -> Unit,
    petWeight: String,
    onWeightChange: (String) -> Unit,
    petBreed: String,
    onBreedChange: (String) -> Unit,
    selectedType: PetType,
    onTypeChange: (PetType) -> Unit,
    onSubmit: () -> Unit,
    isSubmitting: Boolean
) {
    var typeMenuExpanded by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = petName,
        onValueChange = onNameChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Animal Name *") },
        singleLine = true,
        enabled = !isSubmitting
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedType.name,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Type *") },
            readOnly = true,
            enabled = !isSubmitting
        )
        DropdownMenu(
            expanded = typeMenuExpanded && !isSubmitting,
            onDismissRequest = { typeMenuExpanded = false }
        ) {
            PetType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        onTypeChange(type)
                        typeMenuExpanded = false
                    }
                )
            }
        }
    }

    OutlinedTextField(
        value = petAge,
        onValueChange = onAgeChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Age (years) *") },
        singleLine = true,
        enabled = !isSubmitting
    )

    OutlinedTextField(
        value = petWeight,
        onValueChange = onWeightChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Weight (kg) *") },
        singleLine = true,
        enabled = !isSubmitting
    )

    OutlinedTextField(
        value = petBreed,
        onValueChange = onBreedChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Breed") },
        singleLine = true,
        enabled = !isSubmitting
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onSubmit,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = !isSubmitting
    ) {
        if (isSubmitting) {
            CircularProgressIndicator(modifier = Modifier.height(20.dp))
        } else {
            Text("Save")
        }
    }

    Text(
        text = "* Required fields",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun SuccessState(onBackClicked: () -> Unit, onPetAdded: (() -> Unit)?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "✓ Animal created successfully!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            onPetAdded?.invoke()
            onBackClicked()
        }) {
            Text("Back to List")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ApiPetAddScreenPreview() {
    MaterialTheme {
        ApiPetAddScreen(onBackClicked = {})
    }
}

