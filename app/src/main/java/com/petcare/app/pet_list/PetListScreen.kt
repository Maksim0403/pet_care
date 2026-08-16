package com.petcare.app.pet_list

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.More
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.petcare.app.R
import com.petcare.app.components.PetGridItem
import com.petcare.app.components.PetHeader
import com.petcare.app.components.PetListItem
import com.petcare.app.components.PetTitle
import com.petcare.app.components.PetTypeOption
import com.petcare.app.data.MockPetRepository
import com.petcare.app.data.SettingsDataStore
import com.petcare.app.models.Pet
import com.petcare.app.models.PetType
import com.petcare.app.models.SortOrder
import com.petcare.app.pet_profile.PetProfile
import com.petcare.app.ui.theme.ButtonColor
import com.petcare.app.ui.theme.PetCareTheme


@Composable
fun PetListScreen(
    modifier: Modifier = Modifier,
    isColumn: Boolean,
    onPetAddClicked: () -> Unit,
    onPetClicked: (Pet) -> Unit,
    settingsDataStore: SettingsDataStore,
    isScreenExpanded: Boolean = false,
) {
    val viewModel = viewModel<MockPetListViewModel>(
        factory = MockPetListViewModel.Companion.Factory(settingsDataStore)
    )
    val pets by viewModel.filteredPets.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val allPets by viewModel.allPets.collectAsStateWithLifecycle()
    val selectedOption by viewModel.selectedType.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()
    val showOnlyFavorites by viewModel.showOnlyFavorites.collectAsStateWithLifecycle()

    LaunchedEffect(pets) {
        Log.d("PetListScreen", "Pets updated: ${pets.size} pets")
    }

    when {
        isLoading -> Loading()
        pets.isEmpty() && allPets.isEmpty() -> EmptyPets(addNewPetClicked = onPetAddClicked)
        else -> AdaptiveContent(
            modifier = modifier,
            pets = pets,
            isColumn = isColumn,
            isRefreshing = isRefreshing,
            addNewPetClicked = onPetAddClicked,
            onRemovePetClicked = { viewModel.removePet(it) },
            onPetClicked = onPetClicked,
            allPets = allPets,
            selectedOption = selectedOption,
            sortOrder = sortOrder,
            showOnlyFavorites = showOnlyFavorites,
            onSetSelectedType = { viewModel.setSelectedType(it) },
            onSetSortOrder = { viewModel.setSortOrder(it) },
            onChangeToggleFavorites = { viewModel.toggleShowOnlyFavorites() },
            toggleFavoriteChanged = { id, fav -> viewModel.toggleFavorite(id, fav) },
            onRefresh = { viewModel.refresh() },
            isScreenExpanded = isScreenExpanded
        )
    }
}


@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
private fun EmptyPets(modifier: Modifier = Modifier, addNewPetClicked: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PetHeader(text = "There are no pets in the list", Modifier, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        PetTitle(
            text = "Add first Pet",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .clickable { addNewPetClicked() }
        )
    }
}

