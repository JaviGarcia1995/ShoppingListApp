package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

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
     * @throws IllegalArgumentException if the name is blank
     */
    suspend operator fun invoke(name: String) {
        // Business logic: Validate input
        if (name.isBlank()) {
            throw IllegalArgumentException("Shopping list name cannot be empty")
        }
        
        // Delegate to repository
        shoppingListRepository.addShoppingList(name.trim())
    }
} 