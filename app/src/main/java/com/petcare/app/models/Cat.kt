package com.petcare.app.models

class Cat(
    id: Int,
    name: String,
    age: Int,
    weight: Double,
    breed: String?,
    imageResId: Int? = null,
    summary: String = "",
    isFavorite: Boolean = false
) :Pet(id, name, PetType.CAT, age, weight, breed, imageResId, summary, isFavorite) {

    override fun makeSound(): String {
        return "Meow-meow"
    }
}