package com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import kotlinx.coroutines.flow.Flow

interface ShoppingItemRepository {
    fun getItemsForList(listId: Int): Flow<List<ItemModel>>
    suspend fun addItem(listId: Int, itemName: String)
    suspend fun updateItem(item: ItemModel)
} 