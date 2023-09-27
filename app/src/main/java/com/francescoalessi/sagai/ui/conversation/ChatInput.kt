package com.francescoalessi.sagai.ui.conversation

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInput(modifier: Modifier = Modifier, onSend: (message:String) -> Unit)
{
    var field by remember { mutableStateOf("") }
        TextField(
            modifier = modifier.fillMaxWidth(),
            value = field,
            onValueChange = {
                field = it
            },
            keyboardActions = KeyboardActions(onSend = {
                onSend(field)
                field = ""
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            trailingIcon = {
                IconButton(
                    onClick = {
                        onSend(field)
                        field = ""
                    }) {
                    Icon(Icons.Default.Send, "Send")
                }
            },
        )
}