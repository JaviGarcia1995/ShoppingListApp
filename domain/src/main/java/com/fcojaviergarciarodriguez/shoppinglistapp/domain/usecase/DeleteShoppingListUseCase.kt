package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.Result
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
     * @return Result.Success if successful, Result.Error.ValidationError if ID is invalid
     */
    suspend operator fun invoke(shoppingList: ShoppingListModel): Result<Unit> {
        return try {
            if (shoppingList.id <= 0) {
                Result.Error.ValidationError("Invalid shopping list ID")
            } else {
                shoppingListRepository.deleteShoppingList(shoppingList)
                Result.Success(Unit)
            }
        } catch (e: Exception) {
            Result.Error.UnknownError(e.message ?: "Unknown error occurred")
        }
    }
} 