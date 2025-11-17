package com.fcojaviergarciarodriguez.shoppinglistapp.screens.shoppingListItemsScreen

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.fcojaviergarciarodriguez.shoppinglistapp.base.BaseViewModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase.AddItemToListUseCase
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase.DeleteItemUseCase
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase.GetShoppingListByIdUseCase
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase.ToggleItemCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListItemsViewModel @Inject constructor(
    private val getShoppingListByIdUseCase: GetShoppingListByIdUseCase,
    private val addItemToListUseCase: AddItemToListUseCase,
    private val toggleItemCompletedUseCase: ToggleItemCompletedUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ShoppingListItemsUiState>() {

    private val shoppingListId: Int = savedStateHandle.get<Int>("shoppingListId") ?: 0

    override val _uiState = MutableStateFlow(ShoppingListItemsUiState())
    override val uiState: StateFlow<ShoppingListItemsUiState> = _uiState.asStateFlow()

    init {
        loadShoppingListWithItems()
    }

    private fun loadShoppingListWithItems() {
        viewModelScope.launch {
            try {
                getShoppingListByIdUseCase(shoppingListId).collect { shoppingList ->
                    _uiState.value = _uiState.value.copy(
                        shoppingList = shoppingList,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun addItem(name: String) {
        executeUseCase(
            useCase = { addItemToListUseCase(shoppingListId, name) },
            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    showAddItemBottomSheet = false,
                    newItemName = ""
                )
            },
            onError = { message ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = message
                )
            }
        )
    }

    fun toggleItemCompleted(item: ItemModel) {
        executeUseCase(
            useCase = { toggleItemCompletedUseCase(item) },
            onSuccess = { },
            onError = { message ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = message
                )
            }
        )
    }

    fun deleteItem(item: ItemModel) {
        executeUseCase(
            useCase = { deleteItemUseCase(item) },
            onSuccess = { },
            onError = { message ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = message
                )
            }
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun showAddItemBottomSheet() {
        _uiState.value = _uiState.value.copy(showAddItemBottomSheet = true)
    }

    fun hideAddItemBottomSheet() {
        _uiState.value = _uiState.value.copy(
            showAddItemBottomSheet = false,
            newItemName = ""
        )
    }

    fun updateNewItemName(name: String) {
        _uiState.value = _uiState.value.copy(newItemName = name)
    }
}

@Immutable
data class ShoppingListItemsUiState(
    val shoppingList: ShoppingListModel? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val showAddItemBottomSheet: Boolean = false,
    val newItemName: String = ""
)