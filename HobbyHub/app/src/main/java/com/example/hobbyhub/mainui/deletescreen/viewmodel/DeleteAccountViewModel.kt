package com.example.hobbyhub.mainui.deletescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hobbyhub.data.AuthRepository
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class DeleteUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val requiresRecentLogin: Boolean = false
)

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeleteUiState())
    val uiState = _uiState.asStateFlow()

    fun deleteAccount() {
        viewModelScope.launch {
            _uiState.value = DeleteUiState(isLoading = true)

            val user = authRepository.getCurrentUser()
            if (user == null) {
                _uiState.value = DeleteUiState(error = "No user logged in.")
                return@launch
            }

            try {
                // We call delete().await() to make it suspendable
                user.delete().await()
                // If await() completes without exception, it's successful
                _uiState.value = DeleteUiState(isLoading = false, isSuccess = true)
            } catch (e: Exception) {
                if (e is FirebaseAuthRecentLoginRequiredException) {
                    // Specific error for recent login
                    _uiState.value = DeleteUiState(isLoading = false, requiresRecentLogin = true, error = "Please sign out and sign back in to delete your account.")
                } else {
                    // Other errors
                    _uiState.value = DeleteUiState(isLoading = false, error = e.localizedMessage ?: "Failed to delete account.")
                }
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun resetState() {
        _uiState.value = DeleteUiState()
    }
}