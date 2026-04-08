package com.petcare.app.add_new_pet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.R
import com.petcare.app.data.animals
import com.petcare.app.models.Cat
import com.petcare.app.models.Dog
import com.petcare.app.models.Parrot
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType
import com.petcare.app.models.PetType.BIRD
import com.petcare.app.ui.theme.ButtonColor
import com.petcare.app.ui.theme.ButtonDisabledColor
import com.petcare.app.ui.theme.ColorBackground

@Composable
internal fun AddNewPetScreen(
    modifier: Modifier = Modifier,
    animalsSize: Int = animals.size,
    onAnimalAdded: (Pet) -> Unit,
    onCloseClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ColorBackground)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        _root_ide_package_.com.petcare.app.components.PetHeader(text = "Add new pet", Modifier)
        val petOptions = listOf(
            PetType.CAT,
            PetType.DOG,
            BIRD
        )

        var selectedPet by remember { mutableStateOf(PetType.CAT) }
        var petName by remember { mutableStateOf("") }
        var petAge by remember { mutableStateOf("") }

        val petImage by remember(selectedPet) {
            mutableIntStateOf(
                when (selectedPet) {
                    PetType.DOG -> R.drawable.dog_default
                    PetType.CAT -> R.drawable.cat_default
                    else -> R.drawable.bird_default
                }
            )
        }

        _root_ide_package_.com.petcare.app.components.PetImage(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.CenterHorizontally),
            petImage,
            size = 300
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            petOptions.forEach { pet ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { selectedPet = pet }
                        .padding(8.dp)
                ) {
                    RadioButton(
                        selected = (pet == selectedPet),
                        onClick = { selectedPet = pet },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = ButtonColor,
                            unselectedColor = ButtonDisabledColor
                        )
                    )
                    Text(text = pet.title, modifier = Modifier.padding(10.dp), color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        _root_ide_package_.com.petcare.app.components.PetTextField(
            text = petName,
            title = "Name",
            keyboardType = KeyboardType.Text,
            onTextChange = { petName = it })
        _root_ide_package_.com.petcare.app.components.PetTextField(
            text = petAge,
            title = "Age",
            keyboardType = KeyboardType.Number,
            onTextChange = { petAge = it })

        _root_ide_package_.com.petcare.app.components.PetButton(
            title = "Add Pet",
            color = ButtonColor,
            onClicked = {
                val newPet =
                    createPet(
                        name = petName,
                        age = petAge,
                        imageResId = petImage,
                        petType = selectedPet,
                        id = animalsSize + 1
                    )
                onAnimalAdded(newPet)
            }
        )

        _root_ide_package_.com.petcare.app.components.PetButton(
            title = "Close",
            color = ButtonDisabledColor,
            onClicked = { onCloseClicked() }
        )
    }
}

private fun createPet(name: String, age: String, imageResId: Int, petType: PetType, id: Int): Pet {
    return when (petType) {
        PetType.DOG -> Dog(
            id = id,
            name = name,
            age = age.toInt(),
            weight = 3.0,
            breed = "",
            isTrained = false,
            imageResId = imageResId,
            summary = ""
        )

        PetType.CAT -> Cat(
            id = id,
            name = name,
            age = age.toInt(),
            weight = 3.0,
            breed = "",
            imageResId = imageResId,
            summary = ""
        )

        else -> Parrot(
            id = id,
            name = name,
            age = age.toInt(),
            weight = 3.0,
            breed = "",
            imageResId = imageResId,
            wingSpan = 1.0,
            summary = ""
        )
    }
}

@Preview
@Composable
private fun AddNewPetScreenPreview() {
    AddNewPetScreen(Modifier, 0, {}, {})
}