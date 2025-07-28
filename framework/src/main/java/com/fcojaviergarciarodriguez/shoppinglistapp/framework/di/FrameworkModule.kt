package com.fcojaviergarciarodriguez.shoppinglistapp.framework.di

import android.app.Application
import androidx.room.Room
import com.fcojaviergarciarodriguez.shoppinglistapp.data.datasource.LocalDataSource
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.dao.ShoppingListDao
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.database.AppDatabase
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.datasource.RoomLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FrameworkModule {

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        roomLocalDataSource: RoomLocalDataSource
    ): LocalDataSource

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(app: Application): AppDatabase {
            return Room.databaseBuilder(
                app,
                AppDatabase::class.java,
                "shopping_list_database"
            ).build()
        }

        @Provides
        @Singleton
        fun provideShoppingListDao(database: AppDatabase): ShoppingListDao {
            return database.shoppingListDao()
        }
    }
} 