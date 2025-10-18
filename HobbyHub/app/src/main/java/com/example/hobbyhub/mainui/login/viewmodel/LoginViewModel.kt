package com.example.hobbyhub.mainui.login.viewmodel

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

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val rememberMe: Boolean = false,
    val isLoginEnabled: Boolean = false, // Start as disabled
    // Optional error messages
    val emailError: String? = null,
    val passwordError: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        // Clear error on change and validate
        _uiState.update { it.copy(email = newEmail, emailError = null) }
        validateForm()
    }

    fun onPasswordChange(newPassword: String) {
        // Clear error on change and validate
        _uiState.update { it.copy(password = newPassword, passwordError = null) }
        validateForm()
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onRememberMeToggle(isChecked: Boolean) {
        _uiState.update { it.copy(rememberMe = isChecked) }
        // No need to re-validate form for remember me toggle
    }

    private fun validateForm() {
        val state = _uiState.value
        var isFormValid = true
        var emailError: String? = null
        var passwordError: String? = null

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
        }
        // No minimum length check needed for login, just non-empty

        // Update UI state with validation results
        _uiState.update {
            it.copy(
                isLoginEnabled = isFormValid,
                emailError = emailError,
                passwordError = passwordError
            )
        }
    }


    fun onLoginClick() {
        validateForm() // Re-validate before attempting login
        val state = _uiState.value
        if (!state.isLoginEnabled) {
            // Don't proceed if form is invalid
            return
        }

        viewModelScope.launch {
            // Set loading state
            _signInState.update { it.copy(isLoading = true) }
            try {
                // Attempt sign in
                authRepository.signInWithEmailAndPassword(state.email, state.password)
                // Update state on success
                _signInState.update { it.copy(isLoading = false, isSuccess = true, error = null) }
            } catch (e: Exception) {
                // Update state on failure
                _signInState.update { it.copy(isLoading = false, isSuccess = false, error = e.localizedMessage ?: "Login failed") }
            }
        }
    }

    // Removed onGoogleSignIn function as requested previously

    fun resetState() {
        _signInState.value = SignInState()
        // Optionally reset UI fields if needed after error
        // _uiState.value = LoginUiState()
    }
}