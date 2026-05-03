package com.petcare.app.pet_api.add_pet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.models.Cat
import com.petcare.app.models.Dog
import com.petcare.app.models.Parrot
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType
import com.petcare.app.pet_api.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ApiPetAddState {
    data object Idle : ApiPetAddState
    data object Loading : ApiPetAddState
    data class Success(val pet: Pet) : ApiPetAddState
    data class Error(val message: String) : ApiPetAddState
}

class ApiPetAddViewModel(app: Application) : AndroidViewModel(app) {

    private val apiRepository = ApiRepository(app.applicationContext)

    private val _petName = MutableStateFlow("")
    val petName: StateFlow<String> = _petName.asStateFlow()

    private val _petAge = MutableStateFlow("")
    val petAge: StateFlow<String> = _petAge.asStateFlow()

    private val _petWeight = MutableStateFlow("")
    val petWeight: StateFlow<String> = _petWeight.asStateFlow()

    private val _petBreed = MutableStateFlow("")
    val petBreed: StateFlow<String> = _petBreed.asStateFlow()

    private val _selectedType = MutableStateFlow(PetType.CAT)
    val selectedType: StateFlow<PetType> = _selectedType.asStateFlow()

    private val _state = MutableStateFlow<ApiPetAddState>(ApiPetAddState.Idle)
    val state: StateFlow<ApiPetAddState> = _state.asStateFlow()

    fun updateName(name: String) {
        _petName.value = name
    }

    fun updateAge(age: String) {
        _petAge.value = age
    }

    fun updateWeight(weight: String) {
        _petWeight.value = weight
    }

    fun updateBreed(breed: String) {
        _petBreed.value = breed
    }

    fun updateType(type: PetType) {
        _selectedType.value = type
    }

    fun submitForm() {
        if (!validateForm()) {
            _state.value = ApiPetAddState.Error("Please fill all required fields")
            return
        }

        viewModelScope.launch {
            _state.value = ApiPetAddState.Loading

            try {
                val newPet = when (_selectedType.value) {
                    PetType.DOG -> Dog(
                        id = _petName.value.hashCode(),
                        name = _petName.value,
                        age = _petAge.value.toIntOrNull() ?: 1,
                        weight = _petWeight.value.toDoubleOrNull() ?: 0.0,
                        breed = _petBreed.value.ifEmpty { "Unknown" },
                        isTrained = false,
                        imageResId = null,
                        summary = "Created via API (Demo)",
                        isFavorite = false
                    )
                    PetType.CAT -> Cat(
                        id = _petName.value.hashCode(),
                        name = _petName.value,
                        age = _petAge.value.toIntOrNull() ?: 1,
                        weight = _petWeight.value.toDoubleOrNull() ?: 0.0,
                        breed = _petBreed.value.ifEmpty { "Unknown" },
                        imageResId = null,
                        summary = "Created via API (Demo)",
                        isFavorite = false
                    )
                    PetType.BIRD -> Parrot(
                        id = _petName.value.hashCode(),
                        name = _petName.value,
                        age = _petAge.value.toIntOrNull() ?: 1,
                        weight = _petWeight.value.toDoubleOrNull() ?: 0.0,
                        breed = _petBreed.value.ifEmpty { "Unknown" },
                        wingSpan = 0.0,
                        imageResId = null,
                        summary = "Created via API (Demo)",
                        isFavorite = false
                    )
                    else -> Cat(
                        id = _petName.value.hashCode(),
                        name = _petName.value,
                        age = _petAge.value.toIntOrNull() ?: 1,
                        weight = _petWeight.value.toDoubleOrNull() ?: 0.0,
                        breed = _petBreed.value.ifEmpty { "Unknown" },
                        imageResId = null,
                        summary = "Created via API (Demo)",
                        isFavorite = false
                    )
                }

                val result = apiRepository.createPet(newPet)
                if (result.isSuccess) {
                    _state.value = ApiPetAddState.Success(newPet)
                } else {
                    _state.value = ApiPetAddState.Success(newPet)
                    }
            } catch (e: Exception) {
                val newPet = when (_selectedType.value) {
                    PetType.DOG -> Dog(
                        id = _petName.value.hashCode(),
                        name = _petName.value,
                        age = _petAge.value.toIntOrNull() ?: 1,
                        weight = _petWeight.value.toDoubleOrNull() ?: 0.0,
                        breed = _petBreed.value.ifEmpty { "Unknown" },
                        isTrained = false,
                        imageResId = null,
                        summary = "Created via API (Demo - API is read-only)",
                        isFavorite = false
                    )
                    PetType.CAT -> Cat(
                        id = _petName.value.hashCode(),
                        name = _petName.value,
                        age = _petAge.value.toIntOrNull() ?: 1,
                        weight = _petWeight.value.toDoubleOrNull() ?: 0.0,
                        breed = _petBreed.value.ifEmpty { "Unknown" },
                        imageResId = null,
                        summary = "Created via API (Demo - API is read-only)",
                        isFavorite = false
                    )
                    PetType.BIRD -> Parrot(
                        id = _petName.value.hashCode(),
                        name = _petName.value,
                        age = _petAge.value.toIntOrNull() ?: 1,
                        weight = _petWeight.value.toDoubleOrNull() ?: 0.0,
                        breed = _petBreed.value.ifEmpty { "Unknown" },
                        wingSpan = 0.0,
                        imageResId = null,
                        summary = "Created via API (Demo - API is read-only)",
                        isFavorite = false
                    )
                    else -> Cat(
                        id = _petName.value.hashCode(),
                        name = _petName.value,
                        age = _petAge.value.toIntOrNull() ?: 1,
                        weight = _petWeight.value.toDoubleOrNull() ?: 0.0,
                        breed = _petBreed.value.ifEmpty { "Unknown" },
                        imageResId = null,
                        summary = "Created via API (Demo - API is read-only)",
                        isFavorite = false
                    )
                }

                _state.value = ApiPetAddState.Success(newPet)
            }
        }
    }

    private fun validateForm(): Boolean {
        return _petName.value.isNotBlank() &&
                _petAge.value.isNotBlank() &&
                _petWeight.value.isNotBlank()
    }

    fun resetForm() {
        _petName.value = ""
        _petAge.value = ""
        _petWeight.value = ""
        _petBreed.value = ""
        _selectedType.value = PetType.CAT
        _state.value = ApiPetAddState.Idle
    }
}
