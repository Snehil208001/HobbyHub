package com.example.hobbyhub.mainui.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignupUiState(
    val fullName: String = "",
    val email: String = "",
    val city: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val selectedHobbyTags: Set<String> = emptySet(),
    val isSignUpEnabled: Boolean = true
)

@HiltViewModel
class SignupViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome = _navigateToHome.asStateFlow()

    val availableHobbyTags = listOf("Art", "Cycling", "Cooking", "Photography", "Gaming", "Hiking", "Reading", "Music")

    fun onFullNameChange(newFullName: String) {
        _uiState.update { it.copy(fullName = newFullName) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onCityChange(newCity: String) {
        _uiState.update { it.copy(city = newCity) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = newConfirmPassword) }
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

    fun onSignupClick() {
        // In a real app, you would perform registration here.
        viewModelScope.launch {
            _navigateToHome.value = true
        }
    }

    fun onNavigatedToHome() {
        _navigateToHome.value = false
    }
}