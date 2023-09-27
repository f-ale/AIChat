package com.francescoalessi.sagai.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConversationListItem(
    characterName: String,
    lastMessage: String,
    onConversationClicked: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onConversationClicked() }
    ) {
        Icon(
            Icons.Default.AccountCircle,
            characterName,
            modifier = Modifier.size(64.dp)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(38.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                characterName,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                lastMessage,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}