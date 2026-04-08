package com.petcare.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.petcare.app.add_new_pet.AddNewPetScreen
import com.petcare.app.user_profile.UserProfileScreen
import com.petcare.app.enter_name.EnterNameScreen
import com.petcare.app.models.Pet
import com.petcare.app.onboarding.OnBoardingScreen
import com.petcare.app.pet_list.PetListScreen
import com.petcare.app.pet_profile.PetProfileScreen
import com.petcare.app.data.PetRepository
import kotlinx.coroutines.delay

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier,
    onPetAdded: (Pet) -> Unit,
) {
    var isLoading by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (isLoading) {
            delay(2500)
            isLoading = false
        }
    }

    val startDestination = Screen.Onboarding.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screen.Onboarding.route) {
            OnBoardingScreen(navController = navController, onNameChanged = {name = it})
        }

        composable(Screen.EnterName.route) {
            EnterNameScreen(modifier = modifier, navController = navController)
        }

        composable(Screen.PetList.route) {
            PetListScreen(
                isColumn = true,
                modifier = modifier,
                onPetAddClicked = {
                    navController.navigate(Screen.AddNewPet.route)
                },
                onPetClicked = { pet ->
                    navController.navigate(Screen.PetDetail.createRoute(pet.id))
                }
            )
        }

        composable(Screen.AddNewPet.route) {
            AddNewPetScreen(
                modifier = Modifier,
                onAnimalAdded = { pet ->
                    onPetAdded(pet)
                    navController.popBackStack()
                },
                onCloseClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.PetGrid.route) {
            PetListScreen(
                isColumn = false,
                modifier = modifier,
                onPetAddClicked = {
                    navController.navigate(Screen.AddNewPet.route)
                },
                onPetClicked = { pet ->
                    navController.navigate(Screen.PetDetail.createRoute(pet.id))
                }
            )
        }

        composable(
            route = Screen.PetDetail.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            PetProfileScreen(modifier, petId, onBackClicked = {
                navController.popBackStack()
            })
        }

        composable(Screen.Profile.route) {
            UserProfileScreen(
                modifier = modifier,
                name = name,
                onNameChanged = {
                    name = it
                }
            )
        }
    }
}