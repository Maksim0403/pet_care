package com.petcare.app.mainscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.petcare.app.data.animals
import com.petcare.app.lab_1.runLabDemonstration
import com.petcare.app.navigation.NavGraph
import com.petcare.app.pet_list.PetListScreen
import com.petcare.app.ui.theme.PetCareTheme

//(animals.map { it.type }.distinct().toMutableList().apply { add( 0, PetType.ALL) }.toList())
//val selected = PetType.CAT;(animals.map { it.type }.distinct().toMutableList().apply { add( 0, PetType.ALL) }.toList())
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Lab 1
        runLabDemonstration()

        setContent {
            val navController     = rememberNavController()
            val hasSeenOnboarding = false // swap with DataStore/SharedPrefs read

            PetCareTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val pets by remember { mutableStateOf(animals) }
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        animals =pets,
                        hasSeenOnboarding = hasSeenOnboarding,
                    )
                }
            }
        }
        /*setContent {
            PetCareTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val pets by remember { mutableStateOf(animals) }
                    PetListScreen(modifier = Modifier.padding(innerPadding), pets)
                }
            }
        }*/
    }
}