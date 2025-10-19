// In snehil208001/hobbyhub/HobbyHub-102bbb5bfeae283b4c3e52ca5e13f3198e956095/HobbyHub/app/src/main/java/com/example/hobbyhub/mainui/profilescreen/viewmodel/ProfileViewModel.kt

package com.example.hobbyhub.mainui.profilescreen.viewmodel

import android.net.Uri
import android.util.Log
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
    val isEditing: Boolean = false,
    val editedBio: String = "",
    val editedHobbies: List<String> = emptyList(),
    val editedLocation: String = "",
    val editedWebsite: String = "",
    val editedIsPrivate: Boolean = false,
    val updateInProgress: Boolean = false,
    val updateError: String? = null,
    val updateSuccess: Boolean = false,
    val selectedImageUri: Uri? = null,
    // --- ADDED ---
    val allHobbiesList: List<String> = emptyList() // This will hold the predefined list
    // --- END ADD ---
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // --- ADDED: Predefined list of hobbies ---
    private val allHobbiesList: List<String> = listOf(
        "Photography", "Hiking", "Reading", "Cooking", "Gaming", "Painting",
        "Coding", "Gardening", "Cycling", "Traveling", "Fishing", "Yoga",
        "Writing", "Music", "Crafting", "Running", "Dancing", "Swimming"
    )
    // --- END ADD ---

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, updateSuccess = false, updateError = null, selectedImageUri = null) }
            try {
                val userProfile = authRepository.getUserProfile()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = userProfile,
                        editedBio = userProfile?.bio ?: "",
                        editedHobbies = userProfile?.hobbies ?: emptyList(),
                        editedLocation = userProfile?.location ?: "",
                        editedWebsite = userProfile?.website ?: "",
                        editedIsPrivate = userProfile?.isPrivate ?: false,
                        allHobbiesList = this@ProfileViewModel.allHobbiesList, // <-- ADDED
                        error = null,
                        selectedImageUri = null
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error loading profile", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Failed to load profile"
                    )
                }
            }
        }
    }

    // --- Functions for editing ---
    fun onBioChange(newBio: String) {
        _uiState.update { it.copy(editedBio = newBio) }
    }

    // --- REMOVED: onHobbyAdded and onHobbyRemoved ---

    // --- ADDED: New function to toggle hobby selection ---
    fun onHobbyToggle(hobby: String) {
        _uiState.update { currentState ->
            val currentHobbies = currentState.editedHobbies
            val newHobbies = if (currentHobbies.contains(hobby)) {
                currentHobbies - hobby // Remove hobby
            } else {
                currentHobbies + hobby // Add hobby
            }
            currentState.copy(editedHobbies = newHobbies)
        }
    }
    // --- END ADD ---

    fun onProfileImageChanged(uri: Uri?) {
        _uiState.update { it.copy(selectedImageUri = uri, updateSuccess = false, updateError = null) }
    }

    fun onLocationChange(newLocation: String) {
        _uiState.update { it.copy(editedLocation = newLocation) }
    }

    fun onWebsiteChange(newWebsite: String) {
        _uiState.update { it.copy(editedWebsite = newWebsite) }
    }

    fun onPrivacyChange(isPrivate: Boolean) {
        _uiState.update { it.copy(editedIsPrivate = isPrivate) }
    }

    fun saveProfileChanges() {
        val currentState = _uiState.value
        val uid = authRepository.getCurrentUser()?.uid ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(updateInProgress = true, updateError = null, updateSuccess = false) }
            try {
                // Check if any text fields or the toggle changed
                val bioChanged = currentState.user?.bio != currentState.editedBio.trim()
                val hobbiesChanged = currentState.user?.hobbies != currentState.editedHobbies
                val locationChanged = currentState.user?.location != currentState.editedLocation.trim()
                val websiteChanged = currentState.user?.website != currentState.editedWebsite.trim()
                val privacyChanged = currentState.user?.isPrivate != currentState.editedIsPrivate

                if (bioChanged || hobbiesChanged || locationChanged || websiteChanged || privacyChanged) {
                    authRepository.updateProfile(
                        bio = currentState.editedBio.trim(),
                        hobbies = currentState.editedHobbies,
                        location = currentState.editedLocation.trim(),
                        website = currentState.editedWebsite.trim(),
                        isPrivate = currentState.editedIsPrivate
                    )
                }

                // Check if image changed
                currentState.selectedImageUri?.let { uri ->
                    authRepository.updateUserProfileImage(uid, uri)
                }

                _uiState.update { it.copy(updateInProgress = false, updateSuccess = true, selectedImageUri = null) }

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error saving profile", e)
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

    fun resetUpdateStatus() {
        _uiState.update { it.copy(updateError = null, updateSuccess = false, updateInProgress = false) }
    }

    fun onSignOutClick(onSignedOut: () -> Unit) {
        authRepository.signOut()
        onSignedOut()
    }
}