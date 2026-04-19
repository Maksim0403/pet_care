package com.petcare.app.models

class Dog(
    id: Int,
    name: String,
    age: Int,
    weight: Double,
    breed: String?,
    var isTrained: Boolean,
    imageResId: Int? = null,
    summary: String = "",
    isFavorite: Boolean = false
) : Pet(id, name, PetType.DOG, age, weight, breed, imageResId, summary, isFavorite) {

    override fun makeSound(): String {
        return "Woof-woof"
    }
}