package com.francescoalessi.sagai.ui.character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francescoalessi.sagai.data.Character
import com.francescoalessi.sagai.data.TextGenerationHost
import com.francescoalessi.sagai.repositories.CharacterRepository
import com.francescoalessi.sagai.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val characterId:Int = savedStateHandle.get<Int>("characterId") ?: 0
    val character = characterRepository.getCharacterForId(characterId)
    fun saveCharacter(character: Character) {
        viewModelScope.launch {
            characterRepository.saveCharacter(character)
        }
    }
}