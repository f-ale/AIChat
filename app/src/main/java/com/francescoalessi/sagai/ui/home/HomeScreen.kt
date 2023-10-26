package com.francescoalessi.sagai.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.francescoalessi.sagai.R
import com.francescoalessi.sagai.data.Character
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.ui.common.AppScaffold

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
            if(characters.isNotEmpty())
            // TODO: Go to new char screen if no chars present and user wants to initiate convo
            {
                FloatingActionButton(
                    onClick = { isNewConversationDialogOpen = true }
                ) {
                    Icon(Icons.Default.Edit, stringResource(R.string.new_chat))
                }
            }

        }
    ) { paddingValues ->
        if(conversations.value.isNotEmpty())
        {
            LazyColumn(
                Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()) {
                items(conversations.value) { conversationItem ->
                    ConversationListItem(
                        // TODO: Verify that character is actually non-null
                        characterName = conversationItem.character?.name ?: stringResource(R.string.no_name),
                        characterImage = conversationItem.character?.image,
                        lastMessage = conversationItem.messages.lastOrNull()?.content ?: "",
                        onItemClicked = { onConversationClicked(conversationItem.conversation.id)},
                        onItemDeleted = { viewModel.deleteConversation(conversationItem.conversation)}
                    )
                }
            }
        }
        else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.no_conversations_yet))
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
                            // TODO: Navigate to new convo
                            isNewConversationDialogOpen = false
                        }
                    ) {
                            Text(stringResource(R.string.create))
                    }
                },
                title = { Text(stringResource(R.string.choose_a_character))},
                icon = { Icon(Icons.Default.Person, stringResource(R.string.character))},
                dismissButton = {
                    Button(
                        onClick = { isNewConversationDialogOpen = false }
                    ) {
                        Text(stringResource(R.string.cancel))
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
                            label = { Text(stringResource(R.string.character)) },
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