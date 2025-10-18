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
// import com.example.hobbyhub.mainui.profilescreen.viewmodel.ProfileViewModel // Not needed here
import com.example.hobbyhub.mainui.signup.viewmodel.SignupViewModel
import com.example.hobbyhub.ui.theme.EventHubDarkText
import com.example.hobbyhub.ui.theme.EventHubLightGray
import com.example.hobbyhub.ui.theme.EventHubPrimary
import com.example.hobbyhub.ui.theme.TagBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignupViewModel = hiltViewModel()
    // Removed profileViewModel, it's not needed here
) {
    val uiState by viewModel.uiState.collectAsState()
    val signUpState by viewModel.signUpState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    // --- REMOVED this line: var selectedImageUri by remember { mutableStateOf<Uri?>(null) } ---

    // Handle Signup Success/Error
    LaunchedEffect(signUpState) {
        if (signUpState.isSuccess) {
            Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT).show()

            // --- REMOVED these lines, ProfileViewModel will load its own data ---
            // profileViewModel.onNameChange(uiState.fullName)
            // profileViewModel.onProfileImageChange(selectedImageUri)

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
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Back Button
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

            // Title
            Text(
                "Sign up",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                fontWeight = FontWeight.SemiBold,
                color = EventHubDarkText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Profile Picture
            ProfilePictureUploader(
                imageUri = uiState.imageUri, // --- Use imageUri from ViewModel state ---
                onImageSelected = { uri -> viewModel.onImageUriChange(uri) } // --- Call ViewModel function ---
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Full Name Field (Unchanged)
            CustomTextField(
                value = uiState.fullName,
                onValueChange = viewModel::onFullNameChange,
                // ... rest of props
                placeholderText = "Full name",
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = uiState.fullNameError != null
            )
            uiState.fullNameError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Email Field (Unchanged)
            CustomTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                // ... rest of props
                placeholderText = "abc@email.com",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = uiState.emailError != null
            )
            uiState.emailError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Password Field (Unchanged)
            CustomTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                // ... rest of props
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
                isError = uiState.passwordError != null
            )
            uiState.passwordError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field (Unchanged)
            CustomTextField(
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                // ... rest of props
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
                    viewModel.onSignupClick()
                    focusManager.clearFocus()
                }),
                isError = uiState.confirmPasswordError != null
            )
            uiState.confirmPasswordError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Button (Unchanged)
            Button(
                onClick = viewModel::onSignupClick,
                enabled = uiState.isSignUpEnabled && !signUpState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EventHubPrimary,
                    disabledContainerColor = EventHubPrimary.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    "SIGN UP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Sign In Link (Unchanged)
            SignInLink(navController)
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Loading Indicator Overlay (Unchanged)
        if (signUpState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

// --- ProfilePictureUploader Composable (Unchanged) ---
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
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
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


// --- CustomTextField Composable (Unchanged) ---
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
    isError: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText, color = Color.Gray) },
        leadingIcon = leadingIcon,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        isError = isError,
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
            errorContainerColor = Color.White,
            errorIndicatorColor = Color.Transparent,
            errorCursorColor = MaterialTheme.colorScheme.error,
            errorLeadingIconColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
            errorTrailingIconColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
            errorTextColor = EventHubDarkText
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true
    )
}

// --- SignInLink Composable (Unchanged) ---
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
                    navController.navigate(Screen.LoginScreen.route) {
                        launchSingleTop = true
                    }
                }
        },
        style = LocalTextStyle.current.copy(color = EventHubDarkText, textAlign = TextAlign.Center),
        modifier = Modifier.fillMaxWidth()
    )
}

// --- HobbyTagSelector Composable (Unchanged) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HobbyTagSelector(
    availableTags: List<String>,
    selectedTags: Set<String>,
    onTagSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Select your interests",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        com.google.accompanist.flowlayout.FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp
        ) {
            availableTags.forEach { tag ->
                val isSelected = selectedTags.contains(tag)
                FilterChip(
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