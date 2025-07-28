package com.fcojaviergarciarodriguez.shoppinglistapp.domain.fake

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Fake implementation of ShoppingListRepository for Detroit School tests.
 * 
 * This implementation:
 * - Works for real (not a mock)
 * - Stores data in memory
 * - Is simple and predictable
 * - Allows verifying real behaviors
 */
class FakeShoppingListRepository : ShoppingListRepository {
    
    // In-memory storage - simulates a database
    private val _shoppingLists = MutableStateFlow<List<ShoppingListModel>>(emptyList())
    private var nextId = 1
    
    // Method to clear data between tests
    fun clearData() {
        _shoppingLists.value = emptyList()
        nextId = 1
    }
    
    // Helper method to add test data
    fun addTestData(lists: List<ShoppingListModel>) {
        _shoppingLists.value = lists
        nextId = (lists.maxOfOrNull { it.id } ?: 0) + 1
    }
    
    override fun getShoppingLists(): Flow<List<ShoppingListModel>> {
        return _shoppingLists
    }
    
    override fun getShoppingList(id: Int): Flow<ShoppingListModel?> {
        return _shoppingLists.map { lists ->
            lists.find { it.id == id }
        }
    }
    
    override suspend fun addShoppingList(name: String) {
        val currentLists = _shoppingLists.value.toMutableList()
        val newList = ShoppingListModel(
            id = nextId++,
            name = name,
            items = emptyList()
        )
        currentLists.add(newList)
        _shoppingLists.value = currentLists
    }
    
    override suspend fun deleteShoppingList(shoppingList: ShoppingListModel) {
        val currentLists = _shoppingLists.value.toMutableList()
        currentLists.removeAll { it.id == shoppingList.id }
        _shoppingLists.value = currentLists
    }
} 