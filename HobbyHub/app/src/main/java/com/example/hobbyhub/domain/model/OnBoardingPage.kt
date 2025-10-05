package com.example.hobbyhub.domain.model

import androidx.annotation.DrawableRes
import com.example.hobbyhub.R

data class OnBoardingPage(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
)

val onBoardingPages = listOf(
    OnBoardingPage(
        image = R.drawable.group3,
        title = "Explore Hobbies",
        description = "Find your passion. Discover a wide range of hobbies and activities tailored to your interests."
    ),
    OnBoardingPage(
        image = R.drawable.group2,
        title = "Join Local Groups",
        description = "Connect with like-minded people. Join local groups and communities to share your passion."
    ),
    OnBoardingPage(
        image = R.drawable.group1,
        title = "Attend Workshops",
        description = "Learn from the best. Sign up for workshops and classes to improve your skills."
    )
)