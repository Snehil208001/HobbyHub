// In snehil208001/hobbyhub/HobbyHub-102bbb5bfeae283b4c3e52ca5e13f3198e956095/HobbyHub/app/src/main/java/com/example/hobbyhub/domain/model/User.kt

package com.example.hobbyhub.domain.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val bio: String = "",
    val hobbies: List<String> = emptyList(),
    val location: String = "",
    val website: String = "",
    // --- ADDED ---
    val joinedDate: Long = 0L,
    val isPrivate: Boolean = false
    // --- END ADD ---
)