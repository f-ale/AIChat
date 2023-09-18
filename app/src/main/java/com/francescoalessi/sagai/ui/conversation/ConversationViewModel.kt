package com.francescoalessi.sagai.ui.conversation

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.francescoalessi.sagai.data.Character
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.data.Message
import com.francescoalessi.sagai.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    val messages: Flow<PagingData<Message>> =
        repository.getPagedMessagesForConversation(0).cachedIn(viewModelScope)

    // TODO: Assume conversation id is 0 and load all the messages for that convo
    fun sendMessage(character: Character? = null, conversation: Conversation? = null, message:String) { // TODO: remove nullable types
        viewModelScope.launch {
            repository.sendMessage(
                Character(0,"0","0",false),
                Conversation(0,0),
                message
            )
        }
    }
    fun getMessagesForConversation(): Flow<List<Message>>
    {
        return repository.getMessagesForConversation(0)
    }
}