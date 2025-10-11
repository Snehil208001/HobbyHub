package com.example.hobbyhub.mainui.filterscreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hobbyhub.mainui.filterscreen.viewmodel.FilterViewModel
import com.example.hobbyhub.ui.theme.EventHubPrimary
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    viewModel: FilterViewModel = hiltViewModel(),
    onApplyClick: () -> Unit,
    currentLocation: String // Add parameter to accept location
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Drag Handle
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            "Filter",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Category Filter
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(uiState.categories) { category ->
                CategoryChip(
                    text = category,
                    icon = getIconForCategory(category),
                    isSelected = uiState.selectedCategory == category,
                    onClick = { viewModel.onCategorySelected(category) }
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Time & Date Filter
        FilterSectionTitle("Time & Date")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.timeAndDateOptions.forEach { option ->
                TimeDateChip(
                    text = option,
                    isSelected = uiState.selectedTimeAndDate == option,
                    onClick = { viewModel.onTimeAndDateSelected(option) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { /* TODO: Implement Calendar Picker */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = "Calendar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Choose from calender", modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Location Filter
        FilterSectionTitle("Location")
        OutlinedButton(
            onClick = { /* TODO: Implement Location Picker */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = "Location")
            Spacer(modifier = Modifier.width(8.dp))
            // Use the passed-in location
            Text(currentLocation, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Price Range Filter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilterSectionTitle("Select price range")
            Text(
                "$${uiState.priceRange.start.roundToInt()}-$${uiState.priceRange.endInclusive.roundToInt()}",
                fontWeight = FontWeight.Bold,
                color = EventHubPrimary
            )
        }
        RangeSlider(
            value = uiState.priceRange,
            onValueChange = { viewModel.onPriceRangeChanged(it) },
            valueRange = 0f..200f,
            steps = 19
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = viewModel::onResetClicked,
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text("RESET")
            }
            Button(
                onClick = onApplyClick,
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text("APPLY")
            }
        }
    }
}

@Composable
fun CategoryChip(text: String, icon: ImageVector, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) EventHubPrimary else Color.White
    val contentColor = if (isSelected) Color.White else Color.Black
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = text, tint = contentColor, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, fontSize = 14.sp)
    }
}

@Composable
fun TimeDateChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val buttonColors = if (isSelected) {
        ButtonDefaults.buttonColors(containerColor = EventHubPrimary)
    } else {
        ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
    }

    if (isSelected) {
        Button(onClick = onClick, colors = buttonColors) { Text(text) }
    } else {
        OutlinedButton(onClick = onClick) { Text(text) }
    }
}

@Composable
fun FilterSectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

fun getIconForCategory(category: String): ImageVector {
    return when (category) {
        "Sports" -> Icons.Default.SportsBasketball
        "Music" -> Icons.Default.MusicNote
        "Art" -> Icons.Default.Brush
        "Food" -> Icons.Default.Fastfood
        else -> Icons.Default.Help
    }
}