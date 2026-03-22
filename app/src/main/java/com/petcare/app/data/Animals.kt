package com.petcare.app.data

import com.petcare.app.R
import com.petcare.app.models.Cat
import com.petcare.app.models.Dog
import com.petcare.app.models.Parrot
import com.petcare.app.models.Pet

internal var animals = mutableListOf<Pet>(
    Cat(
        id = 1,
        name = "Tom",
        age = 5,
        weight = 3.5,
        breed = "Tabby",
        imageResId = R.drawable.cat,
        summary = "Tabby cats are common domestic cats known for their striped coat patterns and playful personality."
    ),
    Dog(
        id = 2,
        name = "Barsik",
        age = 3,
        weight = 12.5,
        breed = "Jack Russell Terrier",
        isTrained = true,
        imageResId = R.drawable.dog,
        summary = "Jack Russell Terriers are energetic, intelligent dogs originally bred for hunting. They are very active and loyal companions."
    ),
    Parrot(
        id = 3,
        name = "Kesha",
        age = 1,
        weight = 0.5,
        breed = "Blue-and-Yellow Macaw",
        wingSpan = 25.0,
        imageResId = R.drawable.parrot,
        summary = "Blue-and-yellow macaws are large colorful parrots known for their intelligence, loud calls, and ability to mimic sounds."
    ),

    Cat(
        id = 4,
        name = "Luna",
        age = 2,
        weight = 3.1,
        breed = "British Shorthair",
        imageResId = R.drawable.cat2,
        summary = "British Shorthair cats are calm, friendly pets with dense plush fur and a relaxed personality."
    ),
    Cat(
        id = 5,
        name = "Milo",
        age = 4,
        weight = 5.2,
        breed = "Maine Coon",
        imageResId = R.drawable.cat3,
        summary = "Maine Coons are large fluffy cats known for their gentle temperament, long fur, and bushy tails."
    ),

    Dog(
        id = 6,
        name = "Rex",
        age = 6,
        weight = 28.0,
        breed = "Golden Retriever",
        isTrained = true,
        imageResId = R.drawable.dog2,
        summary = "Golden Retrievers are friendly, intelligent dogs often used as family pets, guide dogs, and rescue animals."
    ),
    Dog(
        id = 7,
        name = "Buddy",
        age = 2,
        weight = 24.0,
        breed = "Dalmatian",
        isTrained = false,
        imageResId = R.drawable.dog3,
        summary = "Dalmatians are energetic dogs famous for their unique black-spotted coat and strong endurance."
    ),

    Parrot(
        id = 8,
        name = "Rio",
        age = 3,
        weight = 0.6,
        breed = "Amazon Parrot",
        wingSpan = 35.0,
        imageResId = R.drawable.parrot2,
        summary = "Amazon parrots are intelligent green parrots known for their talking ability and social behavior."
    ),
    Parrot(
        id = 9,
        name = "Sunny",
        age = 2,
        weight = 0.7,
        breed = "Scarlet Macaw",
        wingSpan = 40.0,
        imageResId = R.drawable.parrot3,
        summary = "Scarlet macaws are bright red tropical parrots with impressive wingspans and high intelligence."
    )
)