package com.petcare.app.pet_api

import com.google.gson.annotations.SerializedName
import com.petcare.app.models.Cat
import com.petcare.app.models.Dog
import com.petcare.app.models.Parrot
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType

data class MockPetResponse(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("age")
    val age: Int = 1,

    @SerializedName("weight")
    val weight: Double = 0.0,

    @SerializedName("breed")
    val breed: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("isFavorite")
    val isFavorite: Boolean = false,

    @SerializedName("imageUrl")
    val imageUrl: String? = null,

    @SerializedName("isTrained")
    val isTrained: Boolean? = null,

    @SerializedName("wingSpan")
    val wingSpan: Double? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null
) {

    fun toPet(): Pet {
        val petType = when (type?.lowercase()) {  // ← safe call
            "dog" -> PetType.DOG
            "cat" -> PetType.CAT
            "bird", "parrot" -> PetType.BIRD
            else -> PetType.CAT  // ← safe default instead of ALL
        }

        return when (petType) {
            PetType.DOG -> Dog(
                id = id?.toInt() ?: 0,
                name = name?: "Unknown",
                age = age,
                weight = weight,
                breed = breed ?: "Unknown",
                imageResId = null,
                summary = description ?: "Pet from MockAPI",
                isTrained = isTrained ?: false,
                isFavorite = isFavorite
            )
            PetType.CAT -> Cat(
                id =id?.toInt() ?: 0,
                name = name?: "Unknown",
                age = age,
                weight = weight,
                breed = breed ?: "Unknown",
                imageResId = null,
                summary = description ?: "Pet from MockAPI",
                isFavorite = isFavorite
            )
            PetType.BIRD -> Parrot(
                id =id?.toInt() ?: 0,
                name = name?: "Unknown",
                age = age,
                weight = weight,
                breed = breed ?: "Unknown",
                wingSpan = wingSpan ?: 0.0,
                imageResId = null,
                summary = description ?: "Pet from MockAPI",
                isFavorite = isFavorite
            )
            else -> Cat(
                id = id?.toInt() ?: 0,
                name = name?: "Unknown",
                age = age,
                weight = weight,
                breed = breed ?: "Unknown",
                imageResId = null,
                summary = description ?: "Pet from MockAPI",
                isFavorite = isFavorite
            )
        }
    }

    companion object {
        fun fromPet(pet: Pet): MockPetRequest {
            val type = when (pet.type) {
                PetType.DOG -> "dog"
                PetType.CAT -> "cat"
                PetType.BIRD -> "bird"
                else -> "cat"
            }

            return MockPetRequest(
                name = pet.name,
                type = type,
                age = pet.age,
                weight = pet.weight,
                breed = pet.breed,
                description = pet.summary,
                isFavorite = pet.isFavorite,
                isTrained = (pet as? Dog)?.isTrained,
                wingSpan = (pet as? Parrot)?.wingSpan
            )
        }
    }
}

/**
 * MockAPI Pet Request Model for POST/PUT operations
 */
data class MockPetRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: String, // "dog", "cat", "bird"

    @SerializedName("age")
    val age: Int = 1,

    @SerializedName("weight")
    val weight: Double = 0.0,

    @SerializedName("breed")
    val breed: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("isFavorite")
    val isFavorite: Boolean = false,

    @SerializedName("isTrained")
    val isTrained: Boolean? = null,

    @SerializedName("wingSpan")
    val wingSpan: Double? = null
)

