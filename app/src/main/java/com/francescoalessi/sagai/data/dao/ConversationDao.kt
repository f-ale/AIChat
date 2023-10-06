package com.francescoalessi.sagai.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.data.relations.ConversationWithCharacter
import com.francescoalessi.sagai.data.relations.ConversationWithMessagesAndCharacter
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Insert
    suspend fun insert(conversation: Conversation)
    @Query("SELECT * FROM conversation")
    fun getAllConversations(): Flow<List<Conversation>>
    @Transaction
    @Query("SELECT * FROM conversation")
    fun getAllConversationsWithMessages(): Flow<List<ConversationWithMessagesAndCharacter>>
    @Transaction
    @Query("SELECT * FROM conversation WHERE id = :conversationId LIMIT 1")
    fun getConversationWithMessagesForId(conversationId: Int): Flow<ConversationWithMessagesAndCharacter>
    @Transaction
    @Query("SELECT * FROM conversation WHERE id = :conversationId LIMIT 1")
    fun getConversationWithCharacterForIdAsFlow(conversationId: Int): Flow<ConversationWithCharacter>
    @Transaction
    @Query("SELECT * FROM conversation WHERE id = :conversationId LIMIT 1")
    suspend fun getConversationWithCharacterForId(conversationId: Int): ConversationWithCharacter
    @Delete
    suspend fun deleteConversation(conversation: Conversation)
    @Query("DELETE FROM conversation WHERE id = :conversationId")
    suspend fun deleteConversationById(conversationId: Int)
}