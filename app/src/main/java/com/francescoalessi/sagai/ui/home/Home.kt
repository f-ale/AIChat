package com.francescoalessi.sagai.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.ui.navigation.BottomNavigationBar
import com.francescoalessi.sagai.ui.theme.SagaiAIChatTheme
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    viewModel: HomeViewModel,
    navController: NavController,
    onConversationClicked: (conversationId:Int) -> Unit = {},
) {
    val conversations = viewModel.getConversations()
        .collectAsStateWithLifecycle(initialValue = listOf())

    AppScaffold(
        navController = navController,
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.insertConversation(
                Conversation(characterId = 2)
            )}) {
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(navController: NavController,
                topBar: @Composable () -> Unit = {
                    CenterAlignedTopAppBar(
                        title = { Text("Sagai AI Chat") },
                    )},
                floatingActionButton: @Composable () -> Unit = {},
                content: @Composable (paddingValues: PaddingValues) -> Unit) {
    Scaffold(
        topBar = topBar,
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        floatingActionButton = floatingActionButton,
        content = content
    )
}