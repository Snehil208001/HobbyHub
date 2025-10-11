package com.example.hobbyhub.mainui.explorescreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hobbyhub.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class Hobby(
    val id: Int,
    val name: String,
    val category: String,
    val description: String, // Added description
    val image: Int
)

data class ExploreUiState(
    val searchQuery: String = "",
    val categories: List<String> = listOf("All", "Sports", "Music", "Art", "Food"),
    val selectedCategory: String = "All",
    val trendingHobbies: List<Hobby> = emptyList(),
    val newHobbies: List<Hobby> = emptyList(),
    val recommendedHobbies: List<Hobby> = emptyList()
)

@HiltViewModel
class ExploreViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState

    init {
        loadHobbies()
    }

    private fun loadHobbies() {
        _uiState.value = _uiState.value.copy(
            trendingHobbies = getTrendingHobbies(),
            newHobbies = getNewHobbies(),
            recommendedHobbies = getRecommendedHobbies()
        )
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    // Updated hobby lists with descriptions
    private fun getTrendingHobbies(): List<Hobby> {
        return listOf(
            Hobby(1, "Guitar", "Music", "Learn to play acoustic and electric guitar.", R.drawable.group1),
            Hobby(2, "Painting", "Art", "Express your creativity with oil and watercolor.", R.drawable.group2),
            Hobby(3, "Baking", "Food", "Create delicious cakes, breads, and pastries.", R.drawable.group3),
        )
    }

    private fun getNewHobbies(): List<Hobby> {
        return listOf(
            Hobby(4, "Yoga", "Sports", "Improve flexibility and mindfulness.", R.drawable.group1),
            Hobby(5, "DJing", "Music", "Mix tracks and create your own beats.", R.drawable.group2),
            Hobby(6, "Sculpting", "Art", "Shape clay and other materials into 3D art.", R.drawable.group3),
        )
    }

    private fun getRecommendedHobbies(): List<Hobby> {
        return listOf(
            Hobby(7, "Photography", "Art", "Capture moments and tell stories with your camera.", R.drawable.group1),
            Hobby(8, "Singing", "Music", "Develop your vocal skills and performance.", R.drawable.group2),
            Hobby(9, "Running", "Sports", "Join a local club and improve your cardio.", R.drawable.group3),
        )
    }
}