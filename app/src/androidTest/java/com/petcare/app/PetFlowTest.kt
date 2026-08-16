package com.petcare.app

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.petcare.app.add_new_pet.AddNewPetScreen
import com.petcare.app.data.MockPetRepository
import com.petcare.app.data.SettingsDataStore
import com.petcare.app.pet_list.PetListScreen
import com.petcare.app.ui.theme.PetCareTheme
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import androidx.test.platform.app.InstrumentationRegistry

class PetFlowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testAddPet_AndSwitchToGrid() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            PetCareTheme {
                val navController = rememberNavController()
                val settingsDataStore = remember { SettingsDataStore(context) }
                val scope = rememberCoroutineScope()

                NavHost(navController = navController, startDestination = "list") {
                    //  1: Екран списку (режим List)
                    composable("list") {
                        PetListScreen(
                            isColumn = true,
                            onPetAddClicked = { navController.navigate("add") },
                            onPetClicked = { },
                            settingsDataStore = settingsDataStore
                        )
                    }
                    
                    //  2: Форма додавання
                    composable("add") {
                        AddNewPetScreen(
                            onAnimalAdded = { pet ->
                                scope.launch {
                                    MockPetRepository.addPet(pet)
                                    navController.navigate("grid") {
                                        popUpTo("list") { inclusive = true }
                                    }
                                }
                            },
                            onCloseClicked = { navController.popBackStack() }
                        )
                    }
                    
                    //  3: Екран списку (режим Grid)
                    composable("grid") {
                        PetListScreen(
                            isColumn = false, // Режим сітки
                            onPetAddClicked = { },
                            onPetClicked = { },
                            settingsDataStore = settingsDataStore
                        )
                    }
                }
            }
        }

        // 1.
        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodesWithTag("add_pet_button").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("add_pet_button").performClick()

        // 2.
        composeTestRule.onNodeWithTag("pet_name_field").performTextInput("GridBuddy")
        composeTestRule.onNodeWithTag("pet_age_field").performTextInput("1")
        composeTestRule.onNodeWithTag("add_pet_submit_button").performClick()

        composeTestRule.waitUntil(15000) {
            composeTestRule.onAllNodesWithTag("pet_grid_lazy_vertical_grid").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("pet_grid_lazy_vertical_grid").assertIsDisplayed()
        composeTestRule.onNodeWithText("GridBuddy").assertIsDisplayed()

        Thread.sleep(3000)
    }
}
