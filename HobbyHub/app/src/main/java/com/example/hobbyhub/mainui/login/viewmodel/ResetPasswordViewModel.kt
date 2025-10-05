package com.example.hobbyhub.mainui.login.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ResetPasswordUiState(
    val email: String = "",
    val isSendEnabled: Boolean = false
)

@HiltViewModel
class ResetPasswordViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
        validateForm()
    }

    fun onSendClick() {
        // TODO: Implement the actual logic to send the password reset email via a repository/service.
        println("Sending reset password link to: ${_uiState.value.email}")
    }

    private fun validateForm() {
        val isValid = _uiState.value.email.isNotBlank() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()

        _uiState.update { it.copy(isSendEnabled = isValid) }
    }
}