package com.fcojaviergarciarodriguez.shoppinglistapp.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    
    sealed class Error : Result<Nothing>() {
        data class ValidationError(val message: String) : Error()
        data class NetworkError(val message: String) : Error()
        data class DatabaseError(val message: String) : Error()
        data class UnknownError(val message: String) : Error()
        
        val displayMessage: String
            get() = when (this) {
                is ValidationError -> message
                is NetworkError -> message
                is DatabaseError -> message
                is UnknownError -> message
            }
    }
}

