package com.petcare.app.pet_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.components.PetGridItem
import com.petcare.app.components.PetHeader
import com.petcare.app.components.PetListItem
import com.petcare.app.components.PetTitle
import com.petcare.app.components.PetTypeOption
import com.petcare.app.data.animals
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType
import com.petcare.app.ui.theme.ColorBackground

@Composable
fun PetListScreen(modifier: Modifier = Modifier, animalList: List<Pet>) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ColorBackground)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        var isColumn by remember { mutableStateOf(true) }

        val options by remember (animalList){
            mutableStateOf(
                listOf(PetType.ALL) + animalList.map { it.type }.toSet()
            )
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

        Spacer(Modifier.height(10.dp))
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
            LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
                items(animals.size) { i ->
                    PetListItem(animals[i])
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 columns
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(animals.size) { i ->
                    PetGridItem(animals[i])
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PetListScreenPreview() {
    PetListScreen(animalList = animals)
}


