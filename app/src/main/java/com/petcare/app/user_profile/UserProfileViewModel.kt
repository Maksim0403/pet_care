package com.petcare.app.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val initialName: String,
    private val onNameChanged: (String) -> Unit
) : ViewModel() {

    private val _nameInput = MutableStateFlow(initialName)
    val nameInput: StateFlow<String> = _nameInput

    fun updateNameInput(newName: String) {
        _nameInput.value = newName
    }

    fun updateUsername() {
        viewModelScope.launch {
            onNameChanged(_nameInput.value)
        }
    }

    companion object {
        class Factory(
            private val initialName: String,
            private val onNameChanged: (String) -> Unit
        ) : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
                    return UserProfileViewModel(initialName, onNameChanged) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
