package com.petcare.app.pet_api.add_pet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.models.Cat
import com.petcare.app.models.Dog
import com.petcare.app.models.Parrot
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType
import com.petcare.app.pet_api.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


enum class FormField {
    NAME, BREED, AGE, WEIGHT, TYPE, EMAIL, PHONE
}


sealed interface ApiPetAddState {
    data object Idle : ApiPetAddState
    data object Loading : ApiPetAddState
    data class Success(val pet: Pet) : ApiPetAddState
    data class Error(val message: String) : ApiPetAddState
}


class ApiPetAddViewModel(app: Application) : AndroidViewModel(app) {

    private val apiRepository = ApiRepository(app.applicationContext)

    private val _petName      = MutableStateFlow("")
    val petName: StateFlow<String> = _petName.asStateFlow()

    private val _petAge       = MutableStateFlow("")
    val petAge: StateFlow<String> = _petAge.asStateFlow()

    private val _petWeight    = MutableStateFlow("")
    val petWeight: StateFlow<String> = _petWeight.asStateFlow()

    private val _petBreed     = MutableStateFlow("")
    val petBreed: StateFlow<String> = _petBreed.asStateFlow()

    private val _ownerEmail   = MutableStateFlow("")
    val ownerEmail: StateFlow<String> = _ownerEmail.asStateFlow()

    private val _ownerPhone   = MutableStateFlow("")
    val ownerPhone: StateFlow<String> = _ownerPhone.asStateFlow()

    private val _selectedType = MutableStateFlow(PetType.ALL)
    val selectedType: StateFlow<PetType> = _selectedType.asStateFlow()

    private val _isVaccinated = MutableStateFlow(false)
    val isVaccinated: StateFlow<Boolean> = _isVaccinated.asStateFlow()

    private val _healthScore  = MutableStateFlow(5f)
    val healthScore: StateFlow<Float> = _healthScore.asStateFlow()

    private val _errors = MutableStateFlow<Map<FormField, String>>(emptyMap())
    val errors: StateFlow<Map<FormField, String>> = _errors.asStateFlow()

    private val _state = MutableStateFlow<ApiPetAddState>(ApiPetAddState.Idle)
    val state: StateFlow<ApiPetAddState> = _state.asStateFlow()

    val isFormValid: StateFlow<Boolean> = combine(
        combine(_petName, _petAge, _petWeight, _ownerEmail, _ownerPhone) { name, age, weight, email, phone ->
            name.isNotBlank() && age.isNotBlank() && weight.isNotBlank() &&
                    email.isNotBlank() && phone.isNotBlank()
        },
        combine(_selectedType, _errors) { type, errs ->
            type != PetType.ALL && errs.isEmpty()
        }
    ) { fieldsValid, typeAndErrorsValid ->
        fieldsValid && typeAndErrorsValid
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun updateName(v: String)          { _petName.value = v }
    fun updateAge(v: String)           { _petAge.value = v }
    fun updateWeight(v: String)        { _petWeight.value = v }
    fun updateBreed(v: String)         { _petBreed.value = v }
    fun updateOwnerEmail(v: String)    { _ownerEmail.value = v }
    fun updateOwnerPhone(v: String)    { _ownerPhone.value = v }
    fun updateType(v: PetType)         { _selectedType.value = v }
    fun updateIsVaccinated(v: Boolean) { _isVaccinated.value = v }
    fun updateHealthScore(v: Float)    { _healthScore.value = v }

    fun validateField(field: FormField) {
        val error: String? = when (field) {
            FormField.NAME -> {
                val v = _petName.value
                when {
                    v.isBlank()  -> "Name is required"
                    v.length < 2 -> "Name must be at least 2 characters"
                    else         -> null
                }
            }
            FormField.BREED -> null

            FormField.AGE -> {
                val v = _petAge.value
                val n = v.toDoubleOrNull()
                when {
                    v.isBlank()     -> "Age is required"
                    n == null       -> "Age must be a number"
                    n < 0 || n > 50 -> "Age must be between 0 and 50"
                    else            -> null
                }
            }
            FormField.WEIGHT -> {
                val v = _petWeight.value
                val n = v.toDoubleOrNull()
                when {
                    v.isBlank()       -> "Weight is required"
                    n == null         -> "Weight must be a number"
                    n <= 0 || n > 500 -> "Weight must be between 0 and 500 kg"
                    else              -> null
                }
            }
            FormField.TYPE -> {
                if (_selectedType.value == PetType.ALL) "Please select a type" else null
            }
            FormField.EMAIL -> {
                val v = _ownerEmail.value
                val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
                when {
                    v.isBlank()       -> "Email is required"
                    !regex.matches(v) -> "Enter a valid email address"
                    else              -> null
                }
            }
            FormField.PHONE -> {
                val v = _ownerPhone.value
                    .replace(" ", "")
                    .replace("-", "")
                    .replace("(", "")
                    .replace(")", "")
                val regex = Regex("^(\\+?3?8?0|0)\\d{9}$")
                when {
                    v.isBlank()       -> "Phone is required"
                    !regex.matches(v) -> "Enter a valid phone (e.g. +380XXXXXXXXX)"
                    else              -> null
                }
            }
        }

        _errors.value = if (error != null) {
            _errors.value + (field to error)
        } else {
            _errors.value - field
        }
    }

    fun submitForm() {
        FormField.entries.forEach { validateField(it) }
        if (_errors.value.isNotEmpty()) return

        viewModelScope.launch {
            _state.value = ApiPetAddState.Loading
            try {
                val newPet = buildPet(summary = "Created via API")
                val result = apiRepository.createPet(newPet)
                _state.value = ApiPetAddState.Success(newPet)
            } catch (e: Exception) {
                _state.value = ApiPetAddState.Success(
                    buildPet(summary = "Created via API (Demo - API is read-only)")
                )
            }
        }
    }

    fun resetForm() {
        _petName.value      = ""
        _petAge.value       = ""
        _petWeight.value    = ""
        _petBreed.value     = ""
        _ownerEmail.value   = ""
        _ownerPhone.value   = ""
        _isVaccinated.value = false
        _healthScore.value  = 5f
        _selectedType.value = PetType.ALL
        _errors.value       = emptyMap()
        _state.value        = ApiPetAddState.Idle
    }

    private fun buildPet(summary: String): Pet {
        val id     = _petName.value.hashCode()
        val name   = _petName.value
        val age    = _petAge.value.toIntOrNull() ?: 1
        val weight = _petWeight.value.toDoubleOrNull() ?: 0.0
        val breed  = _petBreed.value.ifEmpty { "Unknown" }

        return when (_selectedType.value) {
            PetType.DOG -> Dog(
                id = id, name = name, age = age, weight = weight, breed = breed,
                isTrained = false, imageResId = null, summary = summary, isFavorite = false
            )
            PetType.BIRD -> Parrot(
                id = id, name = name, age = age, weight = weight, breed = breed,
                wingSpan = 0.0, imageResId = null, summary = summary, isFavorite = false
            )
            else -> Cat(  // CAT, FISH, REPTILE, MAMMAL → Cat як fallback
                id = id, name = name, age = age, weight = weight, breed = breed,
                imageResId = null, summary = summary, isFavorite = false
            )
        }
    }
}