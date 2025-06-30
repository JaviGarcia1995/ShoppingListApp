package com.fcojaviergarciarodriguez.shoppinglistapp.data.repository

import com.fcojaviergarciarodriguez.shoppinglistapp.data.datasource.LocalDataSource
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingListRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class ShoppingListRepositoryImpl(
    private val localDataSource: LocalDataSource
) : ShoppingListRepository {

    override fun getShoppingLists(): Flow<List<ShoppingListModel>> =
        localDataSource.getShoppingLists().flatMapLatest { lists ->
            if (lists.isEmpty()) {
                flowOf(emptyList())
            } else {
                val flows = lists.map { list ->
                    getItemsForList(list.id).map { items ->
                        list.copy(items = items)
                    }
                }
                combine(flows) { it.toList() }
            }
        }

    override fun getShoppingList(id: Int): Flow<ShoppingListModel?> =
        localDataSource.getShoppingListById(id).flatMapLatest { list ->
            if (list == null) {
                flowOf(null)
            } else {
                getItemsForList(id).map { items ->
                    list.copy(items = items)
                }
            }
        }

    override suspend fun addShoppingList(name: String) {
        localDataSource.insertShoppingList(
            ShoppingListModel(id = 0, name = name, items = emptyList())
        )
    }

    private fun getItemsForList(listId: Int): Flow<List<ItemModel>> =
        localDataSource.getItemsForList(listId)
}