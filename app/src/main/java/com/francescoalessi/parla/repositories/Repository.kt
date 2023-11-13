package com.francescoalessi.parla.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.francescoalessi.parla.api.ApiResponse
import com.francescoalessi.parla.api.TextGenerationService
import com.francescoalessi.parla.api.generate.GenerateRequest
import com.francescoalessi.parla.api.generate.GenerateResponse
import com.francescoalessi.parla.data.Character
import com.francescoalessi.parla.data.Conversation
import com.francescoalessi.parla.data.Message
import com.francescoalessi.parla.data.SagaiDatabase
import com.francescoalessi.parla.data.dao.ConversationDao
import com.francescoalessi.parla.data.dao.MessageDao
import com.francescoalessi.parla.data.relations.ConversationWithCharacter
import com.francescoalessi.parla.data.relations.ConversationWithMessagesAndCharacter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val db:SagaiDatabase,
    private val textGenerationService: TextGenerationService,
    private val messageDao: MessageDao,
    private val conversationDao: ConversationDao,
) {
    /*
        Fetch chat messages for a given conversation until a token limit is reached.
     */
    private suspend fun getMessagesWithTokenLimit(conversationId: Int, tokenLimit: Int): List<Message> {
        // A list to store messages that fit within the token limit.
        val messages = mutableListOf<Message>()
        // Variable to keep track of the accumulated tokens from the fetched messages.
        var totalTokens = 0

        // Retrieve the latest 100 messages from the specified conversation.
        // TODO: change hardcoded limit?
        val allMessages = messageDao.getLatestMessagesForConversation(conversationId, 100)

        // Iterate through each message.
        for (message in allMessages) {
            // If adding the message doesn't exceed the token limit, include it in the list.
            if (totalTokens + message.tokens < tokenLimit) {
                messages.add(message)
                totalTokens += message.tokens
            } else {
                // Break the loop once the token limit is reached.
                break
            }
        }

        // Return the messages that fit within the token limit.
        return messages
    }

    suspend fun insertConversation(conversation: Conversation) {
        conversationDao.insert(conversation)
    }
    fun getAllConversationsWithMessagesAndCharacter(): Flow<List<ConversationWithMessagesAndCharacter>> {
        return conversationDao.getAllConversationsWithMessages()
    }

    /*
        A suspending function to send a message in a AI generated chat using the given character.
     */ // TODO: abstract the prompt building logic; use workmanager to handle long running tasks
    suspend fun sendMessage(
        character: Character,
        conversation: Conversation,
        message: String
    ): ApiResponse<GenerateResponse> {
        var apiResponse: ApiResponse<GenerateResponse> = ApiResponse.Error(Throwable("Unknow error"))
        try {
            // Fetch the previous messages up to a token limit, and reverse the list to get the recent messages first.
            val pastMessages =
                getMessagesWithTokenLimit(conversation.id, 4096).asReversed()

            // Check if the message is not blank.
            if(message.isNotBlank())
            {
                // Insert the user's message into the database.
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

            // Begin a database transaction to allow for rollbacks in case of errors.
            db.withTransaction {
                // Construct the prompt for the text generation service using the character's information and past messages.
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
                // Generate a response based on the prompt using the text generation service.
                val response = textGenerationService.generateText(
                    GenerateRequest(
                        prompt = prompt,
                        stopping_strings = listOf(
                            "${character.name}:",
                            "User:",
                            "Assistant:"
                        )
                    )
                )

                val generatedText = response.results.joinToString { it.text }.trim()

                // Check if the generated text is not blank.
                if(generatedText.isNotBlank())
                {
                    // Insert the generated message into the database.
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

                apiResponse = ApiResponse.Success(response)
            }
        } catch (e:Throwable) { // TODO: Use a different approach to error handling
            Log.e("texgen", e.toString())
            apiResponse = ApiResponse.Error(e)
        }
        return apiResponse
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