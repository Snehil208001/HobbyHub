package com.example.hobbyhub.mainui.settingsscreen.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SettingsUiState(
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun onNotificationsToggled(isEnabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = isEnabled) }
    }

    fun onDarkModeToggled(isEnabled: Boolean) {
        _uiState.update { it.copy(darkModeEnabled = isEnabled) }
    }
}