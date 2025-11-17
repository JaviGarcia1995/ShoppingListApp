package com.fcojaviergarciarodriguez.shoppinglistapp.screens.shoppingListsHomeScreen

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.fcojaviergarciarodriguez.shoppinglistapp.base.BaseViewModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase.AddShoppingListUseCase
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase.DeleteShoppingListUseCase
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.usecase.GetShoppingListsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsHomeViewModel @Inject constructor(
    private val getShoppingListsUseCase: GetShoppingListsUseCase,
    private val addShoppingListUseCase: AddShoppingListUseCase,
    private val deleteShoppingListUseCase: DeleteShoppingListUseCase
) : BaseViewModel<ShoppingListsHomeUiState>() {

    override val _uiState = MutableStateFlow(ShoppingListsHomeUiState())
    override val uiState: StateFlow<ShoppingListsHomeUiState> = _uiState.asStateFlow()

    init {
        loadShoppingLists()
    }

    private fun loadShoppingLists() {
        viewModelScope.launch {
            try {
                getShoppingListsUseCase().collect { lists ->
                    _uiState.value = _uiState.value.copy(
                        shoppingLists = lists,
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

    fun addShoppingList(name: String) {
        executeUseCase(
            useCase = { addShoppingListUseCase(name) },
            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    showAddListBottomSheet = false,
                    newListName = ""
                )
            },
            onError = { message ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = message
                )
            }
        )
    }

    fun deleteShoppingList(shoppingList: ShoppingListModel) {
        executeUseCase(
            useCase = { deleteShoppingListUseCase(shoppingList) },
            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    showDeleteDialog = false,
                    listToDelete = null
                )
            },
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

    fun showAddListBottomSheet() {
        _uiState.value = _uiState.value.copy(showAddListBottomSheet = true)
    }

    fun hideAddListBottomSheet() {
        _uiState.value = _uiState.value.copy(
            showAddListBottomSheet = false,
            newListName = ""
        )
    }

    fun updateNewListName(name: String) {
        _uiState.value = _uiState.value.copy(newListName = name)
    }

    fun showDeleteConfirmation(shoppingList: ShoppingListModel) {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = true,
            listToDelete = shoppingList
        )
    }

    fun hideDeleteConfirmation() {
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = false,
            listToDelete = null
        )
    }
}

@Immutable
data class ShoppingListsHomeUiState(
    val shoppingLists: List<ShoppingListModel> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val showAddListBottomSheet: Boolean = false,
    val newListName: String = "",
    val showDeleteDialog: Boolean = false,
    val listToDelete: ShoppingListModel? = null
)