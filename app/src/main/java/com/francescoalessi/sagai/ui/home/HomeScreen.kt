package com.francescoalessi.sagai.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.ui.common.AppScaffold
import com.francescoalessi.sagai.data.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    viewModel: HomeViewModel,
    navController: NavController,
    onConversationClicked: (conversationId:Int) -> Unit = {},
) {
    val conversations = viewModel.getConversations()
        .collectAsStateWithLifecycle(initialValue = listOf())
    val characters by viewModel.characters.collectAsStateWithLifecycle(
        initialValue = listOf(Character(0,"",""))
    )

    var isNewConversationDialogOpen by remember {
        mutableStateOf(false)
    }

    AppScaffold(
        navController = navController,
        floatingActionButton = {
            FloatingActionButton(onClick = { isNewConversationDialogOpen = true }) {
                Icon(Icons.Default.Add, "New Chat")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .padding(paddingValues)
                .fillMaxWidth()) {
            items(conversations.value) { conversationItem ->
                ConversationListItem(
                    characterName = conversationItem.character?.name ?: "None",
                    characterImage = conversationItem.character?.image,
                    lastMessage = conversationItem.messages.lastOrNull()?.content ?: "",
                    onItemClicked = { onConversationClicked(conversationItem.conversation.id)},
                    onItemDeleted = { viewModel.deleteConversation(conversationItem.conversation)}
                )
            }
        }

        if(isNewConversationDialogOpen)
        {
            var selectedCharacter by remember { mutableStateOf(Character(0,"","")) }
            AlertDialog(
                onDismissRequest = { isNewConversationDialogOpen = false },
                confirmButton = {
                    Button(
                        enabled = selectedCharacter.id != 0,
                        onClick = {
                            viewModel.insertConversation(
                                Conversation(characterId = selectedCharacter.id)
                            )
                            isNewConversationDialogOpen = false
                        }
                    ) {
                            Text("Create")
                    }
                },
                title = { Text("Choose a character")},
                icon = { Icon(Icons.Default.Person, "Character")},
                dismissButton = {
                    Button(
                        onClick = { isNewConversationDialogOpen = false }
                    ) {
                        Text("Cancel")
                    }
                },
                text = {
                    var expanded by remember { mutableStateOf(false) }
// We want to react on tap/press on TextField to show menu
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                    ) {
                        TextField(
                            // The `menuAnchor` modifier must be passed to the text field for correctness.
                            modifier = Modifier.menuAnchor(),
                            readOnly = true,
                            value = selectedCharacter.name,
                            onValueChange = {},
                            label = { Text("Character") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            characters.forEach { character ->
                                DropdownMenuItem(
                                    text = { Text(character.name) },
                                    onClick = {
                                        selectedCharacter = character
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}