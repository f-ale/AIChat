package com.francescoalessi.sagai.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.data.dao.CharacterDao
import com.francescoalessi.sagai.data.dao.ConversationDao
import com.francescoalessi.sagai.data.relations.ConversationWithMessagesAndCharacter
import com.francescoalessi.sagai.repositories.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val conversationDao: ConversationDao, // TODO: Use repository
    characterRepository: CharacterRepository
) : ViewModel() {
    val characters = characterRepository.getAllCharacters()
    fun getConversations(): Flow<List<ConversationWithMessagesAndCharacter>> {
        return conversationDao.getAllConversationsWithMessages()
    }

    fun insertConversation(conversation: Conversation) {
        viewModelScope.launch {
            conversationDao.insert(conversation)
        }
    }

    fun deleteConversation(conversation: Conversation) {
        viewModelScope.launch {
            conversationDao.deleteConversation(conversation)
        }
    }
}