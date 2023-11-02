package com.francescoalessi.sagai.repositories

import android.content.Context
import android.net.Uri
import com.francescoalessi.sagai.data.Character
import com.francescoalessi.sagai.data.dao.CharacterDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    @ApplicationContext private val  context: Context,
    private val characterDao: CharacterDao,
) {
    fun getAllCharacters(): Flow<List<Character>> =
        characterDao.getAllCharactersAsFlow()
    fun getCharacterForId(characterId: Int): Flow<Character> =
        characterDao.getCharacterForId(characterId)
    suspend fun saveCharacter(character: Character) {
        val characterToSave = character.copy(image =  copyUriToInternalStorage(character.image))
        characterDao.insert(characterToSave)
    }
    suspend fun deleteCharacter(character: Character) =
        characterDao.deleteCharacter(character)

    private fun copyUriToInternalStorage(uri: Uri?): Uri? {
        if(uri == null) return null

        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.filesDir, uri.lastPathSegment ?: "default_name")
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return Uri.fromFile(file)
    }
}