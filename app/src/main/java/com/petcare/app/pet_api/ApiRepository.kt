package com.petcare.app.pet_api

import android.annotation.SuppressLint
import android.content.Context
import com.petcare.app.models.Pet
import com.petcare.app.api.ApiClient
import com.petcare.app.api.PetApiService
import com.petcare.app.database.PetDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@SuppressLint("StaticFieldLeak")
class ApiRepository(private val apiService: PetApiService = ApiClient.petApiService) {
    private var cachedPets: List<Pet>? = null
    private var cachedPetById: Map<String, Pet> = emptyMap()

    sealed class ApiResult<T> {
        data class Success<T>(val data: T, val isCached: Boolean = false) : ApiResult<T>()
        data class Error<T>(val exception: Exception, val cachedData: T? = null) : ApiResult<T>()
    }

    fun getAllPets(): Flow<ApiResult<List<Pet>>> = flow {
        try {
            val response = apiService.getAllPets()
            if (response.isSuccessful) {
                val apiPets = response.body() ?: emptyList()
                val domainPets = apiPets.map { it.toPet() }
                cachedPets = domainPets
                emit(ApiResult.Success(domainPets, isCached = false))
            } else {
                val error = Exception("HTTP ${response.code()}: ${response.message()}")
                emit(ApiResult.Error(error, cachedData = cachedPets))
            }
        } catch (exception: Exception) {
            emit(ApiResult.Error(exception, cachedData = cachedPets))
        }
    }

    fun searchPets(name: String): Flow<ApiResult<List<Pet>>> = flow {
        try {
            val response = apiService.searchPets(name)
            if (response.isSuccessful) {
                val apiPets = response.body() ?: emptyList()
                val domainPets = apiPets.map { it.toPet() }
                domainPets.forEach { pet ->
                    cachedPetById = cachedPetById + (pet.id.toString() to pet)
                }
                emit(ApiResult.Success(domainPets, isCached = false))
            } else {
                val error = Exception("HTTP ${response.code()}: ${response.message()}")
                emit(ApiResult.Error(error, cachedData = emptyList()))
            }
        } catch (exception: Exception) {
            emit(ApiResult.Error(exception, cachedData = emptyList()))
        }
    }

    fun getPetById(identifier: String): Flow<ApiResult<Pet?>> = flow {
        try {
            cachedPetById[identifier]?.let { cached ->
                emit(ApiResult.Success(cached, isCached = true))
                return@flow
            }

            val response = apiService.getPetById(identifier)
            if (response.isSuccessful) {
                val apiPet = response.body()
                if (apiPet != null) {
                    val domainPet = apiPet.toPet()
                    cachedPetById = cachedPetById + (identifier to domainPet)
                    emit(ApiResult.Success(domainPet, isCached = false))
                } else {
                    emit(ApiResult.Error<Pet?>(Exception("Pet not found"), cachedData = null))
                }
            } else {
                val error = Exception("HTTP ${response.code()}: ${response.message()}")
                emit(ApiResult.Error<Pet?>(error, cachedData = cachedPetById[identifier]))
            }
        } catch (exception: Exception) {
            emit(ApiResult.Error<Pet?>(exception, cachedData = cachedPetById[identifier]))
        }
    }

    suspend fun createPet(pet: Pet): Result<Pet> {
        return try {
            val request = MockPetResponse.fromPet(pet)
            val response = apiService.createPet(request)

            if (response.isSuccessful) {
                val createdPet = response.body()?.toPet()
                if (createdPet != null) {
                    Result.success(createdPet)
                } else {
                    Result.failure(Exception("Failed to parse response"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()} ${response.message()}"))
            }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun deletePet(petId: String): Result<Unit> {
        return try {
            val response = apiService.deletePet(petId)

            if (response.isSuccessful) {
                cachedPetById = cachedPetById.filterKeys { it != petId }
                Result.success(Unit)
            } else {
                Result.failure(Exception("API Error: ${response.code()} ${response.message()}"))
            }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
