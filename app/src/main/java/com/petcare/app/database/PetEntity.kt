package com.petcare.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.petcare.app.models.PetType

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: PetType,
    val age: Int,
    val weight: Double,
    val breed: String?,
    val imageResId: Int?,
    val summary: String,
    val isTrained: Boolean? = null,
    val wingSpan: Double? = null,
    val isFavorite: Boolean = false
)

