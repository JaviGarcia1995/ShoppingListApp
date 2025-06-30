package com.fcojaviergarciarodriguez.shoppinglistapp.data.datasource

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getShoppingLists(): Flow<List<ShoppingListModel>>
    suspend fun insertShoppingList(list: ShoppingListModel): Long
    fun getShoppingListById(listId: Int): Flow<ShoppingListModel?>
    fun getItemsForList(listId: Int): Flow<List<ItemModel>>
    suspend fun insertItem(item: ItemModel): Long
} 