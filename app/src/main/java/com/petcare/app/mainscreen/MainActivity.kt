package com.petcare.app.mainscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.petcare.app.data.PetRepository
import com.petcare.app.lab_1.runLabDemonstration
import com.petcare.app.models.Pet
import com.petcare.app.navigation.BottomNavItem
import com.petcare.app.navigation.NavGraph
import com.petcare.app.ui.theme.PetCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Lab 1
        runLabDemonstration()

        setContent {
            var pets by remember { mutableStateOf<List<Pet>>(PetRepository.getAllPets()) }

            val navController = rememberNavController()

            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            val items = listOf(
                BottomNavItem.PetList,
                BottomNavItem.PetGrid,
                BottomNavItem.Profile
            )

            val showBottomBar = items.any { it.screen.route == currentRoute }

            PetCareTheme {
                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ) {
                                items.forEach { item ->
                                    NavigationBarItem(
                                        selected = currentRoute == item.screen.route,
                                        onClick = {
                                            navController.navigate(item.screen.route) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = { Icon(item.icon, contentDescription = item.label) },
                                        label = { Text(item.label) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.primary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                            unselectedTextColor = MaterialTheme.colorScheme.outlineVariant,
                                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        onPetAdded = { pet ->
                            PetRepository.addPet(pet)
                            pets = PetRepository.getAllPets()
                        },
                    )
                }
            }
        }
    }
}