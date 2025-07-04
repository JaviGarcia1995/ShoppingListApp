package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingItemRepository
import javax.inject.Inject

/**
 * Use case for toggling the completion state of a shopping list item.
 * This encapsulates the business logic for changing item completion status.
 */
class ToggleItemCompletedUseCase @Inject constructor(
    private val shoppingItemRepository: ShoppingItemRepository
) {
    /**
     * Toggles the completion state of the given item.
     * @param item The item to toggle completion state for
     * @throws IllegalArgumentException if the item ID is invalid
     */
    suspend operator fun invoke(item: ItemModel) {
        // Business logic: Validate input
        if (item.id <= 0) {
            throw IllegalArgumentException("Invalid item ID")
        }
        
        // Business logic: Toggle the completion state
        val updatedItem = item.copy(isChecked = !item.isChecked)
        
        // Delegate to repository
        shoppingItemRepository.updateItem(updatedItem)
    }
} 