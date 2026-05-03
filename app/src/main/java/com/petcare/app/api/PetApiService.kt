package com.petcare.app.api

import com.petcare.app.pet_api.MockPetRequest
import com.petcare.app.pet_api.MockPetResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PetApiService {

    @GET("pets")
    suspend fun getAllPets(): Response<List<MockPetResponse>>

    @GET("pets")
    suspend fun searchPets(@Query("name") name: String): Response<List<MockPetResponse>>

    @GET("pets/{id}")
    suspend fun getPetById(@Path("id") petId: String): Response<MockPetResponse>

    @POST("pets")
    suspend fun createPet(@Body request: MockPetRequest): Response<MockPetResponse>

    @DELETE("petsу/{id}")
    suspend fun deletePet(@Path("id") petId: String): Response<Unit>

    @PUT("pets/{id}")
    suspend fun updatePet(@Path("id") petId: String, @Body request: MockPetRequest): Response<MockPetResponse>
}