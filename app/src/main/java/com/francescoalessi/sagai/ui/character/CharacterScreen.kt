package com.francescoalessi.sagai.ui.character

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.ui.home.AppScaffold
import com.francescoalessi.sagai.ui.home.ConversationListItem

@Composable
fun CharacterScreen(navController: NavController) {
    AppScaffold(
        navController = navController,
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Add, contentDescription = "New Character")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            for(i in 0..3)
            {
                ConversationListItem(characterName = "test", lastMessage = "test") {}
            }
        }
    }

}