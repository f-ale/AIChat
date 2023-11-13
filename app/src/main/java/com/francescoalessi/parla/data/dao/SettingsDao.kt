package com.francescoalessi.parla.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.francescoalessi.parla.data.TextGenerationHost
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