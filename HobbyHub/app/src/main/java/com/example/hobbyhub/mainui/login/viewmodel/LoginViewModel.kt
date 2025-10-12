package com.example.hobbyhub.mainui.login.viewmodel

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
    val isLoginEnabled: Boolean = true
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
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onRememberMeToggle(isChecked: Boolean) {
        _uiState.update { it.copy(rememberMe = isChecked) }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _signInState.update { it.copy(isLoading = true) }
            try {
                authRepository.signInWithEmailAndPassword(_uiState.value.email, _uiState.value.password)
                _signInState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _signInState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onGoogleSignIn(idToken: String) {
        viewModelScope.launch {
            _signInState.update { it.copy(isLoading = true) }
            try {
                authRepository.signInWithGoogle(idToken)
                _signInState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _signInState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun resetState() {
        _signInState.value = SignInState()
    }
}