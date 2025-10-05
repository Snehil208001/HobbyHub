package com.example.hobbyhub.mainui.login.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val rememberMe: Boolean = false,
    val isLoginEnabled: Boolean = true // UPDATED: Button is now enabled by default
)

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    // --- Event Handlers for Form Fields ---

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
        // validateForm() // REMOVED: No longer needed to enable/disable button
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
        // validateForm() // REMOVED: No longer needed to enable/disable button
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onRememberMeToggle(isChecked: Boolean) {
        _uiState.update { it.copy(rememberMe = isChecked) }
    }

    // --- Action Handler ---
    fun onLoginClick() {
        // TODO: Implement actual login logic (API calls, validation, navigation)
        // You might want to add form validation here before proceeding
        println("Attempting login for user: ${_uiState.value.email}")
    }

    // --- Basic Validation Example (for button enable/disable) ---
    private fun validateForm() {
        val state = _uiState.value
        // Simple validation: check if both fields are non-empty
        val isValid = state.email.isNotBlank() && state.password.isNotBlank()

        _uiState.update { it.copy(isLoginEnabled = isValid) }
    }
}