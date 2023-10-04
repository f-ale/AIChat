package com.francescoalessi.sagai.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.francescoalessi.sagai.data.Character

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character)
    @Query("SELECT * FROM character WHERE id = :characterId LIMIT 1")
    fun getCharacterForId(characterId: Int): Flow<Character>
    @Query("SELECT * FROM character")
    fun getAllCharactersAsFlow(): Flow<List<Character>>
}