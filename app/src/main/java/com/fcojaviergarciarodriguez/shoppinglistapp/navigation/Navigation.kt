package com.fcojaviergarciarodriguez.shoppinglistapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fcojaviergarciarodriguez.shoppinglistapp.screens.shoppingListItemsScreen.ShoppingListItemsScreen
import com.fcojaviergarciarodriguez.shoppinglistapp.screens.shoppingListItemsScreen.ShoppingListItemsViewModel
import com.fcojaviergarciarodriguez.shoppinglistapp.screens.shoppingListsHomeScreen.ShoppingListsHomeScreen
import com.fcojaviergarciarodriguez.shoppinglistapp.screens.shoppingListsHomeScreen.ShoppingListsHomeViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ShoppingListHome
    ) {
        composable<ShoppingListHome> {
            val viewModel: ShoppingListsHomeViewModel = hiltViewModel()
            ShoppingListsHomeScreen(
                viewModel = viewModel,
                onShoppingListClicked = { shoppingListId ->
                    navController.navigate(ShoppingListItems(shoppingListId = shoppingListId))
                }
            )
        }
        composable<ShoppingListItems> {
            val viewModel: ShoppingListItemsViewModel = hiltViewModel()
            ShoppingListItemsScreen(
                viewModel = viewModel,
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
    }
}