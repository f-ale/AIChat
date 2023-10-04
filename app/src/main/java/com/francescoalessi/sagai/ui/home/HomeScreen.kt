package com.francescoalessi.sagai.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
            items(conversations.value) { item ->
                ConversationListItem(
                    characterName = item.character?.name ?: "None",
                    lastMessage = item.messages.lastOrNull()?.content ?: "",
                    onConversationClicked = { onConversationClicked(item.conversation.id)})
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

                    /*
                    // test end
                    var isDropdownOpen by remember { mutableStateOf(false) }
                    Log.d("texgen", characters.toString())
                    ExposedDropdownMenuBox(
                        expanded = isDropdownOpen,
                        onExpandedChange = {
                            isDropdownOpen = it
                        }
                    ) {
                        TextField("", {})
                        ExposedDropdownMenu(
                            expanded = isDropdownOpen,
                            onDismissRequest = { isDropdownOpen = false }) {
                            characters.forEach { character ->
                                DropdownMenuItem(
                                    onClick = {
                                        //selectedCharacter = character.id
                                        isDropdownOpen = false
                                    }, text = {
                                        Text(character.name)
                                    },
                                    enabled = true
                                )
                            }
                        }

                    }*/
                }
            )
        }
    }
}