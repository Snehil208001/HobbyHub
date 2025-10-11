package com.example.hobbyhub.mainui.groupsscreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hobbyhub.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class Group(
    val id: Int,
    val name: String,
    val memberCount: Int,
    val category: String,
    val image: Int
)

data class GroupsUiState(
    val searchQuery: String = "",
    val categories: List<String> = listOf("All", "Sports", "Music", "Art", "Food"),
    val selectedCategory: String = "All",
    val popularGroups: List<Group> = emptyList(),
    val recommendedGroups: List<Group> = emptyList()
)

@HiltViewModel
class GroupsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(GroupsUiState())
    val uiState: StateFlow<GroupsUiState> = _uiState

    init {
        loadGroups()
    }

    private fun loadGroups() {
        _uiState.value = _uiState.value.copy(
            popularGroups = getPopularGroups(),
            recommendedGroups = getRecommendedGroups()
        )
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    private fun getPopularGroups(): List<Group> {
        return listOf(
            Group(1, "Urban Painters", 120, "Art", R.drawable.group1),
            Group(2, "City Strummers", 85, "Music", R.drawable.group2),
            Group(3, "Marathon Runners", 250, "Sports", R.drawable.group3)
        )
    }

    private fun getRecommendedGroups(): List<Group> {
        return listOf(
            Group(4, "Baking Enthusiasts", 75, "Food", R.drawable.group1),
            Group(5, "Trail Trekkers", 95, "Sports", R.drawable.group2),
            Group(6, "Indie Music Fans", 110, "Music", R.drawable.group3)
        )
    }
}