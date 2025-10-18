package com.example.hobbyhub.domain.model
// Represents details for a single hobby
data class HobbyDetail(
    val name: String = "",
    val proficiency: String = Proficiency.BEGINNER, // Default proficiency
    val seeking: String = "" // What the user is looking for (e.g., partners, mentor, group)
)

// Optional: Define constants for proficiency levels for consistency
object Proficiency {
    const val BEGINNER = "Beginner"
    const val INTERMEDIATE = "Intermediate"
    const val ADVANCED = "Advanced"
    const val EXPERT = "Expert"
    val levels = listOf(BEGINNER, INTERMEDIATE, ADVANCED, EXPERT)
}