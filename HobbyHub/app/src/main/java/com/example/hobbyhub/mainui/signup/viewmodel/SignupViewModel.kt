package com.example.hobbyhub.mainui.signup.viewmodel

import androidx.core.util.PatternsCompat // Import for email validation
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
    // Removed city as it wasn't used in the UI
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    // Removed hobby tags as they weren't used in the provided UI
    val isSignUpEnabled: Boolean = false, // Start as disabled
    // Validation error messages (optional, can be displayed in UI)
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState = _uiState.asStateFlow()

    private val _signUpState = MutableStateFlow(SignInState())
    val signUpState = _signUpState.asStateFlow()

    // Removed availableHobbyTags

    fun onFullNameChange(newFullName: String) {
        // Clear error on change and validate
        _uiState.update { it.copy(fullName = newFullName, fullNameError = null) }
        validateForm()
    }

    fun onEmailChange(newEmail: String) {
        // Clear error on change and validate
        _uiState.update { it.copy(email = newEmail, emailError = null) }
        validateForm()
    }

    // Removed onCityChange

    fun onPasswordChange(newPassword: String) {
        // Clear error on change and validate
        _uiState.update { it.copy(password = newPassword, passwordError = null) }
        validateForm()
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        // Clear error on change and validate
        _uiState.update { it.copy(confirmPassword = newConfirmPassword, confirmPasswordError = null) }
        validateForm()
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    // Removed toggleHobbyTag

    private fun validateForm() {
        val state = _uiState.value
        var isFormValid = true
        var fullNameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        if (state.fullName.isBlank()) {
            fullNameError = "Full name cannot be empty."
            isFormValid = false
        }

        if (state.email.isBlank()) {
            emailError = "Email cannot be empty."
            isFormValid = false
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(state.email).matches()) {
            emailError = "Invalid email format."
            isFormValid = false
        }

        // Firebase default minimum password length is 6
        if (state.password.isBlank()) {
            passwordError = "Password cannot be empty."
            isFormValid = false
        } else if (state.password.length < 6) {
            passwordError = "Password must be at least 6 characters."
            isFormValid = false
        }

        if (state.confirmPassword.isBlank()) {
            confirmPasswordError = "Confirm password cannot be empty."
            isFormValid = false
        } else if (state.password != state.confirmPassword) {
            confirmPasswordError = "Passwords do not match."
            isFormValid = false
        }

        // Update UI state with validation results
        _uiState.update {
            it.copy(
                isSignUpEnabled = isFormValid,
                fullNameError = fullNameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }
    }


    fun onSignupClick() {
        // Re-validate just in case, although button state should handle it
        validateForm()
        val state = _uiState.value
        if (!state.isSignUpEnabled) {
            // Don't proceed if form is invalid
            return
        }

        viewModelScope.launch {
            _signUpState.update { it.copy(isLoading = true) }
            try {
                // Password match is already checked in validateForm
                authRepository.createUserWithEmailAndPassword(state.email, state.password)
                // If successful, update state
                _signUpState.update { it.copy(isLoading = false, isSuccess = true, error = null) }
            } catch (e: Exception) {
                // If error, update state
                _signUpState.update { it.copy(isLoading = false, isSuccess = false, error = e.localizedMessage ?: "An unknown error occurred") }
            }
        }
    }


    fun resetState() {
        _signUpState.value = SignInState()
        // Optionally reset UI fields as well if needed after an error
        // _uiState.value = SignupUiState()
    }
}