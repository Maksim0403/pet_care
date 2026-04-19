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
    val isTrained: Boolean? = null, // For Dog
    val wingSpan: Double? = null,   // For Parrot
    val isFavorite: Boolean = false  // NEW: Track favorite status
)

