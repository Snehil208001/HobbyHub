package com.example.hobbyhub.mainui.filterscreen.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class FilterUiState(
    val categories: List<String> = listOf("Sports", "Music", "Art", "Food"),
    val selectedCategory: String? = null,
    val timeAndDateOptions: List<String> = listOf("Today", "Tomorrow", "This week"),
    val selectedTimeAndDate: String = "Tomorrow",
    val priceRange: ClosedFloatingPointRange<Float> = 20f..120f
)

@HiltViewModel
class FilterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState

    fun onCategorySelected(category: String) {
        _uiState.update { currentState ->
            val newCategory = if (currentState.selectedCategory == category) null else category
            currentState.copy(selectedCategory = newCategory)
        }
    }

    fun onTimeAndDateSelected(timeAndDate: String) {
        _uiState.update { it.copy(selectedTimeAndDate = timeAndDate) }
    }

    fun onPriceRangeChanged(newRange: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(priceRange = newRange) }
    }

    fun onResetClicked() {
        _uiState.value = FilterUiState() // Reset to initial state
    }
}