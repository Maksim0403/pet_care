package com.petcare.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
) {
    data object PetList : BottomNavItem(Screen.PetList, "List", Default.List)
    data object PetGrid : BottomNavItem(Screen.PetGrid, "Grid", Default.GridView)
    data object ApiPets : BottomNavItem(Screen.ApiPetList, "API", Default.Api)
    data object Profile : BottomNavItem(Screen.Profile, "Profile", Default.Person)
}