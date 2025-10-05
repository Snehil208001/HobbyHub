package com.example.hobbyhub.core.utils.navigationbar


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hobbyhub.R
import com.example.hobbyhub.ui.theme.EventHubPrimary

@Composable
fun HomeTopAppBar(location: String) { // UPDATED: Added location parameter
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(EventHubPrimary)
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: Handle menu click */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Current Location", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Change Location",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(location, fontWeight = FontWeight.Bold, color = Color.White) // UPDATED
            }
            IconButton(onClick = { /* TODO: Handle notifications click */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HomeSearchBar()
    }
}
// Keep HomeSearchBar composable as is

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeSearchBar() {
    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon", tint = Color.Gray) },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Divider(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp), color = Color.LightGray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.logo), // Replace with your filter icon
                    contentDescription = "Filter Icon",
                    tint = EventHubPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Filters", color = EventHubPrimary)
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}