package com.example.hobbyhub.mainui.calendarscreen.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class Event(
    val date: LocalDate,
    val title: String,
    val time: String
)

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentMonth: YearMonth = YearMonth.now(),
    val events: List<Event> = emptyList()
)

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    init {
        loadEvents()
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }

    fun onMonthChanged(yearMonth: YearMonth) {
        _uiState.value = _uiState.value.copy(currentMonth = yearMonth)
    }

    private fun loadEvents() {
        // In a real app, you would fetch these from a database or API
        _uiState.value = _uiState.value.copy(
            events = listOf(
                Event(LocalDate.now().plusDays(2), "Pottery Workshop", "10:00 AM - 1:00 PM"),
                Event(LocalDate.now().plusDays(2), "Guitar Class", "3:00 PM - 4:00 PM"),
                Event(LocalDate.now().plusDays(5), "Baking Enthusiasts Meetup", "2:00 PM"),
                Event(LocalDate.now().minusDays(3), "Urban Painters Gathering", "11:00 AM")
            )
        )
    }
}