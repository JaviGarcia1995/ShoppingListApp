package com.fcojaviergarciarodriguez.shoppinglistapp.domain.model

data class ShoppingListModel(
    val id: Int,
    val name: String,
    val items: List<ItemModel>
) 