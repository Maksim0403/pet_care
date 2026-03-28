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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.R
import com.petcare.app.components.AddNewPetScreen
import com.petcare.app.components.PetGridItem
import com.petcare.app.components.PetHeader
import com.petcare.app.components.PetListItem
import com.petcare.app.components.PetTitle
import com.petcare.app.components.PetTypeOption
import com.petcare.app.data.animals
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType
import com.petcare.app.ui.theme.ButtonColor
import com.petcare.app.ui.theme.ButtonDisabledColor
import com.petcare.app.ui.theme.ColorBackground
import kotlinx.coroutines.delay

@Composable
fun PetListScreen(
    modifier: Modifier = Modifier,
    animalList: MutableList<Pet>,
    onPetClicked: (Pet) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var showAddNewPet by remember { mutableStateOf(false) }
    var pets by remember { mutableStateOf(animalList) }

    LaunchedEffect(Unit) {
        if (isLoading) {
            delay(2500)
            isLoading = false
        }
    }

    when {
        isLoading -> Loading()
        showAddNewPet -> AddNewPetScreen(
            modifier = Modifier,
            animalsSize = pets.size,
            onAnimalAdded = { pet ->
                pets.add(pet)
                showAddNewPet = false
            },
            onCloseClicked = { showAddNewPet = false }
        )

        pets.isEmpty() -> EmptyPets(addNewPetClicked = { showAddNewPet = true })

        else -> Content(
            modifier = modifier,
            animalList = pets,
            addNewPetClicked = {
                showAddNewPet = true
            }, onRemovePetClicked = { pet ->
                pets = pets.toMutableList().also { it.remove(pet) }
            }, onPetClicked = { onPetClicked })

    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = ColorBackground)
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
            .background(color = ColorBackground)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PetHeader(text = "There are no pets in the list", Modifier, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        PetTitle(text = "Add first Pet", textAlign = TextAlign.Center, modifier = Modifier
            .fillMaxWidth()
            .background(color = ButtonDisabledColor, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clickable {addNewPetClicked()})
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    animalList: List<Pet>,
    addNewPetClicked: () -> Unit,
    onRemovePetClicked: (Pet) -> Unit,
    onPetClicked: (Pet) -> Unit,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = ColorBackground)
            .padding(horizontal = 10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            var isColumn by remember { mutableStateOf(true) }

            val options by remember(animalList) {
                derivedStateOf {
                    listOf(PetType.ALL) + animalList.map { it.type }.toSet()
                }
            }

            var selectedOption by remember { mutableStateOf(options[0]) }

            val petsGroupedMap: Map<PetType, List<Pet>> = remember(animalList) {
                animalList.groupBy { it.type }
            }

            val animals = if (selectedOption == PetType.ALL) {
                animalList
            } else {
                petsGroupedMap[selectedOption] ?: emptyList()
            }

            val petCountText by remember(animals.size) {
                mutableStateOf(if (animals.size == 1) "There is only one pet" else " Pets in the list: ${animals.size}")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                val title = if (isColumn) "List" else "Grid"
                PetHeader(text = "Animals", Modifier)
                PetTitle(text = title, modifier = Modifier.clickable {
                    isColumn = !isColumn
                })
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
                            selectedOption = options[i]
                        }
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            if (isColumn) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberLazyListState()
                ) {
                    items(
                        items = animals,
                        key = { it.id }
                    ) { pet ->
                        PetListItem(
                            animal = pet,
                            onRemoveClicked = { onRemovePetClicked(pet) },
                            onClick = { onPetClicked })
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
                        items = animals,
                        key = { it.id }
                    ) { pet ->
                        PetGridItem(animal = pet, onRemoveClicked = { onRemovePetClicked(pet) })
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
            .background(ButtonColor, shape = RoundedCornerShape(24.dp))
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

@Preview(showBackground = true)
@Composable
private fun PetListScreenPreview() {
    Content(
        animalList = animals,
        addNewPetClicked = {},
        onRemovePetClicked = {},
        onPetClicked = {})
}


