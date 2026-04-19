package com.petcare.app.database

import com.petcare.app.models.Cat
import com.petcare.app.models.Dog
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType
import com.petcare.app.models.Parrot

object PetMapper {

    fun toPet(entity: PetEntity): Pet {
        return when (entity.type) {
            PetType.CAT -> Cat(
                id = entity.id,
                name = entity.name,
                age = entity.age,
                weight = entity.weight,
                breed = entity.breed,
                imageResId = entity.imageResId,
                summary = entity.summary,
                isFavorite = entity.isFavorite
            )
            PetType.DOG -> Dog(
                id = entity.id,
                name = entity.name,
                age = entity.age,
                weight = entity.weight,
                breed = entity.breed,
                isTrained = entity.isTrained ?: false,
                imageResId = entity.imageResId,
                summary = entity.summary,
                isFavorite = entity.isFavorite
            )
            PetType.BIRD -> Parrot(
                id = entity.id,
                name = entity.name,
                age = entity.age,
                weight = entity.weight,
                breed = entity.breed,
                wingSpan = entity.wingSpan ?: 0.0,
                imageResId = entity.imageResId,
                summary = entity.summary,
                isFavorite = entity.isFavorite
            )
            else -> throw IllegalArgumentException("Unknown pet type: ${entity.type}")
        }
    }

    fun toEntity(pet: Pet): PetEntity {
        return when (pet) {
            is Cat -> PetEntity(
                id = pet.id,
                name = pet.name,
                type = pet.type,
                age = pet.age,
                weight = pet.weight,
                breed = pet.breed,
                imageResId = pet.imageResId,
                summary = pet.summary,
                isFavorite = pet.isFavorite
            )
            is Dog -> PetEntity(
                id = pet.id,
                name = pet.name,
                type = pet.type,
                age = pet.age,
                weight = pet.weight,
                breed = pet.breed,
                imageResId = pet.imageResId,
                summary = pet.summary,
                isTrained = pet.isTrained,
                isFavorite = pet.isFavorite
            )
            is Parrot -> PetEntity(
                id = pet.id,
                name = pet.name,
                type = pet.type,
                age = pet.age,
                weight = pet.weight,
                breed = pet.breed,
                imageResId = pet.imageResId,
                summary = pet.summary,
                wingSpan = pet.wingSpan,
                isFavorite = pet.isFavorite
            )
            else -> throw IllegalArgumentException("Unknown pet type: ${pet::class.simpleName}")
        }
    }
}


