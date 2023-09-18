package com.francescoalessi.sagai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.francescoalessi.sagai.api.TextGenerationService
import com.francescoalessi.sagai.api.generate.GenerateRequest
import com.francescoalessi.sagai.data.Message
import com.francescoalessi.sagai.ui.conversation.ConversationViewModel
import com.francescoalessi.sagai.ui.theme.SagaiAIChatTheme
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
    private var messages = mutableStateListOf(Message(id = null, content = "test", timestamp = System.currentTimeMillis()))

    @OptIn(DelicateCoroutinesApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel:ConversationViewModel by viewModels()


        super.onCreate(savedInstanceState)

        /*GlobalScope.launch {
            text.value += service.generateText(GenerateRequest("Hello how are you?"))
                .results.joinToString{ item -> item.text }
            Log.d("texgen", text.value)
        }*/

        setContent {
            SagaiAIChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val messages =
                        viewModel.messages.collectAsLazyPagingItems()

                    var field by remember { mutableStateOf("")}
                    Column {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            reverseLayout = true
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
                                    ChatMessage(name = item.content)
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
                                }
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
fun ChatMessage(name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Text(
            text = name,
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