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
    val characterId:Int = 0,
    val conversationId:Int = 0,
    val content:String = "",
    val tokens:Int = content.length/4, // TODO: Change tokenization estimate with real count
    val timestamp:Long,
)