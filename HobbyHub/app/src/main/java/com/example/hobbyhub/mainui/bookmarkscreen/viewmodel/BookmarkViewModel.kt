package com.example.hobbyhub.mainui.bookmarkscreen.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Build
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.example.hobbyhub.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

sealed class BookmarkableItem(
    open val id: Int,
    open val title: String,
    open val subtitle: String,
    open val image: Int,
    open val icon: ImageVector
)

data class BookmarkedHobby(
    override val id: Int,
    override val title: String,
    override val subtitle: String,
    override val image: Int,
    override val icon: ImageVector = Icons.Default.Bookmark
) : BookmarkableItem(id, title, subtitle, image, icon)

data class BookmarkedGroup(
    override val id: Int,
    override val title: String,
    override val subtitle: String,
    override val image: Int,
    override val icon: ImageVector = Icons.Default.Group
) : BookmarkableItem(id, title, subtitle, image, icon)

data class BookmarkedWorkshop(
    override val id: Int,
    override val title: String,
    override val subtitle: String,
    override val image: Int,
    override val icon: ImageVector = Icons.Default.Build
) : BookmarkableItem(id, title, subtitle, image, icon)

data class BookmarkUiState(
    val bookmarks: List<BookmarkableItem> = emptyList()
)

@HiltViewModel
class BookmarkViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BookmarkUiState())
    val uiState: StateFlow<BookmarkUiState> = _uiState

    init {
        loadBookmarks()
    }

    private fun loadBookmarks() {
        _uiState.value = _uiState.value.copy(
            bookmarks = listOf(
                BookmarkedHobby(1, "Painting", "Art", R.drawable.group2),
                BookmarkedGroup(2, "Urban Painters", "120 Members", R.drawable.group1),
                BookmarkedWorkshop(3, "Intro to Pottery", "Ana Martinez", R.drawable.group3)
            )
        )
    }
}