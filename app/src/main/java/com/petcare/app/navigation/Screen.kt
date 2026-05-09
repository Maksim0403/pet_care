package com.petcare.app.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object EnterName : Screen("enter_name")
    data object PetList   : Screen("pet_list")
    data object PetGrid   : Screen("pet_grid")
    data object AddNewPet   : Screen("add_new_pet")
    data object PetDetail : Screen("pet_detail/{petId}") {
        fun createRoute(petId: Int) = "pet_detail/$petId"
    }
    data object Profile   : Screen("profile")
    data object ApiPetList : Screen("api_pet_list")
    data object ApiPetDetail : Screen("api_pet_detail/{petIdentifier}") {
        fun createRoute(petIdentifier: String) = "api_pet_detail/$petIdentifier"
    }

    data object ApiPetAdd : Screen("api_pet_add")

    data object Location : Screen("location")
}