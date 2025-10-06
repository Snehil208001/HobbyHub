package com.example.hobbyhub.mainui.signup.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.hobbyhub.core.navigations.Screen
import com.example.hobbyhub.mainui.profilescreen.viewmodel.ProfileViewModel
import com.example.hobbyhub.mainui.signup.viewmodel.SignupViewModel
import com.example.hobbyhub.ui.theme.EventHubDarkText
import com.example.hobbyhub.ui.theme.EventHubLightGray
import com.example.hobbyhub.ui.theme.EventHubPrimary
import com.example.hobbyhub.ui.theme.TagBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignupViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigateToHome by viewModel.navigateToHome.collectAsState()
    val focusManager = LocalFocusManager.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(navigateToHome) {
        if (navigateToHome) {
            profileViewModel.onNameChange(uiState.fullName)
            profileViewModel.onProfileImageChange(selectedImageUri)
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            viewModel.onNavigatedToHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // --- Back Arrow and Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = EventHubDarkText
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Sign up",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
            fontWeight = FontWeight.SemiBold,
            color = EventHubDarkText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
        ProfilePictureUploader(
            imageUri = selectedImageUri,
            onImageSelected = { uri -> selectedImageUri = uri }
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Full Name Input
        CustomTextField(
            value = uiState.fullName,
            onValueChange = viewModel::onFullNameChange,
            placeholderText = "Full name",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email Input
        CustomTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            placeholderText = "abc@email.com",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        Spacer(modifier = Modifier.height(16.dp))

        // City Input
        CustomTextField(
            value = uiState.city,
            onValueChange = viewModel::onCityChange,
            placeholderText = "City (e.g., New York)",
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        CustomTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            placeholderText = "Your password",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
            visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (uiState.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = viewModel::togglePasswordVisibility) {
                    Icon(imageVector = image, contentDescription = "Toggle password visibility", tint = Color.Gray)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Input
        CustomTextField(
            value = uiState.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            placeholderText = "Confirm password",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
            visualTransformation = if (uiState.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (uiState.confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = viewModel::toggleConfirmPasswordVisibility) {
                    Icon(imageVector = image, contentDescription = "Toggle confirm password visibility", tint = Color.Gray)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Select your interests:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = EventHubDarkText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )
        HobbyTagSelector(
            availableTags = viewModel.availableHobbyTags,
            selectedTags = uiState.selectedHobbyTags,
            onTagSelected = viewModel::toggleHobbyTag
        )
        Spacer(modifier = Modifier.height(40.dp))

        // --- SIGN UP Button ---
        Button(
            onClick = viewModel::onSignupClick,
            enabled = uiState.isSignUpEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = EventHubPrimary)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "SIGN UP",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Icon(Icons.Default.ArrowForward, contentDescription = "Sign Up")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        SignInLink(navController)
        Spacer(modifier = Modifier.height(24.dp))
    }
}
// Keep the rest of the file (ProfilePictureUploader, HobbyTagSelector, etc.) as is

// ----------------------------------------------------------------------
// --- MODIFIED Component: Profile Photo Uploader with Coil and Picker ---
// ----------------------------------------------------------------------
@Composable
fun ProfilePictureUploader(
    imageUri: Uri?,
    onImageSelected: (Uri) -> Unit
) {
    // Launcher for picking an image from the gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                onImageSelected(uri)
            }
        }
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(EventHubLightGray)
                .border(2.dp, EventHubPrimary, CircleShape)
                .clickable { imagePickerLauncher.launch("image/*") }, // Launch the picker
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                // Use Coil's AsyncImage to load the image from the URI
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop // Ensures image fits the circular area
                )
            } else {
                // Default icon when no image is selected
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Upload Photo",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Upload Photo", color = EventHubPrimary, fontWeight = FontWeight.Medium)
    }
}


// --- Component: Hobby Tag Selector ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HobbyTagSelector(
    availableTags: List<String>,
    selectedTags: Set<String>,
    onTagSelected: (String) -> Unit
) {
    // Grouping into columns of Rows to simulate flow layout without external dependencies
    Column(modifier = Modifier.fillMaxWidth()) {
        availableTags.chunked(4).forEach { rowTags ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                rowTags.forEach { tag ->
                    val isSelected = selectedTags.contains(tag)
                    AssistChip(
                        onClick = { onTagSelected(tag) },
                        label = { Text(tag) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (isSelected) EventHubPrimary else TagBackground,
                            labelColor = if (isSelected) Color.White else EventHubDarkText
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) Color.Transparent else EventHubLightGray
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


// --- Reused Helper Composable for Text Fields (Pill-shaped, no outline) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    leadingIcon: @Composable () -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText, color = Color.Gray) },
        leadingIcon = leadingIcon,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = EventHubPrimary,
            focusedTextColor = EventHubDarkText,
            unfocusedTextColor = EventHubDarkText,
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true
    )
}

// --- Helper Composable for Sign In Link ---
@Composable
private fun SignInLink(navController: NavController) {
    val annotatedString = buildAnnotatedString {
        append("Already have an account? ")
        pushStringAnnotation(tag = "SignIn", annotation = "SignIn")
        withStyle(style = SpanStyle(color = EventHubPrimary, fontWeight = FontWeight.Bold)) {
            append("Sign in")
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "SignIn", start = offset, end = offset)
                .firstOrNull()?.let {
                    navController.navigate(Screen.LoginScreen.route)
                }
        },
        style = LocalTextStyle.current.copy(color = EventHubDarkText)
    )
}