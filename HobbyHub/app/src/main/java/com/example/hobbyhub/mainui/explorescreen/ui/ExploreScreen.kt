package com.example.hobbyhub.mainui.explorescreen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hobbyhub.core.utils.navigationbar.BottomNavigationBar
import com.example.hobbyhub.mainui.explorescreen.viewmodel.ExploreViewModel
import com.example.hobbyhub.mainui.explorescreen.viewmodel.Hobby
import com.example.hobbyhub.ui.theme.EventHubPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // --- Filtering Logic ---
    val hobbiesToShow = remember(uiState.selectedCategory, uiState.searchQuery) {
        val allHobbies = mapOf(
            "Trending" to uiState.trendingHobbies,
            "New" to uiState.newHobbies,
            "For You" to uiState.recommendedHobbies
        )

        if (uiState.selectedCategory == "All" && uiState.searchQuery.isBlank()) {
            return@remember allHobbies
        }

        val filteredMap = mutableMapOf<String, List<Hobby>>()

        allHobbies.forEach { (section, hobbies) ->
            val filteredList = hobbies.filter { hobby ->
                val categoryMatch = uiState.selectedCategory == "All" || hobby.category.equals(uiState.selectedCategory, ignoreCase = true)
                // Updated search logic to include category and description
                val searchMatch = uiState.searchQuery.isBlank() ||
                        hobby.name.contains(uiState.searchQuery, ignoreCase = true) ||
                        hobby.category.contains(uiState.searchQuery, ignoreCase = true) ||
                        hobby.description.contains(uiState.searchQuery, ignoreCase = true)
                categoryMatch && searchMatch
            }
            if (filteredList.isNotEmpty()) {
                filteredMap[section] = filteredList
            }
        }
        filteredMap
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .statusBarsPadding() // Adds padding for the status bar
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChanged = viewModel::onSearchQueryChanged
            )
            CategoryFilters(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = viewModel::onCategorySelected
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Iterate over the filtered map
                hobbiesToShow.forEach { (section, hobbies) ->
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        SectionHeader(section)
                    }
                    items(hobbies) { hobby ->
                        HobbyCard(hobby = hobby)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Search hobbies...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilters(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            val isSelected = selectedCategory == category
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = EventHubPrimary,
                    selectedLabelColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            )
        }
    }
}

@Composable
fun HobbyCard(hobby: Hobby) {
    Card(
        modifier = Modifier.clickable { /* Handle hobby click */ },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Image(
                painter = painterResource(id = hobby.image),
                contentDescription = hobby.name,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = hobby.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = hobby.category, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    )
}