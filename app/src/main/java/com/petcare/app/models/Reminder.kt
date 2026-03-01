package com.petcare.app.models

class Reminder(var title: String, var dateTime: String, var isCompleted: Boolean = false) {
    constructor(title: String) : this(title, "Today", false)

    fun postpone() {
        this.dateTime = "Postponed by 1 hour"
        println("Reminder '$title' was postponed.")
    }

    fun postpone(hours: Int) {
        this.dateTime = "Postponed by $hours hours"
        println("Reminder '$title' was postponed by $hours hours.")
    }

    fun getStatus(): String {
        return if (isCompleted) "Done" else "Pending"
    }
}