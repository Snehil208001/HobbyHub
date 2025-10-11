package com.example.hobbyhub.mainui.helpscreen.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class FaqItem(
    val id: Int,
    val question: String,
    val answer: String
)

data class HelpUiState(
    val faqs: List<FaqItem> = emptyList()
)

@HiltViewModel
class HelpViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HelpUiState())
    val uiState: StateFlow<HelpUiState> = _uiState.asStateFlow()

    init {
        loadFaqs()
    }

    private fun loadFaqs() {
        _uiState.value = _uiState.value.copy(faqs = getDummyFaqs())
    }

    private fun getDummyFaqs(): List<FaqItem> {
        return listOf(
            FaqItem(
                id = 1,
                question = "How do I join a group?",
                answer = "To join a group, simply navigate to the 'Groups' screen, find a group you're interested in, and tap the 'Join' button. Some groups may require approval from the group admin."
            ),
            FaqItem(
                id = 2,
                question = "How can I create my own hobby group?",
                answer = "Currently, the ability to create groups is being developed and will be available in a future update. Stay tuned!"
            ),
            FaqItem(
                id = 3,
                question = "How do I reset my password?",
                answer = "On the login screen, tap on the 'Forgot Password?' link. You will be prompted to enter your email address, and a password reset link will be sent to you."
            ),
            FaqItem(
                id = 4,
                question = "Is my personal information secure?",
                answer = "Yes, we take your privacy very seriously. Please refer to our Privacy Policy in the settings menu for detailed information on how we collect, use, and protect your data."
            ),
            FaqItem(
                id = 5,
                question = "How do I book a workshop?",
                answer = "Go to the 'Workshops' screen, select a workshop you'd like to attend, and click the 'Book Now' button. You will be guided through the payment and confirmation process."
            )
        )
    }
}