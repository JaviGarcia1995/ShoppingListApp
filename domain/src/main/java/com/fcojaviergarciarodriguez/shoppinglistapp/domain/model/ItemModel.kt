package com.fcojaviergarciarodriguez.shoppinglistapp.domain.model

data class ItemModel(
    val id: Int,
    val listId: Int,
    val name: String,
    val isChecked: Boolean
) 