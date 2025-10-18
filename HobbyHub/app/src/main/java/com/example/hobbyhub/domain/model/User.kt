package com.example.hobbyhub.domain.model

// This data class defines the user's data in Firestore
data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val profileImageUrl: String = "" // We store the URL as a String
)