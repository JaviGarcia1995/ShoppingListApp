package com.fcojaviergarciarodriguez.shoppinglistapp.framework.mapper

import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.database.ItemEntity
import com.fcojaviergarciarodriguez.shoppinglistapp.framework.database.ShoppingListEntity

fun ItemEntity.toItemModel() = ItemModel(
    id = this.id,
    listId = this.listId,
    name = this.name,
    isChecked = this.isChecked
)

fun ItemModel.toItemEntity() = ItemEntity(
    id = this.id,
    listId = this.listId,
    name = this.name,
    isChecked = this.isChecked
)

fun ShoppingListEntity.toShoppingListModel(items: List<ItemModel>) = ShoppingListModel(
    id = this.id,
    name = this.name,
    items = items
)

fun ShoppingListModel.toShoppingListEntity() = ShoppingListEntity(
    id = this.id,
    name = this.name
) 