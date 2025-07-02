package com.fcojaviergarciarodriguez.shoppinglistapp.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.dao.ShoppingListDao

@Database(
    entities = [ShoppingListEntity::class, ItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
} 