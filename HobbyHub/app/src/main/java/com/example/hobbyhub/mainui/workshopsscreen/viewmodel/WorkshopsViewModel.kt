package com.example.hobbyhub.mainui.workshopsscreen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.hobbyhub.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class Workshop(
    val id: Int,
    val title: String,
    val instructor: String,
    val date: String,
    val category: String,
    val image: Int
)

data class WorkshopsUiState(
    val searchQuery: String = "",
    val categories: List<String> = listOf("All", "Online", "In-Person", "Art", "Music"),
    val selectedCategory: String = "All",
    val upcomingWorkshops: List<Workshop> = emptyList(),
    val popularWorkshops: List<Workshop> = emptyList()
)

@HiltViewModel
class WorkshopsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(WorkshopsUiState())
    val uiState: StateFlow<WorkshopsUiState> = _uiState

    init {
        loadWorkshops()
    }

    private fun loadWorkshops() {
        _uiState.value = _uiState.value.copy(
            upcomingWorkshops = getUpcomingWorkshops(),
            popularWorkshops = getPopularWorkshops()
        )
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategorySelected(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    private fun getUpcomingWorkshops(): List<Workshop> {
        return listOf(
            Workshop(1, "Intro to Pottery", "Ana Martinez", "Oct 18, 2025", "Art", R.drawable.group1),
            Workshop(2, "Songwriting Basics", "Sam Jones", "Oct 22, 2025", "Music", R.drawable.group2),
            Workshop(3, "Digital Art with Procreate", "Chloe Chen", "Nov 5, 2025", "Online", R.drawable.group3)
        )
    }

    private fun getPopularWorkshops(): List<Workshop> {
        return listOf(
            Workshop(4, "Advanced Guitar", "Leo Fender", "Nov 12, 2025", "Music", R.drawable.group1),
            Workshop(5, "Live Cooking Class", "Maria Lopez", "Nov 15, 2025", "In-Person", R.drawable.group2),
            Workshop(6, "Abstract Painting", "John Doe", "Nov 20, 2025", "Art", R.drawable.group3)
        )
    }
}