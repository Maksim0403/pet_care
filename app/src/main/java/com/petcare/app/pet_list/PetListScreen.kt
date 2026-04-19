package com.petcare.app.pet_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.petcare.app.R
import com.petcare.app.components.PetGridItem
import com.petcare.app.components.PetHeader
import com.petcare.app.components.PetListItem
import com.petcare.app.components.PetTitle
import com.petcare.app.components.PetTypeOption
import com.petcare.app.data.SettingsDataStore
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType
import com.petcare.app.models.SortOrder

@Composable
fun PetListScreen(
    modifier: Modifier = Modifier,
    isColumn: Boolean,
    onPetAddClicked: () -> Unit,
    onPetClicked: (Pet) -> Unit,
    settingsDataStore: SettingsDataStore,
) {
    val viewModel = viewModel<PetListViewModel>(
        factory = PetListViewModel.Companion.Factory(settingsDataStore)
    )
    val pets by viewModel.filteredPets.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    when {
        isLoading -> Loading()
        pets.isEmpty() -> EmptyPets(addNewPetClicked = { onPetAddClicked() })
        else -> Content(
            modifier = modifier,
            pets = pets,
            isColumn = isColumn,
            addNewPetClicked = {
                onPetAddClicked()
            }, onRemovePetClicked = { pet ->
                viewModel.removePet(pet)
            }, onPetClicked = { pet -> onPetClicked(pet) },
            viewModel = viewModel
        )

    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyPets(modifier: Modifier = Modifier, addNewPetClicked: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PetHeader(text = "There are no pets in the list", Modifier, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        PetTitle(
            text = "Add first Pet", textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .clickable { addNewPetClicked() })
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    pets: List<Pet>,
    isColumn: Boolean,
    addNewPetClicked: () -> Unit,
    onRemovePetClicked: (Pet) -> Unit,
    onPetClicked: (Pet) -> Unit,
    viewModel: PetListViewModel,
) {

    val allPets by viewModel.allPets.collectAsStateWithLifecycle()
    val selectedOption by viewModel.selectedType.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()
    val showOnlyFavorites by viewModel.showOnlyFavorites.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            val options by remember(allPets) {
                derivedStateOf {
                    listOf(PetType.ALL) + allPets.map { it.type }.toSet()
                }
            }

            var filterExpanded by remember { mutableStateOf(false) }

            val petCountText by remember(pets.size) {
                mutableStateOf(if (pets.size == 3) "There are 3 pets in the list" else " Pets in the list: ${pets.size}")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                PetHeader(text = "Animals", Modifier)
            }
            Spacer(Modifier.height(10.dp))
            PetTitle(text = petCountText, modifier = Modifier)

            Spacer(Modifier.height(10.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(options.size) { i ->
                    PetTypeOption(
                        modifier = Modifier,
                        text = options[i].name,
                        isSelected = selectedOption == options[i],
                        onClick = {
                            viewModel.setSelectedType(options[i])
                        }
                    )
                }
            }
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = { filterExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Sort: ${sortOrder.name}")
                    }
                    DropdownMenu(
                        expanded = filterExpanded,
                        onDismissRequest = { filterExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("None") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.NONE); filterExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("By Name") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.NAME); filterExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("By Age") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.AGE); filterExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("By Weight") },
                            onClick = {
                                viewModel.setSortOrder(SortOrder.WEIGHT); filterExpanded = false
                            }
                        )
                    }
                }

                Button(
                    onClick = { viewModel.toggleShowOnlyFavorites() },
                    modifier = Modifier.weight(1f),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (showOnlyFavorites)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(if (showOnlyFavorites) "★ Favorites" else "☆ All")
                }
            }

            Spacer(Modifier.height(10.dp))
            if (isColumn) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberLazyListState()
                ) {
                    items(
                        items = pets,
                        key = { it.id }
                    ) { pet ->
                        PetListItem(
                            animal = pet,
                            onRemoveClicked = { onRemovePetClicked(pet) },
                            onClick = { onPetClicked(pet) },
                            onFavoriteClicked = { viewModel.toggleFavorite(pet.id, pet.isFavorite) }
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // 2 columns
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    state = rememberLazyGridState()
                ) {
                    items(
                        items = pets,
                        key = { it.id }
                    ) { pet ->
                        PetGridItem(
                            animal = pet,
                            onRemoveClicked = { onRemovePetClicked(pet) },
                            onClick = { onPetClicked(pet) },
                            onFavoriteClicked = { viewModel.toggleFavorite(pet.id, pet.isFavorite) }
                        )
                    }
                }
            }
        }
        AddNewPetButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 32.dp, end = 16.dp),
            onClick = {
                addNewPetClicked()
            })

    }
}

@Composable
private fun AddNewPetButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .size(48.dp)
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(24.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,

        ) {
        val painter = painterResource(R.drawable.ic_add)

        Image(
            modifier = Modifier
                .size(24.dp)
                .padding(5.dp),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}
/*
@Preview(showBackground = true)
@Composable
private fun PetListScreenPreview() {
    Content(
        pets = PetRepository.getAllPets(),
        addNewPetClicked = {},
        isColumn = true,
        onRemovePetClicked = {},
        onPetClicked = {})
}*/
