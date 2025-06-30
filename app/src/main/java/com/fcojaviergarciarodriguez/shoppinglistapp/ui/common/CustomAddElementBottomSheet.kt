package com.fcojaviergarciarodriguez.shoppinglistapp.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.PrimaryColor
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.SecondaryColor
import com.fcojaviergarciarodriguez.shoppinglistapp.ui.theme.TertiaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAddListBottomSheet(
    sheetState: SheetState,
    showSheetState: MutableState<Boolean>,
    formTitle: String,
    textFieldLabel: String,
    newElementName: String,
    onNewElementNameChange: (String) -> Unit,
    addElement: (name: String) -> Unit
) {
    if (showSheetState.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheetState.value = false
                onNewElementNameChange("")
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = formTitle,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.size(16.dp))

                TextField(
                    value = newElementName,
                    onValueChange = { onNewElementNameChange(it) },
                    label = { Text(textFieldLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = TertiaryColor,
                        unfocusedContainerColor = TertiaryColor,
                        disabledContainerColor = TertiaryColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black,
                        focusedLabelColor = SecondaryColor,
                        unfocusedLabelColor = SecondaryColor,
                        focusedTextColor = SecondaryColor,
                        unfocusedTextColor = SecondaryColor,
                        disabledLabelColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.size(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End

                ) {
                    Button(
                        onClick = {
                            onNewElementNameChange("")
                            showSheetState.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TertiaryColor
                        )
                    ) {
                        Text(text = "Cancel", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newElementName.isNotBlank()) {
                                addElement(newElementName)
                                showSheetState.value = false
                                onNewElementNameChange("")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor
                        )
                    ) {
                        Text(text = "Create", color = Color.Black)
                    }
                }
            }
        }
    }
}