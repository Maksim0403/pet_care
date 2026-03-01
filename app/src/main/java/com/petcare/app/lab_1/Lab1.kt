package com.petcare.app.lab_1

import com.petcare.app.models.Dog
import com.petcare.app.models.Parrot
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType
import com.petcare.app.models.Reminder
import com.petcare.app.repository.PetManager
import com.petcare.app.utils.getWeightInGrams
import com.petcare.app.utils.isValidName
import com.petcare.app.utils.setHighPriority

fun runLabDemonstration() {
    // 1. Виклик статичного методу (f)
    println(Pet.getAppInfo())

    // 2. створення об'єктів (a, d)
    val myDog = Dog(1, "Barsik", 3, 12.5, "German Shepherd", true)
    val mysteryPet = Parrot(2, "Kesha", 1, 0.5, null, 25.0) // breed = null для (i)

    // 3. додавання в Singleton (g)
    PetManager.addPet(myDog)
    PetManager.addPet(mysteryPet)

    // 4. перевірка Nullable та Elvis (i)
    println(myDog.getDetails())
    println(mysteryPet.getDetails())

    // 5. використання Extension-функції (j)
    if (myDog.name.isValidName()) {
        println("Weight in grams: ${myDog.getWeightInGrams()}")
    }

    // 6. робота з Reminder та перевантаженням (b, c)
    val foodReminder = Reminder("Feeding") // вторинний конструктор
    foodReminder.postpone(2) // перевантажений метод
    foodReminder.setHighPriority() // Extension для Reminder
    PetManager.addReminder(foodReminder)

    // 7. використання лямбди (k)
    val birds = PetManager.filterPets { it.type == PetType.BIRD }
    println("Found ${birds.size} birds in the system.")
}
