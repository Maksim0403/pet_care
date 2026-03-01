package com.petcare.app.repository

import com.petcare.app.models.Pet
import com.petcare.app.models.Reminder

object PetManager {
    private val petList = mutableListOf<Pet>()

    private val reminderList = mutableListOf<Reminder>()

    fun addPet(pet: Pet) {
        petList.add(pet)
    }

    fun addReminder(reminder: Reminder) {
        reminderList.add(reminder)
    }

    fun filterPets(condition: (Pet) -> Boolean): List<Pet> {
        return petList.filter(condition)
    }
}