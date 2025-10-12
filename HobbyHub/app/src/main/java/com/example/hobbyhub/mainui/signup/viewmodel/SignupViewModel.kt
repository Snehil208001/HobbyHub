package com.example.hobbyhub.mainui.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hobbyhub.data.AuthRepository
import com.example.hobbyhub.data.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState = _uiState.asStateFlow()

    private val _signUpState = MutableStateFlow(SignInState())
    val signUpState = _signUpState.asStateFlow()

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
        viewModelScope.launch {
            _signUpState.update { it.copy(isLoading = true) }
            try {
                val state = _uiState.value
                if (state.password != state.confirmPassword) {
                    throw Exception("Passwords do not match.")
                }
                authRepository.createUserWithEmailAndPassword(state.email, state.password)
                _signUpState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _signUpState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun resetState() {
        _signUpState.value = SignInState()
    }
}