package com.example.hobbyhub.mainui.login.viewmodel

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ResetPasswordUiState(
    val email: String = "",
    val isSendEnabled: Boolean = false, // Start disabled
    val emailError: String? = null, // Optional error message for UI
    val successMessage: String? = null, // To show confirmation message in UI
    val errorMessage: String? = null // To show general errors in UI
)

@HiltViewModel
class ResetPasswordViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    // Get Firebase Auth instance directly
    private val firebaseAuth = Firebase.auth

    fun onEmailChange(newEmail: String) {
        _uiState.update {
            it.copy(
                email = newEmail,
                emailError = null, // Clear errors on change
                successMessage = null, // Clear previous success message
                errorMessage = null // Clear previous error message
            )
        }
        // Validate form whenever email changes
        validateForm()
    }

    fun onSendClick() {
        validateForm() // Re-validate before sending
        val state = _uiState.value
        if (!state.isSendEnabled) {
            // Don't proceed if email is invalid
            return
        }

        // Disable button while sending request, clear previous messages
        _uiState.update { it.copy(isSendEnabled = false, successMessage = null, errorMessage = null) }

        // Send password reset email using Firebase Auth
        firebaseAuth.sendPasswordResetEmail(state.email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update state on success
                    _uiState.update {
                        it.copy(
                            successMessage = "Password reset email sent to ${state.email}. Check your inbox.",
                            errorMessage = null,
                            // Keep button disabled after success or re-enable? User choice.
                            // isSendEnabled = true // Example: Re-enable
                            email = "" // Clear email field after successful send
                        )
                    }
                    // Re-validate to disable button if email is now empty
                    validateForm()
                } else {
                    // Update state on failure
                    _uiState.update {
                        it.copy(
                            successMessage = null,
                            errorMessage = task.exception?.localizedMessage ?: "Failed to send reset email.",
                            isSendEnabled = true // Re-enable button on failure
                        )
                    }
                }
            }
    }

    private fun validateForm() {
        val state = _uiState.value
        var isValid = true
        var emailError: String? = null

        if (state.email.isBlank()) {
            emailError = "Email cannot be empty."
            isValid = false
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(state.email).matches()) {
            emailError = "Invalid email format."
            isValid = false
        }

        // Update UI state with validation results
        _uiState.update { it.copy(isSendEnabled = isValid, emailError = emailError) }
    }
}