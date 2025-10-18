package com.example.hobbyhub.domain.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val hobbies: List<String> = emptyList(), // <-- ADDED
    val bio: String = ""                     // <-- ADDED
)