package com.francescoalessi.sagai.repositories

import com.francescoalessi.sagai.data.TextGenerationHost
import com.francescoalessi.sagai.data.dao.CharacterDao
import com.francescoalessi.sagai.data.dao.SettingsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.francescoalessi.sagai.data.Character

class CharacterRepository @Inject constructor(
    private val characterDao: CharacterDao,
) {
    fun getAllCharacters(): Flow<List<Character>> =
        characterDao.getAllCharactersAsFlow()
    fun getCharacterForId(characterId: Int): Flow<Character> =
        characterDao.getCharacterForId(characterId)
    suspend fun saveCharacter(character: Character) =
        characterDao.insert(character)
    suspend fun deleteCharacter(character: Character) =
        characterDao.deleteCharacter(character)
}