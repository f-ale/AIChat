package com.francescoalessi.parla.ui.conversation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.francescoalessi.parla.api.ApiResponse
import com.francescoalessi.parla.data.Message
import com.francescoalessi.parla.data.relations.ConversationWithCharacter
import com.francescoalessi.parla.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val repository: Repository,
    private val workManager: WorkManager,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val conversationId:Int = savedStateHandle.get<Int>("conversationId") ?: 0
    val messages: Flow<PagingData<Message>> =
        repository.getPagedMessagesForConversation(conversationId).cachedIn(viewModelScope)
    val conversation: Flow<ConversationWithCharacter> =
        repository.getConversationWithCharacterAsFlow(conversationId)

    private val _textGenerationState = MutableStateFlow<UiState>(UiState.Idle)
    val textGenerationState: StateFlow<UiState> =_textGenerationState

    val workInfo: Flow<List<WorkInfo.State>> = 
        workManager.getWorkInfosByTagFlow("conversationId: $conversationId").map { list -> list.map { it.state } }

    fun sendMessage(message:String) {
        val data = workDataOf(
            "message" to message,
            "conversationId" to conversationId,
        )

        /*val workRequest = OneTimeWorkRequestBuilder<SendMessageWorker>()
            .setInputData(data)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) // TODO: older android version support
            .addTag("messageSending")
            .addTag("conversationId: $conversationId")
            .build()

        workManager.enqueue(workRequest)*/

        viewModelScope.launch(Dispatchers.IO) {
            _textGenerationState.value = UiState.Loading
            try {
                val conversation = repository.getConversationWithCharacter(conversationId)
                Log.d("textgen", conversation.toString())
                val result = repository.sendMessage(
                    conversation.character,
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