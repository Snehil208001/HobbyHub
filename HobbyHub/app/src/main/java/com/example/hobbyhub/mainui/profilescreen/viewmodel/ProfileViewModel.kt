package com.example.hobbyhub.mainui.profilescreen.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hobbyhub.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Correct the data class state
data class UserProfile(
    val name: String = "",
    val email: String = "",
    val databaseImageUrl: String = "", // URL from Firestore
    val newSelectedImageUri: Uri? = null // Locally selected image for changing
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            // 2. Fetch the full User object from the repository
            val firestoreUser = authRepository.getUserProfile()
            if (firestoreUser != null) {
                _userProfile.update {
                    it.copy(
                        name = firestoreUser.fullName,
                        email = firestoreUser.email,
                        databaseImageUrl = firestoreUser.profileImageUrl // 3. Load the URL
                    )
                }
            }
        }
    }

    // This is for when the user picks a new image *on this screen*
    fun onProfileImageChange(uri: Uri?) {
        _userProfile.value = _userProfile.value.copy(newSelectedImageUri = uri)
    }

    // This function is no longer needed here, name is loaded from DB
    // fun onNameChange(name: String) { ... }

    fun signOut() {
        authRepository.signOut()
    }
}