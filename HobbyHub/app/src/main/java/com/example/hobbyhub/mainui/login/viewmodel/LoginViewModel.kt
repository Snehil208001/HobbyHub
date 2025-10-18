package com.example.hobbyhub.mainui.login.viewmodel

import android.content.Context // Import Context
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hobbyhub.core.utils.PreferencesHelper // Import PreferencesHelper
import com.example.hobbyhub.data.AuthRepository
import com.example.hobbyhub.data.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext // Import ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// LoginUiState remains the same
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val rememberMe: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context // Inject Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    // --- Load saved state on initialization ---
    init {
        val shouldRemember = PreferencesHelper.shouldRememberMe(context)
        val savedEmail = PreferencesHelper.getSavedEmail(context)
        val savedPassword = PreferencesHelper.getSavedPassword(context) // !! Insecure !!

        _uiState.update {
            it.copy(
                rememberMe = shouldRemember,
                email = if (shouldRemember && savedEmail != null) savedEmail else "",
                password = if (shouldRemember && savedPassword != null) savedPassword else "" // !! Insecure !!
            )
        }
        // Validate immediately in case loaded credentials are valid
        if (shouldRemember && !savedEmail.isNullOrBlank() && !savedPassword.isNullOrBlank()) {
            validateForm()
        }
    }
    // --- End Load ---

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail, emailError = null) }
        validateForm()
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword, passwordError = null) }
        validateForm()
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onRememberMeToggle(isChecked: Boolean) {
        _uiState.update { it.copy(rememberMe = isChecked) }
        // --- Save preference immediately ---
        PreferencesHelper.setRememberMe(context, isChecked)
        // If unchecked, clear saved credentials immediately
        if (!isChecked) {
            PreferencesHelper.clearCredentials(context)
        }
        // --- End Save ---
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
            return
        }

        // --- Save or Clear Credentials before login attempt ---
        if (state.rememberMe) {
            PreferencesHelper.saveCredentials(context, state.email, state.password) // !! Insecure !!
        } else {
            // This case might already be handled in onRememberMeToggle,
            // but clearing here ensures consistency if the toggle state changed
            // without triggering the callback somehow.
            PreferencesHelper.clearCredentials(context)
        }
        // --- End Save/Clear ---

        viewModelScope.launch {
            _signInState.update { it.copy(isLoading = true) }
            try {
                authRepository.signInWithEmailAndPassword(state.email, state.password)
                _signInState.update { it.copy(isLoading = false, isSuccess = true, error = null) }
            } catch (e: Exception) {
                _signInState.update { it.copy(isLoading = false, isSuccess = false, error = e.localizedMessage ?: "Login failed") }
            }
        }
    }

    fun resetState() {
        _signInState.value = SignInState()
    }
}