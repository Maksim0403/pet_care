package com.petcare.app.models

import com.petcare.app.models.PetType

class Dog(id: Int, name: String, age: Int, weight: Double, breed: String?, var isTrained: Boolean)
    : Pet(id, name, PetType.MAMMAL, age, weight, breed) {

    override fun makeSound(): String {
        return "Woof-woof"
    }
}