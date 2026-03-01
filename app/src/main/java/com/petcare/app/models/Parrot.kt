package com.petcare.app.models

import com.petcare.app.models.PetType

class Parrot(id: Int, name: String, age: Int, weight: Double, breed: String?, var wingSpan: Double)
    : Pet(id, name, PetType.BIRD, age, weight, breed) {

    override fun makeSound(): String {
        return "Squawk-squawk"
    }
}