package com.petcare.app.add_new_pet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.petcare.app.models.Pet
import com.petcare.app.models.Cat
import com.petcare.app.models.Dog
import org.junit.Rule
import org.junit.Test
import kotlin.collections.plus

class AddNewPetScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addNewPet_threeScreenFlow_petAppearsInList() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            var pets by remember { mutableStateOf(listOf<Pet>()) }


            NavHost(navController = navController, startDestination = "list") {
                composable("list") {
                    Column(modifier = Modifier.Companion.fillMaxSize()) {
                        Text(
                            text = "Pet List",
                            modifier = Modifier.Companion.testTag("screen_pet_list")
                        )
                        Button(
                            onClick = { navController.navigate("add") },
                            modifier = Modifier.Companion.testTag("btn_navigate_to_add")
                        ) { Text("Add Pet") }
                        LazyColumn {
                            items(pets) { pet ->
                                Text(
                                    text = pet.name,
                                    modifier = Modifier.Companion.testTag("pet_item_${pet.name}")
                                )
                            }
                        }
                    }
                }
                composable("add") {
                    AddNewPetScreen(
                        onAnimalAdded = { newPet ->
                            pets = pets + newPet
                            navController.popBackStack()
                        },
                        onCloseClicked = { navController.popBackStack() }
                    )
                }
            }
        }

        // Екран 1 — список
        composeTestRule.onNodeWithTag("screen_pet_list").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_navigate_to_add").performClick()

        // Екран 2 — форма
        composeTestRule.onNodeWithTag("pet_name_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("pet_age_field").assertIsDisplayed()
        composeTestRule.onNodeWithTag("pet_name_field").performTextInput("Buddy")
        composeTestRule.onNodeWithTag("pet_age_field").performTextInput("3")
        composeTestRule.onNodeWithTag("add_pet_submit_button").performClick()

        // Екран 3 — список з новим петом
        composeTestRule.onNodeWithTag("screen_pet_list").assertIsDisplayed()
        composeTestRule.onNodeWithTag("pet_item_Buddy").assertIsDisplayed()
        composeTestRule.onNodeWithText("Buddy").assertIsDisplayed()
    }
}