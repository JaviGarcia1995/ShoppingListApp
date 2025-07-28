package com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all shopping lists.
 * This encapsulates the business logic for getting shopping lists from the repository.
 */
class GetShoppingListsUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) {
    /**
     * Retrieves all shopping lists as a Flow.
     * @return Flow of list of ShoppingListModel
     */
    operator fun invoke(): Flow<List<ShoppingListModel>> {
        return shoppingListRepository.getShoppingLists()
    }
} 