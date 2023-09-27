package com.francescoalessi.sagai.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.francescoalessi.sagai.data.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert
    suspend fun insert(message: Message)
    @Delete
    suspend fun delete(message: Message)
    @Query("SELECT * FROM message WHERE conversationId = :conversationId ORDER BY timestamp DESC")
    fun getAllMessagesForConversation(conversationId: Int): Flow<List<Message>>
    @Query("SELECT * FROM message WHERE conversationId = :conversationId ORDER BY timestamp DESC")
    fun getAllMessagesForConversationAsPagingSource(conversationId: Int): PagingSource<Int, Message>
    @Query("SELECT * FROM message WHERE conversationId = :conversationId ORDER BY timestamp DESC LIMIT :amount")
    suspend fun getLatestMessagesForConversation(conversationId: Int, amount:Int = 1): List<Message>
}