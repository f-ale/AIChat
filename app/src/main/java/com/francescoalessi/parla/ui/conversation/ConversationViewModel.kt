package com.francescoalessi.parla.ui.conversation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.francescoalessi.parla.api.ApiResponse
import com.francescoalessi.parla.data.Character
import com.francescoalessi.parla.data.Message
import com.francescoalessi.parla.data.relations.ConversationWithCharacter
import com.francescoalessi.parla.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _textGenerationState = MutableStateFlow<UiState>(UiState.Idle)
    val textGenerationState: StateFlow<UiState> =_textGenerationState
    fun sendMessage(message:String) {
        viewModelScope.launch {
            _textGenerationState.value = UiState.Loading
            try {
                val conversation = repository.getConversationWithCharacter(conversationId)
                Log.d("textgen", conversation.toString())
                val result = repository.sendMessage(
                    conversation.character ?: Character(0, "", ""),
                    conversation.conversation,
                    message
                )
                _textGenerationState.value = when (result) {
                    is ApiResponse.Error -> UiState.Error("Failed to send message")
                    is ApiResponse.Success -> UiState.Success
                    else -> UiState.Loading
                } // Represents successful completion
            } catch (e: Exception) {
                _textGenerationState.value = UiState.Error("Failed to send message: ${e.message}")
            }
        }
    }

    fun dismissError() {
        if (_textGenerationState.value is UiState.Error) {
            _textGenerationState.value = UiState.Idle
        }
    }
}