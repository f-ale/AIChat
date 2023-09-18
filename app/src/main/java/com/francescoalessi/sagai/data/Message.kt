package com.francescoalessi.sagai.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(
    /*foreignKeys = [
        ForeignKey(
            entity = Character::class,
            parentColumns = ["id"],
            childColumns = ["characterId"],
            onDelete = CASCADE // TODO: Review ondelete policy
        ),
    ForeignKey(
            entity = Conversation::class,
            parentColumns = ["id"],
            childColumns = ["conversationId"],
            onDelete = CASCADE // TODO: Review ondelete policy
        ),
    ]*/
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    val characterId:Int = -1,
    val conversationId:Int = -1,
    val content:String = "",
    val timestamp:Long,
)

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
}
