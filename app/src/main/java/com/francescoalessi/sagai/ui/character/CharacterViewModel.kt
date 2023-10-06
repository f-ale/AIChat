package com.francescoalessi.sagai.ui.character

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
class CharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
): ViewModel() {
    val characters = characterRepository.getAllCharacters()
    fun saveCharacter(character: Character) {
        viewModelScope.launch {
            characterRepository.saveCharacter(character)
        }
    }

    fun deleteCharacter(character: Character) {
        viewModelScope.launch {
            characterRepository.deleteCharacter(character)
        }
    }
}