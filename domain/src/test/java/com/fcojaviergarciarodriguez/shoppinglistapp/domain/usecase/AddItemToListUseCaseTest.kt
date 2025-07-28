package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.fake.FakeShoppingItemRepository
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit test for AddItemToListUseCase using Detroit School (Fakes)
 * 
 * This Use Case has more validations than the previous one:
 * - listId validation (must be > 0)
 * - itemName validation (cannot be empty)
 * - Data transformation (trim name)
 */
class AddItemToListUseCaseTest {

    private val fakeRepository = FakeShoppingItemRepository()
    private lateinit var useCase: AddItemToListUseCase

    @Before
    fun setUp() {
        fakeRepository.clearData()
        useCase = AddItemToListUseCase(fakeRepository)
    }

    @Test
    fun `GIVEN valid parameters WHEN invoke THEN creates item with correct data`() = runTest {
        // Arrange
        val listId = 1
        val itemName = "Milk"

        // Act
        useCase.invoke(listId, itemName)

        // Assert - Verify the item was created with correct data
        val savedItems = fakeRepository.getItemsForList(listId).first()
        assertEquals(1, savedItems.size)
        
        val createdItem = savedItems[0]
        assertEquals(listId, createdItem.listId)
        assertEquals(itemName, createdItem.name)
        assertFalse(createdItem.isChecked) // New item should be unchecked
        assertTrue(createdItem.id > 0) // Should have a valid ID
    }

    @Test
    fun `GIVEN invalid listId WHEN invoke THEN throws IllegalArgumentException and creates no item`() = runTest {
        // Arrange
        val invalidListId = 0
        val itemName = "Milk"

        // Act & Assert
        try {
            useCase.invoke(invalidListId, itemName)
            assert(false) { "Expected IllegalArgumentException but no exception was thrown" }
        } catch (e: IllegalArgumentException) {
            assertEquals("Shopping list ID must be greater than 0", e.message)
        }

        // Verify no item was created
        val savedItems = fakeRepository.getItemsForList(invalidListId).first()
        assertEquals(0, savedItems.size)
    }

    @Test
    fun `GIVEN negative listId WHEN invoke THEN throws IllegalArgumentException`() = runTest {
        // Arrange
        val negativeListId = -1
        val itemName = "Milk"

        // Act & Assert
        try {
            useCase.invoke(negativeListId, itemName)
            assert(false) { "Expected IllegalArgumentException but no exception was thrown" }
        } catch (e: IllegalArgumentException) {
            assertEquals("Shopping list ID must be greater than 0", e.message)
        }

        // Verify no item was created
        val savedItems = fakeRepository.getItemsForList(negativeListId).first()
        assertEquals(0, savedItems.size)
    }

    @Test
    fun `GIVEN empty itemName WHEN invoke THEN throws IllegalArgumentException and creates no item`() = runTest {
        // Arrange
        val listId = 1
        val emptyItemName = ""

        // Act & Assert
        try {
            useCase.invoke(listId, emptyItemName)
            assert(false) { "Expected IllegalArgumentException but no exception was thrown" }
        } catch (e: IllegalArgumentException) {
            assertEquals("Item name cannot be empty", e.message)
        }

        // Verify no item was created
        val savedItems = fakeRepository.getItemsForList(listId).first()
        assertEquals(0, savedItems.size)
    }

    @Test
    fun `GIVEN blank itemName WHEN invoke THEN throws IllegalArgumentException and creates no item`() = runTest {
        // Arrange
        val listId = 1
        val blankItemName = "   "

        // Act & Assert
        try {
            useCase.invoke(listId, blankItemName)
            assert(false) { "Expected IllegalArgumentException but no exception was thrown" }
        } catch (e: IllegalArgumentException) {
            assertEquals("Item name cannot be empty", e.message)
        }

        // Verify no item was created
        val savedItems = fakeRepository.getItemsForList(listId).first()
        assertEquals(0, savedItems.size)
    }

    @Test
    fun `GIVEN itemName with whitespace WHEN invoke THEN creates item with trimmed name`() = runTest {
        // Arrange
        val listId = 1
        val itemNameWithSpaces = "   Whole Milk   "
        val expectedTrimmed = "Whole Milk"

        // Act
        useCase.invoke(listId, itemNameWithSpaces)

        // Assert - Verify it was saved with the trimmed name
        val savedItems = fakeRepository.getItemsForList(listId).first()
        assertEquals(1, savedItems.size)
        assertEquals(expectedTrimmed, savedItems[0].name)
    }

    @Test
    fun `GIVEN multiple items WHEN invoke THEN adds new item without affecting existing ones`() = runTest {
        // Arrange - Prepare existing data
        val listId = 1
        fakeRepository.addTestData(listOf(
            ItemModel(id = 1, listId = listId, name = "Bread", isChecked = false),
            ItemModel(id = 2, listId = listId, name = "Butter", isChecked = true)
        ))

        // Act
        useCase.invoke(listId, "Milk")

        // Assert - Verify it was added without affecting existing ones
        val savedItems = fakeRepository.getItemsForList(listId).first()
        assertEquals(3, savedItems.size)
        
        // Verify existing items are still there
        assertTrue(savedItems.any { it.name == "Bread" && !it.isChecked })
        assertTrue(savedItems.any { it.name == "Butter" && it.isChecked })
        
        // Verify the new item was added
        assertTrue(savedItems.any { it.name == "Milk" && !it.isChecked })
    }

    @Test
    fun `GIVEN items in different lists WHEN invoke THEN adds item only to specified list`() = runTest {
        // Arrange - Prepare items in different lists
        fakeRepository.addTestData(listOf(
            ItemModel(id = 1, listId = 1, name = "Bread", isChecked = false),
            ItemModel(id = 2, listId = 2, name = "Soap", isChecked = false)
        ))

        // Act - Add item to list 1
        useCase.invoke(1, "Milk")

        // Assert - Verify it was added only to list 1
        val itemsList1 = fakeRepository.getItemsForList(1).first()
        val itemsList2 = fakeRepository.getItemsForList(2).first()
        
        assertEquals(2, itemsList1.size) // Bread + Milk
        assertEquals(1, itemsList2.size) // Only Soap
        
        assertTrue(itemsList1.any { it.name == "Milk" })
        assertFalse(itemsList2.any { it.name == "Milk" })
    }
} 