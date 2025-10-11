package com.example.hobbyhub.mainui.workshopsscreen.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
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
import com.example.hobbyhub.mainui.workshopsscreen.viewmodel.Workshop
import com.example.hobbyhub.mainui.workshopsscreen.viewmodel.WorkshopsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkshopsScreen(
    navController: NavController,
    viewModel: WorkshopsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val filteredWorkshops = remember(uiState.selectedCategory, uiState.searchQuery) {
        val allWorkshops = uiState.upcomingWorkshops + uiState.popularWorkshops
        allWorkshops.filter { workshop ->
            val categoryMatch = uiState.selectedCategory == "All" ||
                    workshop.category.equals(uiState.selectedCategory, ignoreCase = true) ||
                    (uiState.selectedCategory == "Online" && workshop.category == "Online") ||
                    (uiState.selectedCategory == "In-Person" && workshop.category == "In-Person")

            val searchMatch = uiState.searchQuery.isBlank() ||
                    workshop.title.contains(uiState.searchQuery, ignoreCase = true) ||
                    workshop.instructor.contains(uiState.searchQuery, ignoreCase = true)

            categoryMatch && searchMatch
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
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
                    SectionHeader("Upcoming Workshops")
                }
                items(filteredWorkshops.filter { uiState.upcomingWorkshops.contains(it) }) { workshop ->
                    WorkshopCard(workshop = workshop)
                }

                item {
                    SectionHeader("Popular Workshops")
                }
                items(filteredWorkshops.filter { uiState.popularWorkshops.contains(it) }) { workshop ->
                    WorkshopCard(workshop = workshop)
                }
            }
        }
    }
}

@Composable
fun WorkshopCard(workshop: Workshop) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Image(
                painter = painterResource(id = workshop.image),
                contentDescription = workshop.title,
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = workshop.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(icon = Icons.Default.Person, text = workshop.instructor)
                Spacer(modifier = Modifier.height(4.dp))
                InfoRow(icon = Icons.Default.CalendarToday, text = workshop.date)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* TODO: Handle booking */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Book Now")
                }
            }
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = Color.Gray, fontSize = 14.sp)
    }
}