package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.fake.FakeShoppingListRepository
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit test for GetShoppingListsUseCase using Detroit School (Fakes).
 *
 * What to test:
 * - When no lists exist, it returns an empty list.
 * - When lists exist, it returns all of them correctly.
 */
class GetShoppingListsUseCaseTest {

    private val fakeRepository = FakeShoppingListRepository()
    private lateinit var useCase: GetShoppingListsUseCase

    @Before
    fun setUp() {
        fakeRepository.clearData()
        useCase = GetShoppingListsUseCase(fakeRepository)
    }

    @Test
    fun `GIVEN no lists WHEN invoke THEN returns empty list`() = runTest {
        // Arrange (no data added)

        // Act
        val lists = useCase.invoke().first()

        // Assert
        assertTrue("The list of shopping lists should be empty", lists.isEmpty())
    }

    @Test
    fun `GIVEN multiple lists WHEN invoke THEN returns all lists`() = runTest {
        // Arrange
        val testData = listOf(
            ShoppingListModel(id = 1, name = "Groceries", items = emptyList()),
            ShoppingListModel(id = 2, name = "Hardware Store", items = emptyList())
        )
        fakeRepository.addTestData(testData)

        // Act
        val lists = useCase.invoke().first()

        // Assert
        assertEquals("Should return the correct number of lists", 2, lists.size)
        assertEquals("The first list should match the test data", testData[0], lists[0])
        assertEquals("The second list should match the test data", testData[1], lists[1])
    }
} 