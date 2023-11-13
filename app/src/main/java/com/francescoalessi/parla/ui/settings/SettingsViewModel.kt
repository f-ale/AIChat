package com.francescoalessi.parla.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francescoalessi.parla.data.TextGenerationHost
import com.francescoalessi.parla.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
): ViewModel() {
    val textGenerationHost = settingsRepository.getTextGenerationHostAsFlow().filterNotNull()
    fun saveTextGenerationHost(textGenerationHost: TextGenerationHost) {
        viewModelScope.launch {
            settingsRepository.saveTextGenerationHost(textGenerationHost)
        }
    }
}