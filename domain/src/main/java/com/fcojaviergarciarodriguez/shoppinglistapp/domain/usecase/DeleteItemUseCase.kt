package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.Result
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
     * @return Result.Success if successful, Result.Error.ValidationError if ID is invalid
     */
    suspend operator fun invoke(item: ItemModel): Result<Unit> {
        return try {
            if (item.id <= 0) {
                Result.Error.ValidationError("Invalid item ID")
            } else {
                shoppingItemRepository.deleteItem(item)
                Result.Success(Unit)
            }
        } catch (e: Exception) {
            Result.Error.UnknownError(e.message ?: "Unknown error occurred")
        }
    }
} 