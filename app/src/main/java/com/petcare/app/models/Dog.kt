package com.petcare.app.models

class Dog(
    id: Int,
    name: String,
    age: Int,
    weight: Double,
    breed: String?,
    var isTrained: Boolean,
    imageResId: Int? = null,
    summary: String = ""
) : Pet(id, name, PetType.DOG, age, weight, breed, imageResId, summary) {

    override fun makeSound(): String {
        return "Woof-woof"
    }
}