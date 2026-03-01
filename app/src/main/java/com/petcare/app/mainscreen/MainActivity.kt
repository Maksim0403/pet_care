package com.petcare.app.mainscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.petcare.app.data.animals
import com.petcare.app.lab_1.runLabDemonstration
import com.petcare.app.ui.theme.PetCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Lab 1
        runLabDemonstration()

        setContent {
            PetCareTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PetProfileScreen(modifier = Modifier.padding(innerPadding), pet = animals[0])
                }
            }
        }
    }
}