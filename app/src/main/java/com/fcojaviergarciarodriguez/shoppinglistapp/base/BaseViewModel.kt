package com.fcojaviergarciarodriguez.shoppinglistapp.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState> : ViewModel() {
    
    protected abstract val _uiState: MutableStateFlow<UiState>
    abstract val uiState: StateFlow<UiState>
    
    protected fun <T> handleResult(
        result: Result<T>,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit
    ) {
        when (result) {
            is Result.Success -> onSuccess(result.data)
            is Result.Error -> onError(result.displayMessage)
        }
    }
    
    protected fun <T> executeUseCase(
        useCase: suspend () -> Result<T>,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            handleResult(
                result = useCase(),
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }
}

