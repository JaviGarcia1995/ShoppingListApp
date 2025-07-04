package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingListRepository
import javax.inject.Inject

/**
 * Use case for deleting a shopping list.
 * This encapsulates the business logic for deleting shopping lists including validation.
 * Note: This will also delete all associated items due to CASCADE configuration in the database.
 */
class DeleteShoppingListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) {
    /**
     * Deletes the specified shopping list and all its associated items.
     * @param shoppingList The shopping list to delete
     * @throws IllegalArgumentException if the shopping list ID is invalid
     */
    suspend operator fun invoke(shoppingList: ShoppingListModel) {
        // Business logic: Validate input
        if (shoppingList.id <= 0) {
            throw IllegalArgumentException("Invalid shopping list ID")
        }
        
        // Delegate to repository
        shoppingListRepository.deleteShoppingList(shoppingList)
    }
} 