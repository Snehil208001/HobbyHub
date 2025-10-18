package com.example.hobbyhub.mainui.profilescreen.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.hobbyhub.mainui.profilescreen.viewmodel.ProfileViewModel
import com.google.accompanist.flowlayout.FlowRow // Make sure you have this dependency: implementation "com.google.accompanist:accompanist-flowlayout:0.xx.x"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    // Use shared ViewModel scoped to the NavGraph (if Profile & EditProfile are in the same graph)
    // Or pass the ViewModel instance if needed differently. HiltViewModel usually works well here.
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    var hobbyInput by remember { mutableStateOf("") }

    // Effect to navigate back on successful save
    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            navController.popBackStack()
            // ProfileScreen's LaunchedEffect will trigger reload
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bio Editor
            OutlinedTextField(
                value = uiState.editedBio,
                onValueChange = viewModel::onBioChange,
                label = { Text("About Me (Bio)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp), // Use heightIn for flexibility
                //maxLines = 5 // Let it grow naturally with heightIn
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Hobby Input
            Text(
                "Hobbies & Interests",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = hobbyInput,
                    onValueChange = { hobbyInput = it },
                    label = { Text("Add a hobby") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        viewModel.onHobbyAdded(hobbyInput)
                        hobbyInput = "" // Clear input
                        focusManager.clearFocus() // Hide keyboard
                    })
                )
                IconButton(
                    onClick = {
                        viewModel.onHobbyAdded(hobbyInput)
                        hobbyInput = "" // Clear input
                        focusManager.clearFocus() // Hide keyboard
                    },
                    enabled = hobbyInput.isNotBlank()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Hobby")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display Added Hobbies with remove button
            if (uiState.editedHobbies.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 4.dp
                ) {
                    uiState.editedHobbies.forEach { hobby ->
                        InputChip(
                            selected = false, // Not selectable, just removable
                            onClick = { /* Could add edit functionality later */ },
                            label = { Text(hobby) },
                            trailingIcon = {
                                IconButton(
                                    onClick = { viewModel.onHobbyRemoved(hobby) },
                                    modifier = Modifier.size(18.dp) // Smaller icon button
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove $hobby",
                                        modifier = Modifier.size(14.dp) // Smaller icon
                                    )
                                }
                            }
                        )
                    }
                }
            } else {
                Text("No hobbies added yet.", style = MaterialTheme.typography.bodyMedium)
            }


            Spacer(modifier = Modifier.height(32.dp))

            // Save Button with Loading Indicator
            Button(
                onClick = viewModel::saveProfileChanges,
                enabled = !uiState.updateInProgress, // Disable while saving
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.updateInProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary // Color that contrasts with button
                    )
                } else {
                    Text("Save Changes")
                }
            }

            // Display Save Error
            if (uiState.updateError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Error: ${uiState.updateError}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}