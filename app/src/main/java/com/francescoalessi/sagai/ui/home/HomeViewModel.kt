package com.francescoalessi.sagai.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.data.dao.CharacterDao
import com.francescoalessi.sagai.data.dao.ConversationDao
import com.francescoalessi.sagai.data.relations.ConversationWithMessagesAndCharacter
import com.francescoalessi.sagai.repositories.CharacterRepository
import com.francescoalessi.sagai.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository, // TODO: Use repository
    characterRepository: CharacterRepository
) : ViewModel() {
    val characters = characterRepository.getAllCharacters()
    fun getConversations(): Flow<List<ConversationWithMessagesAndCharacter>> {
        return repository.getAllConversationsWithMessagesAndCharacter()
    }

    fun insertConversation(conversation: Conversation) {
        viewModelScope.launch {
            repository.insertConversation(conversation)
        }
    }

    fun deleteConversation(conversation: Conversation) {
        viewModelScope.launch {
            repository.deleteConversation(conversation)
        }
    }
}