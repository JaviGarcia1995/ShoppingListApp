package com.fcojaviergarciarodriguez.shoppinglistapp.framework.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.dao.ShoppingListDao

@Database(
    entities = [ShoppingListEntity::class, ItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private const val DATABASE_NAME = "shoppingList_database"

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                    ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 