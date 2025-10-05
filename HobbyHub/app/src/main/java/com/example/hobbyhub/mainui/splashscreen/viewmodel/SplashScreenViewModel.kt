package com.example.hobbyhub.mainui.splashscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sealed class to define possible navigation events from the ViewModel
 */
sealed class SplashScreenEvent {
    object NavigateToOnboarding : SplashScreenEvent()
    object None : SplashScreenEvent()
}

@HiltViewModel
class SplashScreenViewModel @Inject constructor() : ViewModel() {

    // State to control the visibility/animation start (collected by Composable)
    private val _startAnimation = MutableStateFlow(false)
    val startAnimation: StateFlow<Boolean> = _startAnimation

    // Navigation event to tell the UI when to navigate (collected by Composable)
    private val _navigationEvent = MutableStateFlow<SplashScreenEvent>(SplashScreenEvent.None)
    val navigationEvent: StateFlow<SplashScreenEvent> = _navigationEvent

    init {
        startSplashTimer()
    }

    private fun startSplashTimer() {
        viewModelScope.launch {
            // Start the visual animation immediately
            _startAnimation.value = true

            // Wait for 2 seconds (matching original delay)
            delay(2000L)

            // Signal the UI to navigate
            _navigationEvent.value = SplashScreenEvent.NavigateToOnboarding
        }
    }
}