package com.petcare.app.user_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petcare.app.data.SettingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class UserProfileViewModel(
    private val settingsDataStore: SettingsDataStore,
    private val onNameChanged: (String) -> Unit
) : ViewModel() {

    private val _nameInput = MutableStateFlow("")
    val nameInput: StateFlow<String> = _nameInput.asStateFlow()

    private val _measurementUnitInput = MutableStateFlow("kg")
    val measurementUnitInput: StateFlow<String> = _measurementUnitInput.asStateFlow()

    private val _sortModeInput = MutableStateFlow("name")
    val sortModeInput: StateFlow<String> = _sortModeInput.asStateFlow()

    private val _languageInput = MutableStateFlow("en")
    val languageInput: StateFlow<String> = _languageInput.asStateFlow()

    private val _userImage = MutableStateFlow<Uri?>(null)
    val userImage: StateFlow<Uri?> = _userImage.asStateFlow()

    init {
        viewModelScope.launch {
            settingsDataStore.userName.collect { name ->
                _nameInput.value = name
            }
        }

        viewModelScope.launch {
            settingsDataStore.measurementUnit.collect { unit ->
                _measurementUnitInput.value = unit
            }
        }

        viewModelScope.launch {
            settingsDataStore.sortMode.collect { mode ->
                _sortModeInput.value = mode
            }
        }

        viewModelScope.launch {
            settingsDataStore.language.collect { lang ->
                _languageInput.value = lang
            }
        }

        viewModelScope.launch {
            settingsDataStore.userImage.collect { image ->
                _userImage.value = image?.toUri()
            }
        }
    }

    fun updateNameInput(newName: String) {
        _nameInput.value = newName
    }

    fun updateMeasurementUnit(unit: String) {
        _measurementUnitInput.value = unit
    }

    fun updateSortMode(mode: String) {
        _sortModeInput.value = mode
    }

    fun updateLanguage(lang: String) {
        _languageInput.value = lang
    }

    fun saveAllSettings(userImageUri: Uri? = null) {
        viewModelScope.launch {
            // Save each setting to DataStore
            settingsDataStore.saveUserName(_nameInput.value)
            settingsDataStore.saveMeasurementUnit(_measurementUnitInput.value)
            settingsDataStore.saveSortMode(_sortModeInput.value)
            settingsDataStore.saveLanguage(_languageInput.value)
            userImageUri?.let {
                settingsDataStore.saveUserImage(userImageUri.toString())
            }

            onNameChanged(_nameInput.value)
        }
    }

    companion object {
        class Factory(
            private val settingsDataStore: SettingsDataStore,
            private val onNameChanged: (String) -> Unit
        ) : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
                    return UserProfileViewModel(settingsDataStore, onNameChanged) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
