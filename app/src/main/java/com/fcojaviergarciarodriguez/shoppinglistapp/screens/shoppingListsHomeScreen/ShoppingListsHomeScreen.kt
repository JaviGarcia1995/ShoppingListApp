package com.fcojaviergarciarodriguez.shoppinglistapp.screens.shoppingListsHomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.R
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.ShoppingListAppTheme
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomAddElementFloatingButton
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomAddListBottomSheet
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomAlertDialog
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomTopBar
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.PrimaryColor
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.SecondaryColor

@Composable
fun ShoppingListsHomeScreen(
    viewModel: ShoppingListsHomeViewModel,
    onShoppingListClicked: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    val onShowAddListBottomSheet = remember { { viewModel.showAddListBottomSheet() } }
    val onHideAddListBottomSheet = remember { { viewModel.hideAddListBottomSheet() } }
    val onUpdateNewListName = remember { { name: String -> viewModel.updateNewListName(name) } }
    val onAddShoppingList = remember { { name: String -> viewModel.addShoppingList(name) } }
    val onShowDeleteConfirmation = remember { { list: ShoppingListModel -> viewModel.showDeleteConfirmation(list) } }
    val onHideDeleteConfirmation = remember { { viewModel.hideDeleteConfirmation() } }
    val onDeleteShoppingList = remember { { list: ShoppingListModel -> viewModel.deleteShoppingList(list) } }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    ShoppingListsHomeContent(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onShowAddListBottomSheet = onShowAddListBottomSheet,
        onHideAddListBottomSheet = onHideAddListBottomSheet,
        onUpdateNewListName = onUpdateNewListName,
        onAddShoppingList = onAddShoppingList,
        onShowDeleteConfirmation = onShowDeleteConfirmation,
        onHideDeleteConfirmation = onHideDeleteConfirmation,
        onDeleteShoppingList = onDeleteShoppingList,
        onShoppingListClicked = onShoppingListClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListsHomeContent(
    uiState: ShoppingListsHomeUiState,
    snackBarHostState: SnackbarHostState,
    onShowAddListBottomSheet: () -> Unit,
    onHideAddListBottomSheet: () -> Unit,
    onUpdateNewListName: (String) -> Unit,
    onAddShoppingList: (String) -> Unit,
    onShowDeleteConfirmation: (ShoppingListModel) -> Unit,
    onHideDeleteConfirmation: () -> Unit,
    onDeleteShoppingList: (ShoppingListModel) -> Unit,
    onShoppingListClicked: (Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            CustomTopBar(text = stringResource(R.string.shopping_lists_title))
        },
        floatingActionButton = {
            CustomAddElementFloatingButton(
                onClick = onShowAddListBottomSheet,
                text = stringResource(R.string.new_list)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.shoppingLists.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_shopping_lists_yet),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.shoppingLists, key = { it.id }) { shoppingList ->
                            SwipeToDeleteListCard(
                                shoppingList = shoppingList,
                                onItemClick = { onShoppingListClicked(it.id) },
                                onDeleteRequest = { onShowDeleteConfirmation(it) }
                            )
                        }
                    }
                }
            }
        }

        CustomAddListBottomSheet(
            sheetState = sheetState,
            showBottomSheet = uiState.showAddListBottomSheet,
            formTitle = stringResource(R.string.new_shopping_list),
            textFieldLabel = stringResource(R.string.list_name),
            newElementName = uiState.newListName,
            onNewElementNameChange = onUpdateNewListName,
            onDismiss = onHideAddListBottomSheet,
            addElement = onAddShoppingList
        )
        
        uiState.listToDelete?.let { list ->
            if (uiState.showDeleteDialog) {
                CustomAlertDialog(
                    title = stringResource(R.string.delete_shopping_list_title),
                    message = stringResource(R.string.delete_shopping_list_message, list.name),
                    confirmText = stringResource(R.string.delete),
                    cancelText = stringResource(R.string.cancel),
                    onConfirm = {
                        onDeleteShoppingList(list)
                    },
                    onDismiss = {
                        onHideDeleteConfirmation()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteListCard(
    shoppingList: ShoppingListModel,
    onItemClick: (ShoppingListModel) -> Unit,
    onDeleteRequest: (ShoppingListModel) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDeleteRequest(shoppingList)
            }
            false
        },
        positionalThreshold = { it * 0.25f }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val color = if (direction == SwipeToDismissBoxValue.EndToStart) {
                Color.Red.copy(alpha = 0.8f)
            } else {
                Color.Transparent
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (direction == SwipeToDismissBoxValue.EndToStart) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_button),
                        tint = Color.White
                    )
                }
            }
        }
    ) {
        ShoppingListCard(
            shoppingList = shoppingList,
            onItemClick = onItemClick
        )
    }
}

@Composable
fun ShoppingListCard(
    shoppingList: ShoppingListModel,
    onItemClick: (ShoppingListModel) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onItemClick(shoppingList) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = PrimaryColor,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = stringResource(R.string.list_icon),
                    tint = Color.Black
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = shoppingList.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.items_count, shoppingList.items.size),
                    fontSize = 14.sp,
                    color = SecondaryColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListCardPreview() {
    ShoppingListAppTheme {
        ShoppingListCard(
            shoppingList = ShoppingListModel(
                id = 1,
                name = "Mi Lista de Compras",
                items = listOf(
                    ItemModel(
                        id = 1,
                        listId = 1,
                        name = "Leche",
                        isChecked = false
                    ),
                    ItemModel(
                        id = 2,
                        listId = 1,
                        name = "Pan",
                        isChecked = true
                    )
                )
            ),
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListsHomeContentPreview() {
    ShoppingListAppTheme {
        ShoppingListsHomeContent(
            uiState = ShoppingListsHomeUiState(
                shoppingLists = listOf(
                    ShoppingListModel(1, "Lista 1", emptyList()),
                    ShoppingListModel(2, "Lista 2", emptyList())
                ),
                isLoading = false
            ),
            snackBarHostState = remember { SnackbarHostState() },
            onShowAddListBottomSheet = {},
            onHideAddListBottomSheet = {},
            onUpdateNewListName = {},
            onAddShoppingList = {},
            onShowDeleteConfirmation = {},
            onHideDeleteConfirmation = {},
            onDeleteShoppingList = {},
            onShoppingListClicked = {}
        )
    }
}