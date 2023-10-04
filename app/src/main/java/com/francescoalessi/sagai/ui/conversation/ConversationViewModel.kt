package com.francescoalessi.sagai.ui.conversation

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.francescoalessi.sagai.data.Character
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.data.Message
import com.francescoalessi.sagai.data.relations.ConversationWithCharacter
import com.francescoalessi.sagai.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val repository: Repository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val conversationId:Int = savedStateHandle.get<Int>("conversationId") ?: 0
    val messages: Flow<PagingData<Message>> =
        repository.getPagedMessagesForConversation(conversationId).cachedIn(viewModelScope)
    val conversation: Flow<ConversationWithCharacter> =
        repository.getConversationWithCharacterAsFlow(conversationId)

    // TODO: 27/09 Retrieve conversation and character

    // TODO: Assume conversation id is 0 and load all the messages for that convo
    fun sendMessage(message:String) { // TODO: remove nullable types
        viewModelScope.launch {
            val conversation = repository.getConversationWithCharacter(conversationId)
            Log.d("textgen", conversation.toString())
            repository.sendMessage(
                conversation.character
                    ?: Character(0, "", ""),
                conversation.conversation,
                message
            )
        }
    }
}