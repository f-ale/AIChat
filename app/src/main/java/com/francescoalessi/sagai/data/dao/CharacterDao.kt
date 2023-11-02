package com.francescoalessi.sagai.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.francescoalessi.sagai.data.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(characters: List<Character>)
    @Query("SELECT * FROM character WHERE id = :characterId LIMIT 1")
    fun getCharacterForId(characterId: Int): Flow<Character>
    @Query("SELECT * FROM character")
    fun getAllCharactersAsFlow(): Flow<List<Character>>
    @Delete
    suspend fun deleteCharacter(character: Character)
    @Query("DELETE FROM character WHERE id = :characterId")
    suspend fun deleteCharacterById(characterId: Int)
}