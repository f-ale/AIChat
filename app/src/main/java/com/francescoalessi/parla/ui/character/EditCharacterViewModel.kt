package com.francescoalessi.parla.ui.character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francescoalessi.parla.data.Character
import com.francescoalessi.parla.repositories.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val characterId:Int = savedStateHandle.get<Int>("characterId") ?: 0
    val character = characterRepository.getCharacterForId(characterId).filterNotNull()
    fun saveCharacter(character: Character) {
        viewModelScope.launch {
            characterRepository.saveCharacter(character)
            // TODO: Copy image to local storage
        }
    }

}