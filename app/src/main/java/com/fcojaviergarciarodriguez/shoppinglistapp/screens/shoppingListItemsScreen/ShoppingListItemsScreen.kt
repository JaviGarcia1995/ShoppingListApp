package com.fcojaviergarciarodriguez.shoppinglistapp.screens.shoppingListItemsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.R
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomAddElementFloatingButton
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.ShoppingListAppTheme
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomAddListBottomSheet
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomTopBar
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.PrimaryColor

@Composable
fun ShoppingListItemsScreen(
    viewModel: ShoppingListItemsViewModel,
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val onShowAddItemBottomSheet = remember { { viewModel.showAddItemBottomSheet() } }
    val onHideAddItemBottomSheet = remember { { viewModel.hideAddItemBottomSheet() } }
    val onUpdateNewItemName = remember { { name: String -> viewModel.updateNewItemName(name) } }
    val onAddItem = remember { { name: String -> viewModel.addItem(name) } }
    val onToggleItemCompleted = remember { { item: ItemModel -> viewModel.toggleItemCompleted(item) } }
    val onDeleteItem = remember { { item: ItemModel -> viewModel.deleteItem(item) } }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    ShoppingListItemsContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onShowAddItemBottomSheet = onShowAddItemBottomSheet,
        onHideAddItemBottomSheet = onHideAddItemBottomSheet,
        onUpdateNewItemName = onUpdateNewItemName,
        onAddItem = onAddItem,
        onToggleItemCompleted = onToggleItemCompleted,
        onDeleteItem = onDeleteItem,
        onBackClicked = onBackClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItemsContent(
    uiState: ShoppingListItemsUiState,
    snackbarHostState: SnackbarHostState,
    onShowAddItemBottomSheet: () -> Unit,
    onHideAddItemBottomSheet: () -> Unit,
    onUpdateNewItemName: (String) -> Unit,
    onAddItem: (String) -> Unit,
    onToggleItemCompleted: (ItemModel) -> Unit,
    onDeleteItem: (ItemModel) -> Unit,
    onBackClicked: () -> Unit
) {
    val shoppingList = uiState.shoppingList
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            CustomTopBar(
                text = shoppingList?.name ?: stringResource(R.string.loading),
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            CustomAddElementFloatingButton(
                onClick = onShowAddItemBottomSheet,
                text = stringResource(R.string.new_item)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            shoppingList != null -> {
                if (shoppingList.items.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_items_yet),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(shoppingList.items, key = { item -> item.id }) { item ->
                            SwipeToDeleteItemRow(
                                item = item,
                                onCheckedChange = {
                                    onToggleItemCompleted(item)
                                },
                                onDelete = {
                                    onDeleteItem(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    CustomAddListBottomSheet(
        sheetState = sheetState,
        showBottomSheet = uiState.showAddItemBottomSheet,
        formTitle = stringResource(R.string.new_item),
        textFieldLabel = stringResource(R.string.item_name),
        newElementName = uiState.newItemName,
        onNewElementNameChange = onUpdateNewItemName,
        onDismiss = onHideAddItemBottomSheet,
        addElement = onAddItem
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteItemRow(
    item: ItemModel,
    onCheckedChange: () -> Unit,
    onDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        // Delete when the user swipes to the left. Do nothing when the user swipes to the right.
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        },
        positionalThreshold = { it * 0.25f }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val direction = dismissState.dismissDirection

            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> Color.Red.copy(alpha = 0.8f)
                else -> Color.Transparent
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
        ItemRow(item = item, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun ItemRow(
    item: ItemModel,
    onCheckedChange: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { _ -> onCheckedChange() },
                colors = CheckboxDefaults.colors(
                    checkedColor = PrimaryColor,
                    uncheckedColor = PrimaryColor
                )
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = item.name,
                fontSize = 18.sp,
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemRowPreview() {
    ShoppingListAppTheme {
        ItemRow(
            item = ItemModel(
                id = 1,
                listId = 1,
                name = "Leche",
                isChecked = false
            ),
            onCheckedChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemRowCheckedPreview() {
    ShoppingListAppTheme {
        ItemRow(
            item = ItemModel(
                id = 2,
                listId = 1,
                name = "Pan",
                isChecked = true
            ),
            onCheckedChange = {}
        )
    }
}