@Composable
private fun AdaptiveContent(
    modifier: Modifier = Modifier,
    pets: List<Pet>,
    isColumn: Boolean,
    isRefreshing: Boolean,
    addNewPetClicked: () -> Unit,
    onRemovePetClicked: (Pet) -> Unit,
    onPetClicked: (Pet) -> Unit,
    allPets: List<Pet>,
    selectedOption: PetType,
    sortOrder: SortOrder,
    showOnlyFavorites: Boolean,
    onSetSelectedType: (PetType) -> Unit,
    onSetSortOrder: (SortOrder) -> Unit,
    onChangeToggleFavorites: () -> Unit,
    toggleFavoriteChanged: (Int, Boolean) -> Unit,
    onRefresh: () -> Unit,
    isScreenExpanded: Boolean,
) {
    if (isScreenExpanded) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp),
        ) {
            var selectedPet by remember { mutableStateOf(pets.first()) }
            Content(
                modifier = Modifier.weight(1.3f),
                pets = pets,
                isColumn = isColumn,
                isRefreshing = isRefreshing,
                addNewPetClicked = addNewPetClicked,
                onRemovePetClicked = onRemovePetClicked,
                onPetClicked = { selectedPet = it },
                allPets = allPets,
                selectedOption = selectedOption,
                sortOrder = sortOrder,
                showOnlyFavorites = showOnlyFavorites,
                onSetSelectedType = onSetSelectedType,
                onSetSortOrder = onSetSortOrder,
                onChangeToggleFavorites = onChangeToggleFavorites,
                toggleFavoriteChanged = toggleFavoriteChanged,
                onRefresh = onRefresh,
            )
            VerticalDivider(Modifier
                .width(1.dp)
                .fillMaxHeight(), color = ButtonColor)
            PetProfile(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                pet = selectedPet,
                showBackButton = false,
            )
        }
    } else {
        Content(
            modifier = modifier,
            pets = pets,
            isColumn = isColumn,
            isRefreshing = isRefreshing,
            addNewPetClicked = addNewPetClicked,
            onRemovePetClicked = onRemovePetClicked,
            onPetClicked = onPetClicked,
            allPets = allPets,
            selectedOption = selectedOption,
            sortOrder = sortOrder,
            showOnlyFavorites = showOnlyFavorites,
            onSetSelectedType = onSetSelectedType,
            onSetSortOrder = onSetSortOrder,
            onChangeToggleFavorites = onChangeToggleFavorites,
            toggleFavoriteChanged = toggleFavoriteChanged,
            onRefresh = onRefresh,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    pets: List<Pet>,
    isColumn: Boolean,
    isRefreshing: Boolean,
    addNewPetClicked: () -> Unit,
    onRemovePetClicked: (Pet) -> Unit,
    onPetClicked: (Pet) -> Unit,
    allPets: List<Pet>,
    selectedOption: PetType,
    sortOrder: SortOrder,
    showOnlyFavorites: Boolean,
    onSetSelectedType: (PetType) -> Unit,
    onSetSortOrder: (SortOrder) -> Unit,
    onChangeToggleFavorites: () -> Unit,
    toggleFavoriteChanged: (Int, Boolean) -> Unit,
    onRefresh: () -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 10.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val options by remember(allPets) {
                    derivedStateOf { listOf(PetType.ALL) + allPets.map { it.type }.toSet() }
                }
                var filterExpanded by remember { mutableStateOf(false) }
                val petCountText by remember(pets.size) {
                    mutableStateOf(
                        if (pets.size == 3) "There are 3 pets in the list"
                        else "Pets in the list: ${pets.size}"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) { PetHeader(text = "Animals", Modifier) }

                Spacer(Modifier.height(10.dp))
                PetTitle(text = petCountText, modifier = Modifier)
                Spacer(Modifier.height(10.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(options.size) { i ->
                        PetTypeOption(
                            modifier = Modifier,
                            text = options[i].name,
                            isSelected = selectedOption == options[i],
                            onClick = { onSetSelectedType(options[i]) }
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        Button(
                            onClick = { filterExpanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Sort: ${sortOrder.name}") }
                        DropdownMenu(
                            expanded = filterExpanded,
                            onDismissRequest = { filterExpanded = false }
                        ) {
                            DropdownMenuItem(text = { Text("None") }, onClick = {
                                onSetSortOrder(SortOrder.NONE); filterExpanded = false
                            })
                            DropdownMenuItem(text = { Text("By Name") }, onClick = {
                                onSetSortOrder(SortOrder.NAME); filterExpanded = false
                            })
                            DropdownMenuItem(text = { Text("By Age") }, onClick = {
                                onSetSortOrder(SortOrder.AGE); filterExpanded = false
                            })
                            DropdownMenuItem(text = { Text("By Weight") }, onClick = {
                                onSetSortOrder(SortOrder.WEIGHT); filterExpanded = false
                            })
                        }
                    }
                    Button(
                        onClick = onChangeToggleFavorites,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (showOnlyFavorites)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary
                        )
                    ) { Text(if (showOnlyFavorites) "★ Favorites" else "☆ All") }
                }

                Spacer(Modifier.height(10.dp))

                if (isColumn) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().testTag("pet_list_lazy_column"),
                        state = rememberLazyListState()
                    ) {
                        items(items = pets, key = { it.id }) { pet ->
                            AnimatedPetListItem(
                                pet = pet,
                                onRemoveClicked = { onRemovePetClicked(pet) },
                                onClick = { onPetClicked(pet) },
                                onOpenDetails = { onPetClicked(pet) },
                                onDelete = { onRemovePetClicked(pet) }
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize().testTag("pet_grid_lazy_vertical_grid"),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        state = rememberLazyGridState()
                    ) {
                        items(items = pets, key = { it.id }) { pet ->
                            AnimatedGridItem(
                                pet = pet,
                                onRemoveClicked = { onRemovePetClicked(pet) },
                                onClick = { onPetClicked(pet) },
                                onFavoriteClicked = {
                                    toggleFavoriteChanged(
                                        pet.id,
                                        pet.isFavorite
                                    )
                                }
                            )
                        }
                    }
                }
            }

            AddNewPetButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 32.dp, end = 16.dp)
                    .testTag("add_pet_button"),
                onClick = addNewPetClicked
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.AnimatedPetListItem(
    pet: Pet,
    onRemoveClicked: () -> Unit,
    onClick: () -> Unit,
    onOpenDetails: () -> Unit,
    onDelete: () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    var showContextMenu by remember { mutableStateOf(false) }

    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onRemoveClicked()
                true
            } else false
        }
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(800)) + slideInVertically(
            animationSpec = tween(800),
            initialOffsetY = { it / 2 }),
        exit = fadeOut(animationSpec = tween(800)) + shrinkVertically(animationSpec = tween(800))
    ) {
        Box {
            SwipeToDismissBox(
                state = swipeState,
                modifier = Modifier.animateItem(),
                enableDismissFromStartToEnd = false,
                enableDismissFromEndToStart = true,
                backgroundContent = {
                    val progress by animateFloatAsState(
                        targetValue = if (swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart) 1f else 0f,
                        label = "swipe_bg",
                        animationSpec = tween(durationMillis = 1000)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 2.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Red.copy(alpha = 0.1f + progress * 0.9f)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(end = 24.dp)
                                .size(32.dp)
                        )
                    }
                }
            ) {
                PetListItem(
                    animal = pet,
                    onRemoveClicked = onRemoveClicked,
                    onFavoriteClicked = null,
                    modifier = Modifier
                        .testTag("pet_item_${pet.id}")
                        .combinedClickable(
                            onClick = onClick,
                            onLongClick = { showContextMenu = true }
                        )
                )
            }

            // ── Контекстне меню ──
            DropdownMenu(
                expanded = showContextMenu,
                onDismissRequest = { showContextMenu = false }
            ) {
                // Дія 1: Відкрити деталі
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.More, // 👈 Іконка інформації
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text("Open Details") // 👈 Новий текст
                        }
                    },
                    onClick = {
                        onOpenDetails() // 👈 Викликаємо функцію відкриття деталей
                        showContextMenu = false
                    }
                )
                // Дія 2: Видалити
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text("Delete", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    onClick = {
                        onDelete()
                        showContextMenu = false
                    }
                )
            }
        }
    }
}

