package com.example.hobbyhub.mainui.signup.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
    profileViewModel: ProfileViewModel = hiltViewModel() // Assuming this is needed for profile updates after signup
) {
    val uiState by viewModel.uiState.collectAsState()
    val signUpState by viewModel.signUpState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Handle Signup Success/Error
    LaunchedEffect(signUpState) {
        if (signUpState.isSuccess) {
            Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT).show()
            // Update profile ViewModel *after* successful signup
            profileViewModel.onNameChange(uiState.fullName)
            profileViewModel.onProfileImageChange(selectedImageUri)
            // Navigate to Home screen, clearing the back stack
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
            viewModel.resetState() // Reset signup state after navigation
        }
        signUpState.error?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
            viewModel.resetState() // Reset signup state after showing error
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical)) // Respect system bars
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), // Padding from status bar
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

            // Title
            Text(
                "Sign up",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                fontWeight = FontWeight.SemiBold,
                color = EventHubDarkText,
                modifier = Modifier
                    .fillMaxWidth() // Align text start
                    .padding(bottom = 24.dp)
            )

            // Profile Picture
            ProfilePictureUploader(
                imageUri = selectedImageUri,
                onImageSelected = { uri -> selectedImageUri = uri }
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Full Name Field
            CustomTextField(
                value = uiState.fullName,
                onValueChange = viewModel::onFullNameChange,
                placeholderText = "Full name",
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = uiState.fullNameError != null // Pass error state
            )
            // Display Full Name Error (Optional)
            uiState.fullNameError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            CustomTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                placeholderText = "abc@email.com",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = uiState.emailError != null // Pass error state
            )
            // Display Email Error (Optional)
            uiState.emailError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            CustomTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                placeholderText = "Your password (min. 6 characters)",
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (uiState.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = viewModel::togglePasswordVisibility) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility", tint = Color.Gray)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = uiState.passwordError != null // Pass error state
            )
            // Display Password Error (Optional)
            uiState.passwordError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
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
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.onSignupClick() // Attempt signup on keyboard done
                    focusManager.clearFocus()
                }),
                isError = uiState.confirmPasswordError != null // Pass error state
            )
            // Display Confirm Password Error (Optional)
            uiState.confirmPasswordError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Button
            Button(
                onClick = viewModel::onSignupClick,
                // Use the validated state from ViewModel, also check loading state
                enabled = uiState.isSignUpEnabled && !signUpState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EventHubPrimary,
                    // Optional: Dim button when disabled
                    disabledContainerColor = EventHubPrimary.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    "SIGN UP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White // Ensure text is white
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Sign In Link
            SignInLink(navController)
            Spacer(modifier = Modifier.height(24.dp)) // Bottom padding
        }

        // Loading Indicator Overlay
        if (signUpState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                // Optional: Add a semi-transparent background scrim
                // .background(Color.Black.copy(alpha = 0.3f))
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

// --- ProfilePictureUploader Composable (No changes needed) ---
@Composable
fun ProfilePictureUploader(
    imageUri: Uri?,
    onImageSelected: (Uri) -> Unit
) {
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
                .clickable { imagePickerLauncher.launch("image/*") }, // Allow clicking to pick image
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(), // Fill the circle
                    contentScale = ContentScale.Crop // Crop to fit the circle
                )
            } else {
                // Placeholder Icon
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


// --- CustomTextField Composable (Updated with isError) ---
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
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false // ADDED isError parameter
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText, color = Color.Gray) },
        leadingIcon = leadingIcon,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        isError = isError, // PASS isError to the underlying TextField
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp), // Pill shape
        colors = TextFieldDefaults.colors( // Use new colors API
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White, // Use same background when disabled
            focusedIndicatorColor = Color.Transparent, // No underline
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = EventHubPrimary,
            focusedTextColor = EventHubDarkText,
            unfocusedTextColor = EventHubDarkText,
            // Error state colors
            errorContainerColor = Color.White, // Keep background white on error
            errorIndicatorColor = Color.Transparent, // No underline on error
            errorCursorColor = MaterialTheme.colorScheme.error,
            errorLeadingIconColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
            errorTrailingIconColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
            errorTextColor = EventHubDarkText // Keep text color normal even on error
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true
    )
}

// --- SignInLink Composable (No changes needed) ---
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
                    // Navigate to Login Screen
                    navController.navigate(Screen.LoginScreen.route) {
                        // Optional: Pop Signup off the stack if desired
                        // popUpTo(Screen.SignupScreen.route) { inclusive = true }
                        // Or pop up to the start destination
                        // popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true // Prevent multiple Login screens
                    }
                }
        },
        style = LocalTextStyle.current.copy(color = EventHubDarkText, textAlign = TextAlign.Center),
        modifier = Modifier.fillMaxWidth() // Center align the link text
    )
}

// --- HobbyTagSelector Composable (No changes needed if removed from UI state) ---
// If you decide to add hobby selection back, this composable is ready.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HobbyTagSelector(
    availableTags: List<String>,
    selectedTags: Set<String>,
    onTagSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Select your interests", // Add a label for the tags
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Using FlowRow from Accompanist is often better for tags
        com.google.accompanist.flowlayout.FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp
        ) {
            availableTags.forEach { tag ->
                val isSelected = selectedTags.contains(tag)
                FilterChip( // Use FilterChip for toggle behavior
                    selected = isSelected,
                    onClick = { onTagSelected(tag) },
                    label = { Text(tag) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = TagBackground,
                        labelColor = EventHubDarkText,
                        selectedContainerColor = EventHubPrimary,
                        selectedLabelColor = Color.White
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) Color.Transparent else EventHubLightGray
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}