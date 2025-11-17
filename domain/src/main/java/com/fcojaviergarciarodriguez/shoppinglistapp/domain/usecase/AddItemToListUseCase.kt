package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.Result
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
     * @return Result.Success if successful, Result.Error.ValidationError if inputs are invalid
     */
    suspend operator fun invoke(listId: Int, itemName: String): Result<Unit> {
        return try {
            when {
                listId <= 0 -> Result.Error.ValidationError("Shopping list ID must be greater than 0")
                itemName.isBlank() -> Result.Error.ValidationError("Item name cannot be empty")
                else -> {
                    shoppingItemRepository.addItem(listId, itemName.trim())
                    Result.Success(Unit)
                }
            }
        } catch (e: Exception) {
            Result.Error.UnknownError(e.message ?: "Unknown error occurred")
        }
    }
} 