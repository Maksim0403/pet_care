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
import com.petcare.app.pet_api.pet_list.ApiPetListScreen
import com.petcare.app.pet_api.pet_profile.ApiPetProfileScreen
import com.petcare.app.data.SettingsDataStore
import com.petcare.app.enter_name.EnterNameScreen
import com.petcare.app.models.Pet
import com.petcare.app.onboarding.OnBoardingScreen
import com.petcare.app.pet_api.add_pet.ApiPetAddScreen
import com.petcare.app.pet_list.PetListScreen
import com.petcare.app.pet_profile.PetProfileScreen
import com.petcare.app.user_profile.UserProfileScreen
import kotlinx.coroutines.delay

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier,
    onPetAdded: (Pet) -> Unit,
    settingsDataStore: SettingsDataStore,
    isScreenExpanded: Boolean = false,
) {
    var isLoading by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }

    // Check if user name exists in DataStore
    // This determines whether to show onboarding
    var hasExistingUser by remember { mutableStateOf(false) }
    var startDestination by remember { mutableStateOf(Screen.Onboarding.route) }

    LaunchedEffect(Unit) {
        // Check if user has already set their name
        settingsDataStore.hasUserName.collect { hasName ->
            hasExistingUser = hasName
            // If user exists, skip onboarding and go directly to PetList
            // Otherwise, show onboarding
            startDestination = if (hasName) Screen.PetList.route else Screen.Onboarding.route
        }
    }

    LaunchedEffect(Unit) {
        if (isLoading) {
            delay(2500)
            isLoading = false
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screen.Onboarding.route) {
            OnBoardingScreen(
                navController = navController,
                onNameChanged = { newName ->
                    name = newName
                }
            )
        }

        composable(Screen.EnterName.route) {
            EnterNameScreen(
                modifier = modifier,
                navController = navController,
                settingsDataStore = settingsDataStore
            )
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
                },
                settingsDataStore = settingsDataStore,
                isScreenExpanded = isScreenExpanded
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
                },
                settingsDataStore = settingsDataStore,
                isScreenExpanded = isScreenExpanded
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
                onNameChanged = { newName ->
                    name = newName
                },
                settingsDataStore = settingsDataStore
            )
        }

        composable(Screen.ApiPetList.route) {
            ApiPetListScreen(
                modifier = modifier,
                onPetClicked = { pet ->
                    navController.navigate(Screen.ApiPetDetail.createRoute(pet.id.toString()))
                },
                onAddClicked = {
                    navController.navigate(Screen.ApiPetAdd.route)
                }
            )
        }

        composable(
            route = Screen.ApiPetDetail.route,
            arguments = listOf(navArgument("petIdentifier") { type = NavType.StringType })
        ) { backStackEntry ->
            val petIdentifier = backStackEntry.arguments?.getString("petIdentifier") ?: ""
            ApiPetProfileScreen(modifier, petIdentifier, onBackClicked = {
                navController.popBackStack()
            })
        }

        composable(Screen.ApiPetAdd.route) {
            ApiPetAddScreen(modifier,  onPetAdded = {
                navController.popBackStack()
            })
        }
    }
}