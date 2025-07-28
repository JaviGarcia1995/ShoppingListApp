package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingItemRepository
import javax.inject.Inject

/**
 * Use case for adding an item to a shopping list.
 * This encapsulates the business logic for creating items including validation.
 */
class AddItemToListUseCase @Inject constructor(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    /**
     * Adds a new item to the specified shopping list.
     * @param listId The ID of the shopping list to add the item to
     * @param itemName The name of the item to add
     * @throws IllegalArgumentException if the listId is invalid or itemName is blank
     */
    suspend operator fun invoke(listId: Int, itemName: String) {
        // Business logic: Validate inputs
        if (listId <= 0) {
            throw IllegalArgumentException("Shopping list ID must be greater than 0")
        }
        
        if (itemName.isBlank()) {
            throw IllegalArgumentException("Item name cannot be empty")
        }
        
        // Delegate to repository
        shoppingItemRepository.addItem(listId, itemName.trim())
    }
} 