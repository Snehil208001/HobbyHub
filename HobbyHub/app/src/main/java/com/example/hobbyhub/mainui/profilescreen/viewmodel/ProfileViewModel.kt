package com.example.hobbyhub.mainui.profilescreen.viewmodel

import android.util.Log // <-- Add Log import
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hobbyhub.data.AuthRepository
import com.example.hobbyhub.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
    // --- State for editing ---
    val isEditing: Boolean = false, // To potentially show/hide edit fields in ProfileScreen itself (alternative approach)
    val editedBio: String = "",
    val editedHobbies: List<String> = emptyList(),
    val updateInProgress: Boolean = false,
    val updateError: String? = null,
    val updateSuccess: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() { // Make it public to refresh after edit
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, updateSuccess = false, updateError = null) } // Reset update status
            try {
                val userProfile = authRepository.getUserProfile()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = userProfile,
                        // Initialize edit state when loading
                        editedBio = userProfile?.bio ?: "",
                        editedHobbies = userProfile?.hobbies ?: emptyList(),
                        error = null
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading profile", e) // Log error
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Failed to load profile"
                    )
                }
            }
        }
    }

    // --- Add functions for editing ---
    fun onBioChange(newBio: String) {
        _uiState.update { it.copy(editedBio = newBio) }
    }

    fun onHobbyAdded(hobby: String) {
        // Trim whitespace and check if not blank and not already present
        val trimmedHobby = hobby.trim()
        if (trimmedHobby.isNotBlank() && !_uiState.value.editedHobbies.contains(trimmedHobby)) {
            _uiState.update {
                it.copy(editedHobbies = it.editedHobbies + trimmedHobby) // Add trimmed hobby
            }
        }
    }

    fun onHobbyRemoved(hobby: String) {
        _uiState.update {
            it.copy(editedHobbies = it.editedHobbies - hobby)
        }
    }

    fun saveProfileChanges() {
        val currentState = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(updateInProgress = true, updateError = null, updateSuccess = false) }
            try {
                authRepository.updateProfile(
                    bio = currentState.editedBio.trim(), // Trim bio before saving
                    hobbies = currentState.editedHobbies // Already trimmed when added
                )
                _uiState.update { it.copy(updateInProgress = false, updateSuccess = true) }
                // Optionally reload profile data right after saving - handled by LaunchedEffect in ProfileScreen
                // loadUserProfile()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error saving profile", e) // Log error
                _uiState.update {
                    it.copy(
                        updateInProgress = false,
                        updateError = e.localizedMessage ?: "Failed to save profile",
                        updateSuccess = false
                    )
                }
            }
        }
    }
    // --- End Add functions ---

    fun onSignOutClick(onSignedOut: () -> Unit) {
        authRepository.signOut()
        onSignedOut()
    }
}