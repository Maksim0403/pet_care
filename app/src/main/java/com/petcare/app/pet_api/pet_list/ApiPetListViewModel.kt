package com.petcare.app.pet_api.pet_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.models.Pet
import com.petcare.app.pet_api.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ApiPetListViewModel(app: Application) : AndroidViewModel(app) {

    private val apiRepository = ApiRepository(app.applicationContext)

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isDeletingId = MutableStateFlow<String?>(null)
    val isDeletingId: StateFlow<String?> = _isDeletingId.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    private val _isCachedData = MutableStateFlow(false)
    val isCachedData: StateFlow<Boolean> = _isCachedData.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun clearSnackbar() { _snackbarMessage.value = null }

    init {
        loadPets()
    }

    fun loadPets() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                apiRepository.getAllPets().collect { result ->
                    when (result) {
                        is ApiRepository.ApiResult.Success -> {
                            _pets.value = result.data
                            _isCachedData.value = result.isCached
                            _isOffline.value = false
                            _isLoading.value = false
                            _error.value = null
                        }
                        is ApiRepository.ApiResult.Error -> {
                            if (result.cachedData != null && result.cachedData.isNotEmpty()) {
                                _pets.value = result.cachedData
                                _isOffline.value = true
                                _isCachedData.value = true
                                _error.value = null
                                _isLoading.value = false
                            } else {
                                _error.value = "Network error: ${result.exception.message}"
                                _isLoading.value = false
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message ?: "Unknown error"}"
                _isLoading.value = false
            }
        }
    }

    fun searchPets(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            loadPets()
        } else {
            viewModelScope.launch {
                _isLoading.value = true
                _error.value = null

                try {
                    apiRepository.searchPets(query).collect { result ->
                        when (result) {
                            is ApiRepository.ApiResult.Success -> {
                                _pets.value = result.data
                                _isCachedData.value = result.isCached
                                _isOffline.value = false
                                _isLoading.value = false
                                _error.value = null
                            }
                            is ApiRepository.ApiResult.Error -> {
                                _error.value = "Search failed: ${result.exception.message}"
                                _isLoading.value = false
                            }
                        }
                    }
                } catch (e: Exception) {
                    _error.value = "Search error: ${e.message ?: "Unknown error"}"
                    _isLoading.value = false
                }
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        loadPets()
    }

    fun retry() {
        if (_searchQuery.value.isBlank()) {
            loadPets()
        } else {
            searchPets(_searchQuery.value)
        }
    }

    fun deletePet(petId: String) {
        viewModelScope.launch {
            _isDeletingId.value = petId
            try {
                val result = apiRepository.deletePet(petId)
                _pets.value = _pets.value.filter {
                    it.id.toString() != petId && it.id != petId.toIntOrNull()
                }
                _isDeletingId.value = null

                if (result.isFailure) {
                    _snackbarMessage.value = "Failed to delete pet"
                }
            } catch (e: Exception) {
                _error.value = "Delete error: ${e.message}"
                _isDeletingId.value = null
            }
        }
    }
}
