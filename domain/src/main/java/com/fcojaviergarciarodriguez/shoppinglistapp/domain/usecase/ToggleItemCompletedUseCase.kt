package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.Result
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
     * @return Result.Success if successful, Result.Error.ValidationError if ID is invalid
     */
    suspend operator fun invoke(item: ItemModel): Result<Unit> {
        return try {
            if (item.id <= 0) {
                Result.Error.ValidationError("Invalid item ID")
            } else {
                val updatedItem = item.copy(isChecked = !item.isChecked)
                shoppingItemRepository.updateItem(updatedItem)
                Result.Success(Unit)
            }
        } catch (e: Exception) {
            Result.Error.UnknownError(e.message ?: "Unknown error occurred")
        }
    }
} 