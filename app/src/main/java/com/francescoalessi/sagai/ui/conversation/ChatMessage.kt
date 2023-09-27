package com.francescoalessi.sagai.ui.conversation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.francescoalessi.sagai.ui.theme.SagaiAIChatTheme

@Composable
fun ChatMessage(content: String, modifier: Modifier = Modifier, isUser:Boolean = false) {
    Card(
        modifier = modifier.fillMaxWidth(0.8f),
        colors =
        if(isUser)
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        else
            CardDefaults.cardColors()
    ) {
        Text(
            text = content,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessagePreview() {
    SagaiAIChatTheme {
        ChatMessage("Android")
    }
}