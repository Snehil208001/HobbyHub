package com.example.hobbyhub.mainui.groupsscreen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.example.hobbyhub.mainui.explorescreen.ui.CategoryFilters
import com.example.hobbyhub.mainui.explorescreen.ui.SearchBar
import com.example.hobbyhub.mainui.explorescreen.ui.SectionHeader
import com.example.hobbyhub.mainui.groupsscreen.viewmodel.Group
import com.example.hobbyhub.mainui.groupsscreen.viewmodel.GroupsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(
    navController: NavController,
    viewModel: GroupsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // --- Filtering Logic ---
    val filteredGroups = remember(uiState.selectedCategory, uiState.searchQuery) {
        val allGroups = uiState.popularGroups + uiState.recommendedGroups
        allGroups.filter { group ->
            val categoryMatch = uiState.selectedCategory == "All" || group.category.equals(uiState.selectedCategory, ignoreCase = true)
            val searchMatch = uiState.searchQuery.isBlank() || group.name.contains(uiState.searchQuery, ignoreCase = true)
            categoryMatch && searchMatch
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
        // The FloatingActionButton has been removed from here.
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .statusBarsPadding()
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
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SectionHeader("Popular Groups")
                }
                items(filteredGroups.filter { uiState.popularGroups.contains(it) }) { group ->
                    GroupCard(group = group)
                }
                item {
                    SectionHeader("Recommended For You")
                }
                items(filteredGroups.filter { uiState.recommendedGroups.contains(it) }) { group ->
                    GroupCard(group = group)
                }
            }
        }
    }
}

@Composable
fun GroupCard(group: Group) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = group.image),
                contentDescription = group.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = group.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Members",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${group.memberCount} Members",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            Button(onClick = { /* TODO: Handle join group */ }) {
                Text("Join")
            }
        }
    }
}