package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a specific shopping list by its ID.
 * This encapsulates the business logic for getting a shopping list with its items.
 */
class GetShoppingListByIdUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) {
    /**
     * Retrieves a shopping list by its ID.
     * @param listId The ID of the shopping list to retrieve
     * @return Flow of ShoppingListModel or null if not found
     * @throws IllegalArgumentException if the listId is invalid
     */
    operator fun invoke(listId: Int): Flow<ShoppingListModel?> {
        // Business logic: Validate input
        if (listId <= 0) {
            throw IllegalArgumentException("Shopping list ID must be greater than 0")
        }
        
        return shoppingListRepository.getShoppingList(listId)
    }
} 