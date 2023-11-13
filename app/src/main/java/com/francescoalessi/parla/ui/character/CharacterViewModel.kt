package com.francescoalessi.parla.ui.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francescoalessi.parla.data.Character
import com.francescoalessi.parla.repositories.CharacterRepository
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