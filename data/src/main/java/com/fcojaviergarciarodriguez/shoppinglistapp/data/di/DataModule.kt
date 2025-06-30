package com.fcojaviergarciarodriguez.shoppinglistapp.data.di

import com.fcojaviergarciarodriguez.shoppinglistapp.data.datasource.LocalDataSource
import com.fcojaviergarciarodriguez.shoppinglistapp.data.repository.ShoppingItemRepositoryImpl
import com.fcojaviergarciarodriguez.shoppinglistapp.data.repository.ShoppingListRepositoryImpl
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingItemRepository
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideShoppingListRepository(localDataSource: LocalDataSource): ShoppingListRepository {
        return ShoppingListRepositoryImpl(localDataSource)
    }

    @Provides
    @Singleton
    fun provideShoppingItemRepository(localDataSource: LocalDataSource): ShoppingItemRepository {
        return ShoppingItemRepositoryImpl(localDataSource)
    }
} 