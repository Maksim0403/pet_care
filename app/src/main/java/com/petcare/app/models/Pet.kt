package com.petcare.app.models

import com.petcare.app.models.PetType

open class Pet(var id: Int, var name: String, var type: PetType, var age: Int, var weight: Double = 0.0, var breed: String? = null) {
    open fun makeSound(): String {
        return "Animal sounds"
    }

    fun getDetails(): String {
        return "ID: $id, Name: $name, Type: $type, Age: $age, Weight: $weight, Breed: ${breed ?: "Unknown"}"
    }

    fun updateWeight(newWeight: Double) {
        this.weight = newWeight
    }

    companion object {
        fun getAppInfo(): String {
            return "PetCare System v1.0 | Laboratory Work #1"
        }
    }
}