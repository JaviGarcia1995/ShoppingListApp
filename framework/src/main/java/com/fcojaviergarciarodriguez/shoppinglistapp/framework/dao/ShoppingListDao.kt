package com.fcojaviergarciarodriguez.shoppinglistapp.framework.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.database.ItemEntity
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.database.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_lists ORDER BY name ASC")
    fun getShoppingLists(): Flow<List<ShoppingListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingList(list: ShoppingListEntity): Long

    @Query("SELECT * FROM shopping_lists WHERE id = :listId")
    fun getShoppingListById(listId: Int): Flow<ShoppingListEntity?>

    @Query("SELECT * FROM items WHERE listId = :listId ORDER BY id ASC")
    fun getItemsForList(listId: Int): Flow<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity): Long

    @Query("DELETE FROM items WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Int)
} 