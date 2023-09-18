package com.francescoalessi.sagai.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.francescoalessi.sagai.api.TextGenerationService
import com.francescoalessi.sagai.api.generate.GenerateRequest
import com.francescoalessi.sagai.data.Character
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.data.Message
import com.francescoalessi.sagai.data.MessageDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val textGenerationService: TextGenerationService,
    private val messageDao: MessageDao
) {

    suspend fun sendMessage(character: Character, conversation: Conversation, message: String) {
        // TODO: Make non nullable
        val generatedText = textGenerationService.generateText(
            GenerateRequest(
                prompt =/*character.attributes+*/message
            )
        ).results.joinToString { it.text }

        messageDao.insert(
            Message(
                id = null,
                characterId = character.id,
                conversationId = conversation.id,
                content = generatedText,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun getMessagesForConversation(conversationId: Int): Flow<List<Message>> =
        messageDao.getAllMessagesForConversation(conversationId)

    fun getPagedMessagesForConversation(conversationId: Int): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(pageSize = 50, enablePlaceholders = false),
            pagingSourceFactory =
            { messageDao.getAllMessagesForConversationAsPagingSource(conversationId) }
        ).flow
    }
}