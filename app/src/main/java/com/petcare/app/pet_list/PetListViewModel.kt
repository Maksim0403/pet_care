package com.petcare.app.pet_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.data.PetRepository
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType
import com.petcare.app.models.SortOrder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetListViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedType = MutableStateFlow(PetType.ALL)
    val selectedType: StateFlow<PetType> = _selectedType.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NONE)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites.asStateFlow()

    val allPets: StateFlow<List<Pet>> = PetRepository.petsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredPets: StateFlow<List<Pet>> = combine(
        PetRepository.petsFlow,
        _selectedType,
        _sortOrder,
        _showOnlyFavorites
    ) { pets, type, sort, showFavOnly ->
        var filtered = if (type == PetType.ALL) pets else pets.filter { it.type == type }
        
        if (showFavOnly) {
            filtered = filtered.filter { it.isFavorite }
        }
        
        when (sort) {
            SortOrder.NONE -> filtered
            SortOrder.NAME -> filtered.sortedBy { it.name }
            SortOrder.AGE -> filtered.sortedBy { it.age }
            SortOrder.WEIGHT -> filtered.sortedBy { it.weight }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadPets()
    }

    private fun loadPets() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(750) // 0.75 seconds
            _isLoading.value = false
        }
    }

    fun setSelectedType(type: PetType) {
        _selectedType.value = type
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    fun toggleShowOnlyFavorites() {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
    }

    fun removePet(pet: Pet) {
        viewModelScope.launch {
            PetRepository.removePet(pet)
        }
    }

    fun toggleFavorite(petId: Int, currentState: Boolean) {
        viewModelScope.launch {
            PetRepository.toggleFavorite(petId, !currentState)
        }
    }
}