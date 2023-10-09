package com.francescoalessi.sagai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.francescoalessi.sagai.data.Conversation
import com.francescoalessi.sagai.data.TextGenerationHost
import com.francescoalessi.sagai.data.relations.ConversationWithCharacter
import com.francescoalessi.sagai.data.relations.ConversationWithMessagesAndCharacter
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insert(textGenerationHost: TextGenerationHost)
    @Query("SELECT * FROM textgenerationhost LIMIT 1")
    suspend fun getTextGenerationHost(): TextGenerationHost?
    @Query("SELECT * FROM textgenerationhost LIMIT 1")
    fun getTextGenerationHostAsFlow(): Flow<TextGenerationHost>
}