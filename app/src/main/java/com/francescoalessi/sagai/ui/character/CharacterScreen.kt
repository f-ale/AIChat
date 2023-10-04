package com.francescoalessi.sagai.ui.character

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.francescoalessi.sagai.data.Character
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.ui.common.AppScaffold
import com.francescoalessi.sagai.ui.home.ConversationListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(
    navController: NavController,
    characterViewModel: CharacterViewModel,
    onNewCharacterClicked: () -> Unit,
    onCharacterClicked: (characterId: Int) -> Unit,
) {
    val characters = characterViewModel.characters.collectAsStateWithLifecycle(initialValue = listOf())
    AppScaffold(
        navController = navController,
        floatingActionButton = {
            FloatingActionButton(onClick = onNewCharacterClicked) {
                Icon(Icons.Default.Add, contentDescription = "New Character")
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Characters") }
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(characters.value) { character ->
                ConversationListItem(
                    characterName = character.name,
                    lastMessage = character.attributes,
                    onConversationClicked = {
                        onCharacterClicked(character.id)
                    }
                )
            }
        }
    }

}