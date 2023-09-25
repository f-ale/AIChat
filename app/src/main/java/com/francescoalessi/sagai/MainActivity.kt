package com.francescoalessi.sagai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.francescoalessi.sagai.api.TextGenerationService
import com.francescoalessi.sagai.api.generate.GenerateRequest
import com.francescoalessi.sagai.data.Message
import com.francescoalessi.sagai.ui.conversation.ConversationViewModel
import com.francescoalessi.sagai.ui.theme.SagaiAIChatTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var service: TextGenerationService
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val viewModel:ConversationViewModel by viewModels()
        super.onCreate(savedInstanceState)

        setContent {
            SagaiAIChatTheme {
                val systemUiController = rememberSystemUiController()
                val isSystemInDarkTheme = isSystemInDarkTheme()
                val colorScheme = MaterialTheme.colorScheme
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = colorScheme.surface.copy(alpha = 0.8F),
                        darkIcons = !isSystemInDarkTheme
                    )
                    systemUiController.setNavigationBarColor(
                        color = colorScheme.surface,
                        darkIcons = !isSystemInDarkTheme
                    )
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .imePadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val messages =
                        viewModel.messages.collectAsLazyPagingItems()

                    var field by remember { mutableStateOf("")}
                    Column {
                        TopAppBar(
                            title = {
                                Text("Assistant")
                            },
                            navigationIcon = {
                                Icon(Icons.Default.ArrowBack, "Back")
                            },
                        )
                        LazyColumn(
                            modifier = Modifier.weight(1f).imeNestedScroll(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
                            reverseLayout = true,
                        ) {
                            if (messages.loadState.refresh == LoadState.Loading) {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                    )
                                }
                            }

                            items(count = messages.itemCount) { index ->
                                val item = messages[index]
                                item?.let {
                                    ChatMessage(
                                        content = item.content,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(
                                                align =
                                                if (item.characterId == 0)
                                                    Alignment.End
                                                else
                                                    Alignment.Start
                                            ),
                                        isUser = item.characterId == 0 //TODO: Change
                                    )
                                }
                            }

                            if (messages.loadState.append == LoadState.Loading) {
                                item {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                    )
                                }
                            }

                        }

                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                                value = field,
                                onValueChange = {
                                    field = it
                                },
                                keyboardActions = KeyboardActions(onSend = {
                                    viewModel.sendMessage(message = field)
                                    field = ""
                                }),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send)
                            )
                            Button(
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    Log.d("texgen", field)
                                    viewModel.sendMessage(message = field)
                                    field = ""
                                }) {
                                    Text("Send")
                                }
                        }

                    }
                }
            }
        }
    }
}

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
fun GreetingPreview() {
    SagaiAIChatTheme {
        ChatMessage("Android")
    }
}