package com.francescoalessi.parla.ui.conversation

sealed class UiState {
    data object Idle : UiState()
    data object Success : UiState()
    data class Error(val message: String) : UiState()
    data object Loading : UiState()
}