package com.fcojaviergarciarodriguez.shoppinglistapp.screens.shoppingListItemsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ItemModel
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomAddElementFloatingButton
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomAddListBottomSheet
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomTopBar
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.PrimaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItemsScreen(
    viewModel: ShoppingListItemsViewModel,
    onBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val shoppingList = uiState.shoppingList

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showBottomSheet = remember { mutableStateOf(false) }
    val newItemName = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar errores con Snackbar
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                text = shoppingList?.name ?: "Loading...",
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            CustomAddElementFloatingButton(
                showSheetState = showBottomSheet,
                text = "New Item"
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
                            text = "No items yet. Add some!",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(shoppingList.items, key = { it.id }) { item ->
                            ItemRow(
                                item = item,
                                onCheckedChange = {
                                    viewModel.toggleItemCompleted(item)
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
        showSheetState = showBottomSheet,
        formTitle = "New Item",
        textFieldLabel = "Item name",
        newElementName = newItemName.value,
        onNewElementNameChange = { newItemName.value = it },
        addElement = { 
            viewModel.addItem(it)
            newItemName.value = ""
        }
    )
}

@Composable
fun ItemRow(
    item: ItemModel,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = { _ -> onCheckedChange() },
            colors = CheckboxDefaults.colors(
                checkedColor = PrimaryColor,
            )
        )
        Text(
            text = item.name,
            fontSize = 18.sp,
            textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}