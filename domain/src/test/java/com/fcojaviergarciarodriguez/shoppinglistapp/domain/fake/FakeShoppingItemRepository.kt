package com.fcojaviergarciarodriguez.shoppinglistapp.domain.fake

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Fake implementation of ShoppingItemRepository for Detroit School tests.
 */
class FakeShoppingItemRepository : ShoppingItemRepository {
    
    // In-memory storage
    private val _items = MutableStateFlow<List<ItemModel>>(emptyList())
    private var nextId = 1
    
    // Method to clear data between tests
    fun clearData() {
        _items.value = emptyList()
        nextId = 1
    }
    
    // Helper method to add test data
    fun addTestData(items: List<ItemModel>) {
        _items.value = items
        nextId = (items.maxOfOrNull { it.id } ?: 0) + 1
    }
    
    override fun getItemsForList(listId: Int): Flow<List<ItemModel>> {
        return _items.map { items ->
            items.filter { it.listId == listId }
        }
    }
    
    override suspend fun addItem(listId: Int, itemName: String) {
        val currentItems = _items.value.toMutableList()
        val newItem = ItemModel(
            id = nextId++,
            listId = listId,
            name = itemName,
            isChecked = false
        )
        currentItems.add(newItem)
        _items.value = currentItems
    }
    
    override suspend fun updateItem(item: ItemModel) {
        val currentItems = _items.value.toMutableList()
        val index = currentItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            currentItems[index] = item
            _items.value = currentItems
        }
    }
    
    override suspend fun deleteItem(item: ItemModel) {
        val currentItems = _items.value.toMutableList()
        currentItems.removeAll { it.id == item.id }
        _items.value = currentItems
    }
} 