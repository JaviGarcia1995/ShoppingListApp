package com.fcojaviergarciarodriguez.shoppinglistapp.framework.datasource

import com.fcojaviergarciarodriguez.shoppinglistapp.data.datasource.LocalDataSource
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.dao.ShoppingListDao
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.mapper.toItemEntity
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.mapper.toItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.mapper.toShoppingListEntity
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.mapper.toShoppingListModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalDataSource @Inject constructor(private val shoppingListDao: ShoppingListDao) : LocalDataSource {
    override fun getShoppingLists(): Flow<List<ShoppingListModel>> {
        return shoppingListDao.getShoppingLists().map { list ->
            list.map { it.toShoppingListModel(emptyList()) }
        }
    }

    override suspend fun insertShoppingList(list: ShoppingListModel): Long {
        return shoppingListDao.insertShoppingList(list.toShoppingListEntity())
    }

    override fun getShoppingListById(listId: Int): Flow<ShoppingListModel?> {
        return shoppingListDao.getShoppingListById(listId).map { it?.toShoppingListModel(emptyList()) }
    }

    override fun getItemsForList(listId: Int): Flow<List<ItemModel>> {
        return shoppingListDao.getItemsForList(listId).map { list ->
            list.map { it.toItemModel() }
        }
    }

    override suspend fun insertItem(item: ItemModel): Long {
        return shoppingListDao.insertItem(item.toItemEntity())
    }

    override suspend fun deleteItem(item: ItemModel) {
        shoppingListDao.deleteItemById(item.id)
    }

    override suspend fun deleteShoppingList(shoppingList: ShoppingListModel) {
        shoppingListDao.deleteShoppingListById(shoppingList.id)
    }
} 