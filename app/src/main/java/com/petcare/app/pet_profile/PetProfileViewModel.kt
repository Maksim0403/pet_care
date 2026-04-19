package com.petcare.app.pet_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petcare.app.data.PetRepository
import com.petcare.app.models.Pet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface PetDetailState {
    data object Loading : PetDetailState
    data class Success(
        val pet: Pet,
        val relatedPets: List<Pet>,
        val statistics: String
    ) : PetDetailState
    data class Error(val message: String) : PetDetailState
}

class PetProfileViewModel(private val petId: Int) : ViewModel() {

    private val _state = MutableStateFlow<PetDetailState>(PetDetailState.Loading)
    val state: StateFlow<PetDetailState> = _state

    init {
        loadPetDetails()
    }

    private fun loadPetDetails() {
        viewModelScope.launch {
            _state.value = PetDetailState.Loading
            delay(500) // Simulate loading delay

            val pet = PetRepository.getPetById(petId)
            if (pet != null) {
                val relatedPets = computeRelatedPets(pet)
                val statistics = computeStatistics(pet)
                _state.value = PetDetailState.Success(pet, relatedPets, statistics)
            } else {
                _state.value = PetDetailState.Error("Pet not found")
            }
        }
    }

    private fun computeRelatedPets(pet: Pet): List<Pet> {
        // Return other pets of the same type, excluding the current pet
        return PetRepository.getAllPets()
            .filter { it.type == pet.type && it.id != pet.id }
            .take(3) // Limit to 3 related pets
    }

    private fun computeStatistics(pet: Pet): String {
        val allPets = PetRepository.getAllPets()
        val typeCount = allPets.count { it.type == pet.type }
        val totalPets = allPets.size
        val typePercentage = if (totalPets > 0) (typeCount * 100) / totalPets else 0

        return "Type: ${pet.type.title}\n" +
               "Pets of this type: $typeCount\n" +
               "Percentage: $typePercentage%\n" +
               "Total pets: $totalPets"
    }

    class Factory(private val petId: Int) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PetProfileViewModel::class.java)) {
                return PetProfileViewModel(petId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}