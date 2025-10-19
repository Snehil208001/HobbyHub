// In snehil208001/hobbyhub/HobbyHub-102bbb5bfeae283b4c3e52ca5e13f3198e956095/HobbyHub/app/src/main/java/com/example/hobbyhub/mainui/profilescreen/ui/EditProfileScreen.kt

package com.example.hobbyhub.mainui.profilescreen.ui

import android.Manifest // <-- ADDED
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MyLocation // <-- ADDED
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.hobbyhub.R
import com.example.hobbyhub.core.utils.navigationbar.LocationViewModel // <-- ADDED
import com.example.hobbyhub.mainui.profilescreen.viewmodel.ProfileViewModel
import com.example.hobbyhub.ui.theme.EventHubLightGray
import com.example.hobbyhub.ui.theme.EventHubPrimary
import com.google.accompanist.flowlayout.FlowRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel() // <-- ADDED LocationViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // --- ADDED: Location Permission Logic ---
    val locationState by locationViewModel.location.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                locationViewModel.fetchLocation(context)
            }
            // You might want to show a message if permission is denied
        }
    )

    // This effect listens for the location to be updated by the locationViewModel,
    // and then sets it in the profileViewModel's state, updating the text field.
    LaunchedEffect(locationState) {
        if (locationState != "Loading..." && locationState != "Unknown") {
            viewModel.onLocationChange(locationState)
        }
    }
    // --- END ADD ---


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.onProfileImageChanged(it) }
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetUpdateStatus()
        }
    }

    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            navController.popBackStack()
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

            // Profile Picture Uploader (No changes)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(EventHubLightGray)
                    .border(2.dp, EventHubPrimary, CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                val imageToShow = uiState.selectedImageUri ?: uiState.user?.profileImageUrl
                val imageModel = if (uiState.selectedImageUri != null) {
                    ImageRequest.Builder(context)
                        .data(imageToShow)
                        .crossfade(true)
                        .build()
                } else {
                    ImageRequest.Builder(context)
                        .data(imageToShow?.takeIf { it.toString().isNotEmpty() } ?: R.drawable.logo)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .crossfade(true)
                        .build()
                }

                AsyncImage(
                    model = imageModel ?: R.drawable.logo,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.logo),
                    error = painterResource(id = R.drawable.logo)
                )
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Change Photo",
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-8).dp, y = (-8).dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .padding(4.dp),
                    tint = Color.White
                )
            }
            Text(
                "Tap picture to change",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Bio Editor (No changes)
            OutlinedTextField(
                value = uiState.editedBio,
                onValueChange = viewModel::onBioChange,
                label = { Text("About Me (Bio)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                supportingText = {
                    Text(
                        text = "${uiState.editedBio.length} / 200",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- MODIFIED: Location Field + Button ---
            OutlinedTextField(
                value = uiState.editedLocation,
                onValueChange = viewModel::onLocationChange,
                label = { Text("Location") },
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = "Location")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            TextButton(
                onClick = {
                    // This will check for permission, and if granted, call
                    // locationViewModel.fetchLocation(context)
                    // The LaunchedEffect will then update the text field.
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.MyLocation,
                    contentDescription = "Get Current Location",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Use Current Location")
            }
            // --- END MODIFICATION ---

            Spacer(modifier = Modifier.height(8.dp)) // Reduced space

            // Website Field (No changes)
            OutlinedTextField(
                value = uiState.editedWebsite,
                onValueChange = viewModel::onWebsiteChange,
                label = { Text("Website") },
                leadingIcon = {
                    Icon(Icons.Default.Language, contentDescription = "Website")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Privacy Toggle (No changes)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Privacy",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Private Profile",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = uiState.editedIsPrivate,
                    onCheckedChange = viewModel::onPrivacyChange
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Hobby Input & Display (No changes)
            Text(
                "Hobbies & Interests",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "Select all that apply:",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 4.dp
            ) {
                uiState.allHobbiesList.forEach { hobby ->
                    FilterChip(
                        selected = uiState.editedHobbies.contains(hobby),
                        onClick = { viewModel.onHobbyToggle(hobby) },
                        label = { Text(hobby) },
                        leadingIcon = if (uiState.editedHobbies.contains(hobby)) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Selected",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button (No changes)
            Button(
                onClick = viewModel::saveProfileChanges,
                enabled = !uiState.updateInProgress,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.updateInProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save Changes")
                }
            }

            // Display Save Error (No changes)
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