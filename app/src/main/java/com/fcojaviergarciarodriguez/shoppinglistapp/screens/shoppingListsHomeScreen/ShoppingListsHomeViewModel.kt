package com.fcojaviergarciarodriguez.shoppinglistapp.screens.shoppingListsHomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.repository.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListsHomeViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListsHomeUiState())
    val uiState: StateFlow<ShoppingListsHomeUiState> = _uiState.asStateFlow()

    init {
        loadShoppingLists()
    }

    private fun loadShoppingLists() {
        viewModelScope.launch {
            try {
                shoppingListRepository.getShoppingLists().collect { lists ->
                    _uiState.value = _uiState.value.copy(
                        shoppingLists = lists,
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

    fun addShoppingList(name: String) {
        viewModelScope.launch {
            try {
                shoppingListRepository.addShoppingList(name)
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

data class ShoppingListsHomeUiState(
    val shoppingLists: List<ShoppingListModel> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)