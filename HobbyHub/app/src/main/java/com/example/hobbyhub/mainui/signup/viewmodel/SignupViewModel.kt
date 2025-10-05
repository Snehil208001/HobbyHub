package com.example.hobbyhub.mainui.signup.viewmodel


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// Data class to hold all mutable state for the screen
data class SignupUiState(
    val fullName: String = "",
    val email: String = "",
    val city: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val selectedHobbyTags: Set<String> = emptySet(),
    val isSignUpEnabled: Boolean = false // Example derived state for button enablement
)

@HiltViewModel
class SignupViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    val availableHobbyTags = listOf("Art", "Cycling", "Cooking", "Photography", "Gaming", "Hiking", "Reading", "Music")

    // --- Event Handlers for Form Fields ---

    fun onFullNameChange(newFullName: String) {
        _uiState.update { it.copy(fullName = newFullName) }
        validateForm()
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
        validateForm()
    }

    fun onCityChange(newCity: String) {
        _uiState.update { it.copy(city = newCity) }
        validateForm()
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
        validateForm()
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = newConfirmPassword) }
        validateForm()
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    fun toggleHobbyTag(tag: String) {
        _uiState.update { currentState ->
            val newTags = if (currentState.selectedHobbyTags.contains(tag)) {
                currentState.selectedHobbyTags - tag
            } else {
                currentState.selectedHobbyTags + tag
            }
            currentState.copy(selectedHobbyTags = newTags)
        }
    }

    // --- Action Handler ---
    fun onSignupClick() {
        // TODO: Implement actual signup logic (API calls, validation, navigation)
        println("Attempting signup with: ${_uiState.value}")
    }

    // --- Basic Validation Example (for button enable/disable) ---
    private fun validateForm() {
        val state = _uiState.value
        // Simple validation: check if email is present, password length is at least 6, and passwords match
        val isValid = state.email.isNotBlank() &&
                state.password.length >= 6 &&
                state.password == state.confirmPassword

        _uiState.update { it.copy(isSignUpEnabled = isValid) }
    }
}