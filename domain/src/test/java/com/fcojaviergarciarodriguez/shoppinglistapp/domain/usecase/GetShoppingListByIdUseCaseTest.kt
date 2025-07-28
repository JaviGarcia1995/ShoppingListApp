package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.fake.FakeShoppingListRepository
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Unit test for GetShoppingListByIdUseCase using Detroit School (Fakes).
 *
 * What to test:
 * - When a list with the given ID exists, it returns the correct list.
 * - When a list with the given ID does not exist, it returns null.
 * - It returns the correct list even when other lists are present.
 */
class GetShoppingListByIdUseCaseTest {
    private val fakeRepository = FakeShoppingListRepository()
    private lateinit var useCase: GetShoppingListByIdUseCase

    @Before
    fun setUp() {
        // Arrange: prepare test data before each test
        fakeRepository.clearData()
        val testData = listOf(
            ShoppingListModel(id = 1, name = "Groceries", items = listOf(ItemModel(1, 1, "Milk", false))),
            ShoppingListModel(id = 2, name = "Hardware Store", items = emptyList())
        )
        fakeRepository.addTestData(testData)
        useCase = GetShoppingListByIdUseCase(fakeRepository)
    }

    @Test
    fun `GIVEN existing listId WHEN invoke THEN returns correct shopping list`() = runTest {
        // Arrange
        val targetId = 1
        val expectedName = "Groceries"

        // Act
        val result = useCase.invoke(targetId).first()

        // Assert
        assertNotNull("Result should not be null for an existing ID", result)
        assertEquals("Should return the list with the correct ID", targetId, result?.id)
        assertEquals("The name of the returned list should be correct", expectedName, result?.name)
        assertEquals("The items in the list should be correct", 1, result?.items?.size)
    }

    @Test
    fun `GIVEN non-existing listId WHEN invoke THEN returns null`() = runTest {
        // Arrange
        val nonExistingId = 99

        // Act
        val result = useCase.invoke(nonExistingId).first()

        // Assert
        assertNull("Result should be null for a non-existing ID", result)
    }

    @Test
    fun `GIVEN listId of zero WHEN invoke THEN throws IllegalArgumentException`() = runTest {
        // Arrange
        val zeroId = 0

        // Act & Assert
        try {
            // We need to collect the flow to trigger the exception
            useCase.invoke(zeroId).first() 
            assert(false) { "Expected IllegalArgumentException but no exception was thrown" }
        } catch (e: IllegalArgumentException) {
            assertEquals("Shopping list ID must be greater than 0", e.message)
        }
    }
} 