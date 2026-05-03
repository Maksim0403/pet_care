package com.petcare.app.mainscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.petcare.app.data.PetRepository
import com.petcare.app.data.SettingsDataStore
import com.petcare.app.lab_1.runLabDemonstration
import com.petcare.app.mainscreen.adaptive.AdaptiveNavigationLayout
import com.petcare.app.navigation.BottomNavItem
import com.petcare.app.ui.theme.PetCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize PetRepository with context
        PetRepository.init(this)
        // Lab 1
        runLabDemonstration()

        setContent {
            // Create SettingsDataStore instance for this Activity
            val settingsDataStore = remember { SettingsDataStore(this@MainActivity) }

            val navController = rememberNavController()

            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            val items = listOf(
                BottomNavItem.PetList,
                BottomNavItem.PetGrid,
                BottomNavItem.ApiPets,
                BottomNavItem.Profile
            )

            val showBottomBar = items.any { it.screen.route == currentRoute }

            PetCareTheme {
                AdaptiveNavigationLayout(
                    navController = navController,
                    items = items,
                    currentRoute = currentRoute,
                    showBottomBar = showBottomBar,
                    settingsDataStore = settingsDataStore,
                    lifecycleScope = lifecycleScope
                )
            }
        }
    }
}