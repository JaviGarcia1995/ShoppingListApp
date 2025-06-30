package com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun getShoppingLists(): Flow<List<ShoppingListModel>>
    fun getShoppingList(id: Int): Flow<ShoppingListModel?>
    suspend fun addShoppingList(name: String)
} 