package com.petcare.app.mainscreen.adaptive

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavHostController
import com.petcare.app.data.PetRepository
import com.petcare.app.data.SettingsDataStore
import com.petcare.app.navigation.BottomNavItem
import com.petcare.app.navigation.NavGraph
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AdaptiveNavigationLayout(
    navController: NavHostController,
    items: List<BottomNavItem>,
    currentRoute: String?,
    showBottomBar: Boolean,
    settingsDataStore: SettingsDataStore,
    lifecycleScope: LifecycleCoroutineScope,
    imageUri: Uri? = null,
    onUserImageCLicked: (() -> Unit)? = null,
) {

    val activity = LocalContext.current as ComponentActivity
    val windowSizeClass = calculateWindowSizeClass(activity)

    LaunchedEffect(windowSizeClass.widthSizeClass) {
        Log.d("Adaptive", "Class: ${windowSizeClass.widthSizeClass}")
    }


    val content: @Composable (PaddingValues) -> Unit = { innerPadding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            onPetAdded = { pet ->
                lifecycleScope.launch {
                    PetRepository.addPet(pet)
                }
            },
            settingsDataStore = settingsDataStore,
            imageUri = imageUri,
            isScreenExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded,
            onUserImageCLicked = onUserImageCLicked
        )
    }

    when (windowSizeClass.widthSizeClass) {

        WindowWidthSizeClass.Compact -> {
            BottomBarLayout(
                navController,
                items,
                currentRoute,
                showBottomBar,
                content
            )
        }

        WindowWidthSizeClass.Medium -> {
            RailLayout(
                navController,
                items,
                currentRoute,
                content
            )
        }

        WindowWidthSizeClass.Expanded -> {
            DrawerLayout(
                navController,
                items,
                currentRoute,
                content
            )
        }
    }
}

@Composable
fun BottomBarLayout(
    navController: NavHostController,
    items: List<BottomNavItem>,
    currentRoute: String?,
    showBottomBar: Boolean,
    content: @Composable (PaddingValues) -> Unit
) {

    Scaffold(
        bottomBar = {
            // if (showBottomBar) {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
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
                        icon = { Icon(item.icon, item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
        //    }
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
fun RailLayout(
    navController: NavHostController,
    items: List<BottomNavItem>,
    currentRoute: String?,
    content: @Composable (PaddingValues) -> Unit
) {

    Row {

        NavigationRail {
            items.forEach { item ->
                NavigationRailItem(
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(item.icon, item.label) },
                    label = { Text(item.label) }
                )
            }
        }

        Scaffold { innerPadding ->
            content(innerPadding)
        }
    }
}

@Composable
fun DrawerLayout(
    navController: NavHostController,
    items: List<BottomNavItem>,
    currentRoute: String?,
    content: @Composable (PaddingValues) -> Unit
) {

    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(
                modifier = Modifier.width(150.dp)
            ) {
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label) },
                        selected = currentRoute == item.screen.route,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {

        Scaffold { innerPadding ->
            content(innerPadding)
        }

    }
}