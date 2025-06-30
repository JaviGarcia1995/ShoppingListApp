package com.fcojaviergarciarodriguez.shoppinglistapp.data.repository

import com.fcojaviergarciarodriguez.shoppinglistapp.data.datasource.LocalDataSource
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingItemRepository
import kotlinx.coroutines.flow.Flow

class ShoppingItemRepositoryImpl(
    private val localDataSource: LocalDataSource
) : ShoppingItemRepository {

    override fun getItemsForList(listId: Int): Flow<List<ItemModel>> =
        localDataSource.getItemsForList(listId)

    override suspend fun addItem(listId: Int, itemName: String) {
        localDataSource.insertItem(
            ItemModel(
                id = 0,
                listId = listId,
                name = itemName,
                isChecked = false
            )
        )
    }

    override suspend fun updateItem(item: ItemModel) {
        localDataSource.insertItem(item)
    }
} 