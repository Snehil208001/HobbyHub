package com.example.hobbyhub.mainui.messagescreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hobbyhub.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class Message(
    val sender: String,
    val text: String,
    val timestamp: String,
    val isFromCurrentUser: Boolean
)

data class Conversation(
    val id: Int,
    val participantName: String,
    val participantImage: Int,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int,
    val messages: List<Message>
)

data class MessageUiState(
    val conversations: List<Conversation> = emptyList(),
    val selectedConversation: Conversation? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class MessageViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MessageUiState())
    val uiState: StateFlow<MessageUiState> = _uiState

    init {
        loadConversations()
    }

    private fun loadConversations() {
        _uiState.value = _uiState.value.copy(conversations = getDummyConversations())
    }

    fun onConversationSelected(conversation: Conversation) {
        _uiState.value = _uiState.value.copy(selectedConversation = conversation)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    private fun getDummyConversations(): List<Conversation> {
        return listOf(
            Conversation(
                id = 1,
                participantName = "John Doe",
                participantImage = R.drawable.logo,
                lastMessage = "Hey, are we still on for the painting workshop?",
                timestamp = "10:45 AM",
                unreadCount = 2,
                messages = listOf(
                    Message("John Doe", "Hey!", "10:40 AM", false),
                    Message("Me", "Hi John!", "10:41 AM", true),
                    Message("John Doe", "Hey, are we still on for the painting workshop?", "10:45 AM", false)
                )
            ),
            Conversation(
                id = 2,
                participantName = "Urban Painters Group",
                participantImage = R.drawable.group1,
                lastMessage = "Don't forget to bring your own brushes!",
                timestamp = "Yesterday",
                unreadCount = 0,
                messages = emptyList()
            ),
            Conversation(
                id = 3,
                participantName = "Ana Martinez",
                participantImage = R.drawable.group2,
                lastMessage = "Thanks for the pottery tips!",
                timestamp = "Yesterday",
                unreadCount = 0,
                messages = emptyList()
            )
        )
    }
}