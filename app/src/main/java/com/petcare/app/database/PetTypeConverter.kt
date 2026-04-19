package com.petcare.app.database

import androidx.room.TypeConverter
import com.petcare.app.models.PetType

class PetTypeConverter {

    @TypeConverter
    fun fromPetType(petType: PetType): String {
        return petType.name
    }

    @TypeConverter
    fun toPetType(value: String): PetType {
        return PetType.valueOf(value)
    }
}
