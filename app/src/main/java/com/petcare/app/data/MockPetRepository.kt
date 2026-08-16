package com.petcare.app.data

import androidx.room.util.copy
import com.petcare.app.R
import com.petcare.app.models.Cat
import com.petcare.app.models.Dog
import com.petcare.app.models.Parrot
import com.petcare.app.models.Pet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MockPetRepository {

    // MutableStateFlow замість простого flow — щоб emit при змінах (refresh, add, remove)
    private val _petsFlow = MutableStateFlow(defaultAnimals())
    val petsFlow: Flow<List<Pet>> = _petsFlow.asStateFlow()

    var pets: MutableList<Pet>
        get() = _petsFlow.value.toMutableList()
        set(value) {
            _petsFlow.value = value
        }

    fun getAllPets(): List<Pet> = _petsFlow.value

    suspend fun addPet(pet: Pet) {
        val updated = _petsFlow.value.toMutableList()
        updated.add(pet)
        _petsFlow.value = updated
    }

    suspend fun removePet(pet: Pet) {
        _petsFlow.value = _petsFlow.value.filter { it.id != pet.id } as MutableList<Pet>
    }

    suspend fun toggleFavorite(petId: Int, favorite: Boolean) {
        _petsFlow.value = _petsFlow.value.map {
            if (it.id == petId) it.apply { isFavorite = favorite } else it
        } as MutableList<Pet>
    }

    suspend fun getPetById(id: Int): Pet? = _petsFlow.value.firstOrNull { it.id == id }

    // Завдання 4 — refresh: перезавантажує список (імітація API-запиту)
    suspend fun refresh() {
        _petsFlow.value = defaultAnimals()
    }
}

private fun defaultAnimals(): MutableList<Pet> = mutableListOf(
    Cat(
        id = 1, name = "Tom", age = 5, weight = 3.5, breed = "Tabby",
        imageResId = R.drawable.cat,
        summary = "Tabby cats are common domestic cats known for their striped coat patterns and playful personality."
    ),
    Dog(
        id = 2, name = "Barsik", age = 3, weight = 12.5, breed = "Jack Russell Terrier",
        isTrained = true, imageResId = R.drawable.dog,
        summary = "Jack Russell Terriers are energetic, intelligent dogs originally bred for hunting."
    ),
    Parrot(
        id = 3, name = "Kesha", age = 1, weight = 0.5, breed = "Blue-and-Yellow Macaw",
        wingSpan = 25.0, imageResId = R.drawable.parrot,
        summary = "Blue-and-yellow macaws are large colorful parrots known for their intelligence."
    ),
    Cat(
        id = 4, name = "Luna", age = 2, weight = 3.1, breed = "British Shorthair",
        imageResId = R.drawable.cat2,
        summary = "British Shorthair cats are calm, friendly pets with dense plush fur."
    ),
    Cat(
        id = 5, name = "Milo", age = 4, weight = 5.2, breed = "Maine Coon",
        imageResId = R.drawable.cat3,
        summary = "Maine Coons are large fluffy cats known for their gentle temperament."
    ),
    Dog(
        id = 6, name = "Rex", age = 6, weight = 28.0, breed = "Golden Retriever",
        isTrained = true, imageResId = R.drawable.dog2,
        summary = "Golden Retrievers are friendly, intelligent dogs often used as family pets."
    ),
    Dog(
        id = 7, name = "Buddy", age = 2, weight = 24.0, breed = "Dalmatian",
        isTrained = false, imageResId = R.drawable.dog3,
        summary = "Dalmatians are energetic dogs famous for their unique black-spotted coat."
    ),
    Parrot(
        id = 8, name = "Rio", age = 3, weight = 0.6, breed = "Amazon Parrot",
        wingSpan = 35.0, imageResId = R.drawable.parrot2,
        summary = "Amazon parrots are intelligent green parrots known for their talking ability."
    ),
    Parrot(
        id = 9, name = "Sunny", age = 2, weight = 0.7, breed = "Scarlet Macaw",
        wingSpan = 40.0, imageResId = R.drawable.parrot3,
        summary = "Scarlet macaws are bright red tropical parrots with impressive wingspans."
    )
)