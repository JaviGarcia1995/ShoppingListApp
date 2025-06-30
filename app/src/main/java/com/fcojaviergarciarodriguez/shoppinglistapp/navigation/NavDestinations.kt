package com.fcojaviergarciarodriguez.shoppinglistapp.navigation

import kotlinx.serialization.Serializable

interface NavDestinations

@Serializable
object ShoppingListHome

@Serializable
data class ShoppingListItems(
    val shoppingListId: Int
) : NavDestinations