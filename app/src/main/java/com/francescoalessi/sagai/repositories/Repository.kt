package com.francescoalessi.sagai.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.francescoalessi.sagai.api.TextGenerationService
import com.francescoalessi.sagai.api.generate.GenerateRequest
import com.francescoalessi.sagai.data.Character
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.data.Message
import com.francescoalessi.sagai.data.SagaiDatabase
import com.francescoalessi.sagai.data.dao.ConversationDao
import com.francescoalessi.sagai.data.dao.MessageDao
import com.francescoalessi.sagai.data.relations.ConversationWithCharacter
import com.francescoalessi.sagai.data.relations.ConversationWithMessagesAndCharacter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val db:SagaiDatabase,
    private val textGenerationService: TextGenerationService,
    private val messageDao: MessageDao,
    private val conversationDao: ConversationDao,
) {
    private suspend fun getMessagesWithTokenLimit(conversationId: Int, tokenLimit: Int): List<Message> {
        val messages = mutableListOf<Message>()
        var totalTokens = 0

        val allMessages = messageDao.getLatestMessagesForConversation(conversationId, 100)
        for (message in allMessages) {
            if (totalTokens + message.tokens < tokenLimit) {
                messages.add(message)
                totalTokens += message.tokens
            } else {
                break
            }
        }

        return messages
    }

    suspend fun insertConversation(conversation: Conversation) {
        conversationDao.insert(conversation)
    }
    fun getAllConversationsWithMessagesAndCharacter(): Flow<List<ConversationWithMessagesAndCharacter>> {
        return conversationDao.getAllConversationsWithMessages()
    }
    suspend fun sendMessage(character: Character, conversation: Conversation, message: String) {
        try {
            val pastMessages =
                getMessagesWithTokenLimit(conversation.id, 4096).asReversed()

            if(message.isNotBlank())
            {
                messageDao.insert(
                    Message(
                        null,
                        characterId = -1 /* TODO: User Id */,
                        conversationId = conversation.id,
                        content = message,
                        tokens = message.length/4, // TODO: Change simplified token calculation
                        timestamp = System.currentTimeMillis()
                    )
                )
            }

            db.withTransaction {
                val prompt =
                    "You are ${character.name} and you are chatting online. "+
                            "Write short responses like in a text chat. "+
                            "Behave realistically like ${character.name}. "+
                            "${character.name} is ${character.attributes}\n" +
                    pastMessages.joinToString(
                        separator = "\n",
                ) { it ->
                    val name = if(it.characterId == -1) "User" else character.name
                    "${name}:\n${it.content}\n"
                } + if(message.isNotEmpty())
                            "User:\n${message}\n${character.name}:"
                        else
                            "${character.name}:"

                Log.d("texgen", prompt)
                val generatedText = textGenerationService.generateText(
                    GenerateRequest(
                        prompt = prompt,
                        stopping_strings = listOf(
                            "${character.name}:",
                            "User:",
                            "Assistant:"
                        )
                    )
                ).results.joinToString { it.text }.trim()

                if(generatedText.isNotBlank())
                {
                    messageDao.insert(
                        Message(
                            id = null,
                            characterId = character.id,
                            conversationId = conversation.id,
                            content = generatedText,
                            timestamp = System.currentTimeMillis(),
                            tokens = generatedText.length/4, // TODO: Change simplified token calculation
                        )
                    )
                }
            }
        } catch (e:Throwable) { // TODO: Use a different approach to error handling
            Log.e("texgen", e.toString())
        }
    }

    fun getConversationWithCharacterAsFlow(conversationId: Int): Flow<ConversationWithCharacter> =
        conversationDao.getConversationWithCharacterForIdAsFlow(conversationId)
    suspend fun getConversationWithCharacter(conversationId: Int): ConversationWithCharacter =
        conversationDao.getConversationWithCharacterForId(conversationId)

    suspend fun deleteConversation(conversation: Conversation) =
        conversationDao.deleteConversation(conversation)
    suspend fun deleteConversationById(conversationId: Int) =
        conversationDao.deleteConversationById(conversationId)
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