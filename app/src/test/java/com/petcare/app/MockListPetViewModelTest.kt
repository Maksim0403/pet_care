package com.petcare.app

import com.petcare.app.data.MockPetRepository
import com.petcare.app.data.SettingsDataStore
import com.petcare.app.models.Cat
import com.petcare.app.models.PetType
import com.petcare.app.models.SortOrder
import com.petcare.app.pet_list.MockPetListViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MockPetListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeSettingsDataStore: SettingsDataStore = mockk()
    private lateinit var viewModel: MockPetListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { fakeSettingsDataStore.sortMode } returns flowOf("none")
        runTest {
            MockPetRepository.refresh()
        }
        viewModel = MockPetListViewModel(fakeSettingsDataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun TestScope.activateFlows() {
        backgroundScope.launch { viewModel.filteredPets.collect {} }
        backgroundScope.launch { viewModel.allPets.collect {} }
        advanceUntilIdle()
    }

    // ПОЗИТИВНІ СЦЕНАРІЇ
    // -------------------------------------------------------------------------

    @Test
    fun `filterByType CAT returns only cats`() = runTest {
        activateFlows()

        viewModel.setSelectedType(PetType.CAT)
        activateFlows()

        val result = viewModel.filteredPets.value
        assertTrue("Result should not be empty", result.isNotEmpty())
        assertTrue("All pets should be CAT type", result.all { it.type == PetType.CAT })
    }

    @Test
    fun `sortByName returns pets in alphabetical order`() = runTest {
        // Arrange
        activateFlows()

        // Act
        viewModel.setSelectedType(PetType.ALL)
        viewModel.setSortOrder(SortOrder.NAME)
        activateFlows()

        // Assert
        val names = viewModel.filteredPets.value.map { it.name }
        assertEquals("Names should be sorted alphabetically", names.sorted(), names)
    }

    @Test
    fun `sortByAge returns pets sorted ascending`() = runTest {
        // Arrange
        activateFlows()

        // Act
        viewModel.setSortOrder(SortOrder.AGE)
        activateFlows()

        // Assert
        val ages = viewModel.filteredPets.value.map { it.age }
        assertEquals("Ages should be sorted ascending", ages.sorted(), ages)
    }

    @Test
    fun `sortByWeight returns pets sorted ascending`() = runTest {
        // Arrange
        activateFlows()

        // Act
        viewModel.setSortOrder(SortOrder.WEIGHT)
        activateFlows()

        // Assert
        val weights = viewModel.filteredPets.value.map { it.weight }
        assertEquals("Weights should be sorted ascending", weights.sorted(), weights)
    }

    @Test
    fun `toggleFavorite marks pet as favorite`() = runTest {
        // Arrange
        activateFlows()
        val pet = viewModel.filteredPets.value.first()
        val initialState = pet.isFavorite

        // Act
        viewModel.toggleFavorite(pet.id, initialState)
        activateFlows()

        // Assert
        val updated = viewModel.filteredPets.value.firstOrNull { it.id == pet.id }
        assertEquals("Favorite state should be toggled", !initialState, updated?.isFavorite)
    }

    @Test
    fun `removePet removes pet from list`() = runTest {
        // Arrange
        activateFlows()
        val petToRemove = viewModel.filteredPets.value.first()
        val sizeBefore = viewModel.filteredPets.value.size

        // Act
        viewModel.removePet(petToRemove)
        activateFlows()

        // Assert
        assertEquals("List size should decrease by 1", sizeBefore - 1, viewModel.filteredPets.value.size)
        assertFalse(
            "Removed pet should not be in list",
            viewModel.filteredPets.value.any { it.id == petToRemove.id }
        )
    }

    @Test
    fun `refresh restores default list`() = runTest {
        // Arrange
        activateFlows()
        val petToRemove = viewModel.filteredPets.value.first()
        viewModel.removePet(petToRemove)
        activateFlows()

        // Act
        viewModel.refresh()
        activateFlows()

        // Assert
        assertTrue(
            "Removed pet should be back after refresh",
            viewModel.filteredPets.value.any { it.id == petToRemove.id }
        )
    }

    // НЕГАТИВНІ СЦЕНАРІЇ
    // -------------------------------------------------------------------------

    @Test
    fun `filterByDog returns empty list after all dogs removed`() = runTest {
        // Arrange
        activateFlows()
        val dogs = viewModel.allPets.value.filter { it.type == PetType.DOG }
        dogs.forEach { viewModel.removePet(it) }
        activateFlows()

        // Act
        viewModel.setSelectedType(PetType.DOG)
        activateFlows()

        // Assert
        assertTrue("Filtered dog list should be empty", viewModel.filteredPets.value.isEmpty())
    }

    @Test
    fun `showOnlyFavorites with no favorites returns empty list`() = runTest {
        // Arrange
        activateFlows()

        // Act
        viewModel.toggleShowOnlyFavorites()
        activateFlows()

        // Assert
        assertTrue(
            "No favorites should result in empty list",
            viewModel.filteredPets.value.isEmpty()
        )
    }

    @Test
    fun `removePet with nonexistent id does not change list size`() = runTest {
        // Arrange
        activateFlows()
        val sizeBefore = viewModel.filteredPets.value.size
        val ghostPet = Cat(id = 9999, name = "Ghost", age = 0, weight = 0.0, breed = "Unknown")

        // Act
        viewModel.removePet(ghostPet)
        activateFlows()

        // Assert
        assertEquals("List size should not change", sizeBefore, viewModel.filteredPets.value.size)
    }

    // EDGE CASES
    // -------------------------------------------------------------------------

    @Test
    fun `filterByAll with sortNone returns full unmodified list`() = runTest {
        // Arrange
        activateFlows()
        val totalSize = viewModel.allPets.value.size

        // Act
        viewModel.setSelectedType(PetType.ALL)
        viewModel.setSortOrder(SortOrder.NONE)
        activateFlows()

        // Assert
        assertEquals(
            "Filtered size should equal total size",
            totalSize,
            viewModel.filteredPets.value.size
        )
    }

    @Test
    fun `toggleShowOnlyFavorites twice restores original list size`() = runTest {
        // Arrange
        activateFlows()
        val sizeBefore = viewModel.filteredPets.value.size

        // Act
        viewModel.toggleShowOnlyFavorites()
        activateFlows()
        viewModel.toggleShowOnlyFavorites()
        activateFlows()

        // Assert
        assertEquals(
            "List size should be restored after double toggle",
            sizeBefore,
            viewModel.filteredPets.value.size
        )
    }

    @Test
    fun `isRefreshing is false after refresh completes`() = runTest {
        // Arrange
        activateFlows()

        // Act
        viewModel.refresh()
        advanceUntilIdle()

        // Assert
        assertFalse("isRefreshing should be false after refresh", viewModel.isRefreshing.value)
    }

    @Test
    fun `isLoading is false after initial load`() = runTest {
        // Act
        advanceUntilIdle()

        // Assert
        assertFalse("isLoading should be false after init", viewModel.isLoading.value)
    }

    @Test
    fun `combined CAT filter and showOnlyFavorites returns only favorite cats`() = runTest {
        // Arrange
        activateFlows()
        val firstCat = viewModel.allPets.value.first { it.type == PetType.CAT }
        viewModel.toggleFavorite(firstCat.id, false)
        activateFlows()

        // Act
        viewModel.setSelectedType(PetType.CAT)
        viewModel.toggleShowOnlyFavorites()
        activateFlows()

        // Assert
        val result = viewModel.filteredPets.value
        assertTrue("All results should be CAT", result.all { it.type == PetType.CAT })
        assertTrue("All results should be favorites", result.all { it.isFavorite })
    }
}
