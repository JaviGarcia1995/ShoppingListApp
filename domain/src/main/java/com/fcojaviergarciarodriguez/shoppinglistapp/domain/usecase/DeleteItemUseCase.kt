package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingItemRepository
import javax.inject.Inject

/**
 * Use case for deleting a shopping list item.
 * This encapsulates the business logic for deleting items including validation.
 */
class DeleteItemUseCase @Inject constructor(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    /**
     * Deletes the specified shopping list item.
     * @param item The item to delete
     * @throws IllegalArgumentException if the item ID is invalid
     */
    suspend operator fun invoke(item: ItemModel) {
        // Business logic: Validate input
        if (item.id <= 0) {
            throw IllegalArgumentException("Invalid item ID")
        }
        
        // Delegate to repository
        shoppingItemRepository.deleteItem(item)
    }
} 