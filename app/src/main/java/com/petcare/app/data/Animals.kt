package com.petcare.app.data

import com.petcare.app.R
import com.petcare.app.models.Cat
import com.petcare.app.models.Dog
import com.petcare.app.models.Parrot

internal val animals = listOf(
    Cat(
        id = 1,
        name = "Tom",
        age = 5,
        weight = 3.5,
        breed = "British",
        imageResId = R.drawable.cat,
        summary = "A cat is a small domesticated mammal known for its independence, agility, and playful behavior. Cats are popular pets and are valued for their companionship and ability to hunt small pests."
    ),
    Dog(
        id = 2,
        name = "Barsik",
        age = 3,
        weight = 12.5,
        breed = "Shepherd",
        isTrained = true,
        imageResId = R.drawable.dog,
        summary = "A dog is a loyal and social domesticated animal often referred to as “man’s best friend.” Dogs are known for their intelligence, trainability, and strong bond with humans."
    ),
    Parrot(
        id = 3,
        name = "Kesha",
        age = 1,
        weight = 0.5,
        breed = "Super",
        wingSpan = 25.0,
        imageResId = R.drawable.parrot,
        summary = "A parrot is a colorful bird famous for its ability to mimic sounds and human speech. Parrots are intelligent, social animals that require attention and mental stimulation."
    )
)