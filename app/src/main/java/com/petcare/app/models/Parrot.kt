package com.petcare.app.models

class Parrot(
    id: Int,
    name: String,
    age: Int,
    weight: Double,
    breed: String?,
    var wingSpan: Double,
    imageResId: Int? = null,
    summary: String = ""
) : Pet(id, name, PetType.BIRD, age, weight, breed, imageResId, summary) {

    override fun makeSound(): String {
        return "Squawk-squawk"
    }
}