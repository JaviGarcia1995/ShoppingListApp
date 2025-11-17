package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.Result
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingListRepository
import javax.inject.Inject

/**
 * Use case for adding a new shopping list.
 * This encapsulates the business logic for creating shopping lists including validation.
 */
class AddShoppingListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) {
    /**
     * Adds a new shopping list with the given name.
     * @param name The name of the shopping list to create
     * @return Result.Success if successful, Result.Error.ValidationError if name is blank
     */
    suspend operator fun invoke(name: String): Result<Unit> {
        return try {
            if (name.isBlank()) {
                Result.Error.ValidationError("Shopping list name cannot be empty")
            } else {
                shoppingListRepository.addShoppingList(name.trim())
                Result.Success(Unit)
            }
        } catch (e: Exception) {
            Result.Error.UnknownError(e.message ?: "Unknown error occurred")
        }
    }
} 