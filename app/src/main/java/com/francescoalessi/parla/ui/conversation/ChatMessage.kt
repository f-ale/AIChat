package com.francescoalessi.parla.ui.conversation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.francescoalessi.parla.R
import com.francescoalessi.parla.ui.theme.ParlaTheme

@Composable
fun ChatMessage(content: String, modifier: Modifier = Modifier, isUser:Boolean = false) {
    BaseChatBubble(
        modifier = modifier,
        content = {
            Text(
                text = content,
                modifier = Modifier.padding(16.dp)
            )
        },
        secondaryContainerColor = isUser
    )
}
@Composable
fun BaseChatBubble(
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    secondaryContainerColor:Boolean
) {
    Card(
        modifier = modifier.fillMaxWidth(0.8f),
        colors =
        if(secondaryContainerColor)
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        else
            CardDefaults.cardColors(),
        content = content
    )
}
@Preview(showBackground = true)
@Composable
fun ChatMessagePreview() {
    ParlaTheme {
        ChatMessage(stringResource(R.string.android))
    }
}