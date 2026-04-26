package com.petcare.app.pet_api.pet_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petcare.app.models.Pet
import com.petcare.app.pet_api.ApiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed interface ApiPetDetailState {
    data object Loading : ApiPetDetailState
    data class Success(
        val pet: Pet,
        val statistics: String
    ) : ApiPetDetailState
    data class Error(val message: String) : ApiPetDetailState
}

class ApiPetProfileViewModel(private val petIdentifier: String) : ViewModel() {

    private val apiRepository = ApiRepository()

    private val _state = MutableStateFlow<ApiPetDetailState>(ApiPetDetailState.Loading)
    val state: StateFlow<ApiPetDetailState> = _state

    init {
        loadPetDetails()
    }

    private fun loadPetDetails() {
        viewModelScope.launch {
            _state.value = ApiPetDetailState.Loading
            delay(500) // Simulate loading delay

            try {
                apiRepository.getPetById(petIdentifier).collectLatest { result ->
                    when (result) {
                        is ApiRepository.ApiResult.Success -> {
                            if (result.data != null) {
                                val statistics = computeStatistics(result.data)
                                val statusMsg = if (result.isCached) " (cached)" else ""
                                _state.value = ApiPetDetailState.Success(
                                    result.data,
                                    statistics + statusMsg
                                )
                            } else {
                                _state.value = ApiPetDetailState.Error("Animal not found")
                            }
                        }
                        is ApiRepository.ApiResult.Error -> {
                            if (result.cachedData != null) {
                                val statistics = computeStatistics(result.cachedData)
                                _state.value = ApiPetDetailState.Success(
                                    result.cachedData,
                                    statistics + " (cached)"
                                )
                            } else {
                                _state.value = ApiPetDetailState.Error("Network error: ${result.exception.message}")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = ApiPetDetailState.Error("Error: ${e.message}")
            }
        }
    }

    private fun computeStatistics(pet: Pet): String {
        return buildString {
            appendLine("Type: ${pet.type}")
            appendLine("Name: ${pet.name}")
            appendLine("Age: ${pet.age} years")
            appendLine("Weight: ${pet.weight} kg")
            pet.breed?.let { appendLine("Breed: $it") }
            appendLine("Data source: API Ninjas Animals API")
        }.trimEnd()
    }

    fun retry() {
        loadPetDetails()
    }

    class Factory(private val petIdentifier: String) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ApiPetProfileViewModel::class.java)) {
                return ApiPetProfileViewModel(petIdentifier) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
