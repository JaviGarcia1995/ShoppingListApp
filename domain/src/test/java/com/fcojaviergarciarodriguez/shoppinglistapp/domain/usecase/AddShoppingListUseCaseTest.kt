package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.fake.FakeShoppingListRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unitary test for AddShoppingListUseCase usando Detroit School (Fakes)
 * 
 * What to test:
 * - Real behaviour of the Use Case
 * - List created with correct name
 * - Entry validation
 */
class AddShoppingListUseCaseTest {
    private val fakeRepository = FakeShoppingListRepository()
    private lateinit var useCase: AddShoppingListUseCase

    @Before
    fun setUp() {
        fakeRepository.clearData()
        useCase = AddShoppingListUseCase(fakeRepository)
    }

    @Test
    fun `GIVEN valid name WHEN invoke THEN creates shopping list with correct name`() = runTest {
        // Arrange
        val listName = "My Shopping List"

        // Act
        useCase.invoke(listName)

        // Assert - Verify the list was created with the correct name
        val savedLists = fakeRepository.getShoppingLists().first()
        assertEquals(1, savedLists.size)
        assertEquals(listName, savedLists[0].name)
        assertTrue(savedLists[0].id > 0)
        assertTrue(savedLists[0].items.isEmpty())
    }

    @Test
    fun `GIVEN empty name WHEN invoke THEN throws IllegalArgumentException and creates no list`() = runTest {
        // Arrange
        val emptyName = ""

        // Act & Assert
        try {
            useCase.invoke(emptyName)
            assert(false) { "Expected IllegalArgumentException but no exception was thrown" }
        } catch (e: IllegalArgumentException) {
            assertEquals("Shopping list name cannot be empty", e.message)
        }

        // Verify no list was created - Detroit School verifica el resultado
        val savedLists = fakeRepository.getShoppingLists().first()
        assertEquals(0, savedLists.size)
    }

    @Test
    fun `GIVEN blank name WHEN invoke THEN throws IllegalArgumentException and creates no list`() = runTest {
        // Arrange
        val blankName = "   "

        // Act & Assert
        try {
            useCase.invoke(blankName)
            assert(false) { "Expected IllegalArgumentException but no exception was thrown" }
        } catch (e: IllegalArgumentException) {
            assertEquals("Shopping list name cannot be empty", e.message)
        }

        // Verify no list was created
        val savedLists = fakeRepository.getShoppingLists().first()
        assertEquals(0, savedLists.size)
    }

    @Test
    fun `GIVEN name with whitespace WHEN invoke THEN creates list with trimmed name`() = runTest {
        // Arrange
        val nameWithSpaces = "   List with blanks   "
        val expectedTrimmed = "List with blanks"

        // Act
        useCase.invoke(nameWithSpaces)

        // Assert - Verify the list was created with the trimmed name
        val savedLists = fakeRepository.getShoppingLists().first()
        assertEquals(1, savedLists.size)
        assertEquals(expectedTrimmed, savedLists[0].name)
    }

    @Test
    fun `GIVEN multiple lists WHEN invoke THEN adds new list without affecting existing ones`() = runTest {
        // Arrange - Prepare some pre-existing data
        fakeRepository.addTestData(listOf(
            com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel(
                id = 1, 
                name = "Existing List",
                items = emptyList()
            )
        ))

        // Act
        useCase.invoke("New List")

        // Assert - Verify the new list was added without affecting existing ones
        val savedLists = fakeRepository.getShoppingLists().first()
        assertEquals(2, savedLists.size)
        
        // Verify the existing list is still present
        assertTrue(savedLists.any { it.name == "Existing List" })
        
        // Verify the new list was added
        assertTrue(savedLists.any { it.name == "New List" })
    }
} 