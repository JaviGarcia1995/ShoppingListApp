package com.fcojaviergarciarodriguez.shoppinglistapp.data.di

import com.fcojaviergarciarodriguez.shoppinglistapp.data.repository.ShoppingItemRepositoryImpl
import com.fcojaviergarciarodriguez.shoppinglistapp.data.repository.ShoppingListRepositoryImpl
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingItemRepository
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindShoppingListRepository(
        shoppingListRepositoryImpl: ShoppingListRepositoryImpl
    ): ShoppingListRepository

    @Binds
    @Singleton
    abstract fun bindShoppingItemRepository(
        shoppingItemRepositoryImpl: ShoppingItemRepositoryImpl
    ): ShoppingItemRepository
} 