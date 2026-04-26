package com.petcare.app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Query("SELECT * FROM pets")
    fun getAllPets(): Flow<List<PetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)

    @Query("UPDATE pets SET isFavorite = :isFavorite WHERE id = :petId")
    suspend fun updateFavoriteStatus(petId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM pets WHERE isFavorite = 1")
    fun getFavoritePets(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE id = :petId")
    suspend fun getPetById(petId: Int): PetEntity?

    @Query("SELECT * FROM pets")
    suspend fun getAllPetsOnce(): List<PetEntity>
}
