package com.petcare.app.data

import android.annotation.SuppressLint
import android.content.Context
import com.petcare.app.database.PetDatabase
import com.petcare.app.database.PetMapper
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@SuppressLint("StaticFieldLeak")
object PetRepository {

    lateinit var context: Context

    fun init(appContext: Context) {
        context = appContext.applicationContext
    }

    private val database by lazy { PetDatabase.getDatabase(context) }
    private val petDao by lazy { database.petDao() }

    val petsFlow: Flow<List<Pet>>
        get() = petDao.getAllPets().map { entities ->
            entities.map { PetMapper.toPet(it) }
        }

    val favoritePetsFlow: Flow<List<Pet>>
        get() = petDao.getFavoritePets().map { entities ->
            entities.map { PetMapper.toPet(it) }
        }

    fun getAllPets(): List<Pet> {
        return emptyList()
    }

    suspend fun addPet(pet: Pet) {
        val entity = PetMapper.toEntity(pet)
        petDao.insertPet(entity)
    }

    suspend fun removePet(pet: Pet) {
        val entity = PetMapper.toEntity(pet)
        petDao.deletePet(entity)
    }

    suspend fun toggleFavorite(petId: Int, isFavorite: Boolean) {
        petDao.updateFavoriteStatus(petId, isFavorite)
    }

    fun getPetById(id: Int): Pet? {
        return null
    }

    fun getPetTypes(): List<PetType> = PetType.entries

    fun getNextId(): Int {
        return 0
    }
}