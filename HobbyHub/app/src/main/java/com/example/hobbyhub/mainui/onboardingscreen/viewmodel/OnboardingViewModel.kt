package com.example.hobbyhub.mainui.onboardingscreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hobbyhub.domain.model.onBoardingPages
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {

    // Expose the list of onboarding pages to the UI
    val pages = onBoardingPages

    // In a real app, this function would handle saving a preference that onboarding is complete
    fun onOnboardingComplete() {
        // TODO: Save preference (e.g., using DataStore) that onboarding has been viewed.
    }
}