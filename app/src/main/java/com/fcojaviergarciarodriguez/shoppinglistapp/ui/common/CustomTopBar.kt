package com.fcojaviergarciarodriguez.shoppinglistapp.ui.common

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    text: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        },
        actions = {
            actions?.invoke()
        },
        navigationIcon = {
            navigationIcon?.invoke()
        }
    )
}