@Composable
private fun LazyGridItemScope.AnimatedGridItem(
    pet: Pet,
    onRemoveClicked: () -> Unit,
    onClick: () -> Unit,
    onFavoriteClicked: () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(350)) +
                scaleIn(
                    animationSpec = tween(350),
                    initialScale = 0.85f
                ),
        exit = fadeOut(animationSpec = tween(200)) +
                scaleOut(animationSpec = tween(200))
    ) {
        PetGridItem(
            animal = pet,
            onRemoveClicked = onRemoveClicked,
            onClick = onClick,
            onFavoriteClicked = onFavoriteClicked,
            modifier = Modifier.animateItem()
        )
    }
}

// ─────────────────────────────────────────────────────────────────
//  Add button
// ─────────────────────────────────────────────────────────────────

@Composable
private fun AddNewPetButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .size(48.dp)
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(24.dp))
            .clickable(
                onClick = onClick,
                role = Role.Button,
                onClickLabel = "Add new pet"
            )
            .semantics {
                contentDescription = "Add new pet"
            },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .size(24.dp)
                .padding(5.dp),
            painter = painterResource(R.drawable.ic_add),
            contentDescription = null, // decorative as the Box has the description
            contentScale = ContentScale.Fit,
        )
    }
}


@Preview
@Composable
private fun AdaptiveContentPreview() {
    PetCareTheme {
        AdaptiveContent(
            pets = MockPetRepository.getAllPets(),
            isColumn = true,
            isRefreshing = false,
            addNewPetClicked = {},
            onRemovePetClicked = {},
            onPetClicked = {},
            allPets = MockPetRepository.getAllPets(),
            selectedOption = PetType.ALL,
            sortOrder = SortOrder.NONE,
            showOnlyFavorites = false,
            onSetSelectedType = {},
            onSetSortOrder = {},
            onChangeToggleFavorites = {},
            toggleFavoriteChanged = { _, _ -> },
            onRefresh = {},
            isScreenExpanded = false
        )
    }
}

@Preview(widthDp = 1200, heightDp = 800, showBackground = true)
@Composable
private fun AdaptiveContentExpandedPreview() {
    PetCareTheme {
        AdaptiveContent(
            pets = MockPetRepository.getAllPets(),
            isColumn = true,
            isRefreshing = false,
            addNewPetClicked = {},
            onRemovePetClicked = {},
            onPetClicked = {},
            allPets = MockPetRepository.getAllPets(),
            selectedOption = PetType.ALL,
            sortOrder = SortOrder.NONE,
            showOnlyFavorites = false,
            onSetSelectedType = {},
            onSetSortOrder = {},
            onChangeToggleFavorites = {},
            toggleFavoriteChanged = { _, _ -> },
            onRefresh = {},
            isScreenExpanded = true
        )
    }
}