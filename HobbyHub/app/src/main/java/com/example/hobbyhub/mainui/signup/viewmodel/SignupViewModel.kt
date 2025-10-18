package com.example.hobbyhub.mainui.signup.viewmodel

import android.net.Uri // Import Uri
import androidx.core.util.PatternsCompat
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
    val password: String = "",
    val confirmPassword: String = "",
    val imageUri: Uri? = null, // Add imageUri
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val isSignUpEnabled: Boolean = false,
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

    // --- ADD THIS FUNCTION ---
    fun onImageUriChange(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }
    // --- END ---

    fun onFullNameChange(newFullName: String) {
        _uiState.update { it.copy(fullName = newFullName, fullNameError = null) }
        validateForm()
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, emailError = null) }
        validateForm()
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, passwordError = null) }
        validateForm()
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = newConfirmPassword, confirmPasswordError = null) }
        validateForm()
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }
    }

    private fun validateForm() {
        val state = _uiState.value
        // ... (validation logic is unchanged)
        // ...
        // ...
        // Note: You can add validation for imageUri if it's mandatory

        // --- THIS FUNCTION'S CONTENT IS UNCHANGED ---
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
        _uiState.update {
            it.copy(
                isSignUpEnabled = isFormValid,
                fullNameError = fullNameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }
        // --- END OF UNCHANGED FUNCTION ---
    }


    fun onSignupClick() {
        validateForm()
        val state = _uiState.value
        if (!state.isSignUpEnabled) {
            return
        }

        viewModelScope.launch {
            _signUpState.update { it.copy(isLoading = true) }
            try {
                // --- UPDATE THIS CALL ---
                authRepository.createUserWithEmailAndPassword(
                    fullName = state.fullName,
                    email = state.email,
                    password = state.password,
                    imageUri = state.imageUri // Pass the imageUri
                )
                // --- END UPDATE ---
                _signUpState.update { it.copy(isLoading = false, isSuccess = true, error = null) }
            } catch (e: Exception) {
                _signUpState.update { it.copy(isLoading = false, isSuccess = false, error = e.localizedMessage ?: "An unknown error occurred") }
            }
        }
    }

    fun resetState() {
        _signUpState.value = SignInState()
    }
}