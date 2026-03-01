package com.petcare.app.utils

import com.petcare.app.models.Pet
import com.petcare.app.models.Reminder

fun String.isValidName(): Boolean {
    return this.length in 2..20
}

fun Pet.getWeightInGrams(): Double {
    return this.weight * 1000
}

fun Reminder.setHighPriority() {
    this.title = "URGENT: ${this.title}"
}