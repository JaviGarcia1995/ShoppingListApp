package com.fcojaviergarciarodriguez.shoppinglistapp.screens.shoppingListItemsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingItemRepository
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListItemsViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
    private val shoppingItemRepository: ShoppingItemRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val shoppingListId: Int = savedStateHandle.get<Int>("shoppingListId") ?: 0

    private val _uiState = MutableStateFlow(ShoppingListItemsUiState())
    val uiState: StateFlow<ShoppingListItemsUiState> = _uiState.asStateFlow()

    init {
        loadShoppingListWithItems()
    }

    private fun loadShoppingListWithItems() {
        viewModelScope.launch {
            try {
                shoppingListRepository.getShoppingList(shoppingListId).collect { shoppingList ->
                    _uiState.value = _uiState.value.copy(
                        shoppingList = shoppingList,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun addItem(name: String) {
        viewModelScope.launch {
            try {
                shoppingItemRepository.addItem(shoppingListId, name)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    fun toggleItemCompleted(item: ItemModel) {
        viewModelScope.launch {
            try {
                shoppingItemRepository.updateItem(item.copy(isChecked = !item.isChecked))
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class ShoppingListItemsUiState(
    val shoppingList: ShoppingListModel? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)