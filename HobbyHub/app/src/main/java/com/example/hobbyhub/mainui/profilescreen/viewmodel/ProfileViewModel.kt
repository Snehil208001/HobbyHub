package com.example.hobbyhub.mainui.profilescreen.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class UserProfile(
    val name: String = "Ashfak Sayem",
    val profileImageUri: Uri? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile

    fun onProfileImageChange(uri: Uri?) {
        _userProfile.value = _userProfile.value.copy(profileImageUri = uri)
    }

    fun onNameChange(name: String) {
        _userProfile.value = _userProfile.value.copy(name = name)
    }
}