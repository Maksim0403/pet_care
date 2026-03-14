package com.petcare.app.models

class Cat(
    id: Int,
    name: String,
    age: Int,
    weight: Double,
    breed: String?,
    imageResId: Int? = null,
    summary: String = ""
) :Pet(id, name, PetType.CAT, age, weight, breed, imageResId, summary) {

    override fun makeSound(): String {
        return "Meow-meow"
    }
}