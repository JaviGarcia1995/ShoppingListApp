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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcojaviergarciarodriguez.shoppinglistapp.domain.model.ShoppingListModel
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomAddElementFloatingButton
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomAddListBottomSheet
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.common.CustomTopBar
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.PrimaryColor
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.SecondaryColor
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.TertiaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListsHomeScreen(
    viewModel: ShoppingListsHomeViewModel,
    onShoppingListClicked: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showBottomSheet = remember { mutableStateOf(false) }
    val newListName = remember { mutableStateOf("") }
    val snackBarHostState = remember { SnackbarHostState() }

    // Mostrar errores con SnackBar
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(text = "Shopping Lists")
        },
        floatingActionButton = {
            CustomAddElementFloatingButton(
                showSheetState = showBottomSheet,
                text = "New List"
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
                            text = "No shopping lists yet.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.shoppingLists) { shoppingList ->
                            ShoppingListCard(
                                shoppingList = shoppingList,
                                onItemClick = {
                                    onShoppingListClicked(it.id)
                                }
                            )
                        }
                    }
                }
            }
        }

        CustomAddListBottomSheet(
            sheetState = sheetState,
            showSheetState = showBottomSheet,
            formTitle = "New Shopping List",
            textFieldLabel = "List name",
            newElementName = newListName.value,
            onNewElementNameChange = { newListName.value = it },
            addElement = {
                viewModel.addShoppingList(it)
                newListName.value = ""
            }
        )
    }
}

@Composable
fun ShoppingListCard(
    shoppingList: ShoppingListModel,
    onItemClick: (ShoppingListModel) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onItemClick(shoppingList) },
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
                contentDescription = "List Icon",
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
                text = "Items: ${shoppingList.items.size}",
                fontSize = 14.sp,
                color = SecondaryColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}