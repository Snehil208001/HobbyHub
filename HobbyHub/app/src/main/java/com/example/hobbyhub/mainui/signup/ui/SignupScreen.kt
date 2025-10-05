package com.example.hobbyhub.mainui.signup.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage // <-- COIL IMPORT
import com.example.hobbyhub.R
import com.example.hobbyhub.core.navigations.Screen

// Define colors to closely match the image (reused EventHub constants)
val EventHubPrimary = Color(0xFF5E54F3)
val EventHubLightGray = Color(0xFFE0E0E0)
val EventHubDarkText = Color(0xFF1E1E1E)
val TagBackground = Color(0xFFF3F3FF) // Light background for unselected tags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    // --- PHOTO STATE ---
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Basic Auth & Profile Fields
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Profile Setup State
    val availableHobbyTags = remember {
        listOf("Art", "Cycling", "Cooking", "Photography", "Gaming", "Hiking", "Reading", "Music")
    }
    var selectedHobbyTags by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
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
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        // ----------------------------------------------------
        // --- 1. Upload Photo (Updated to pass state) ---
        // ----------------------------------------------------
        ProfilePictureUploader(
            imageUri = selectedImageUri,
            onImageSelected = { uri ->
                selectedImageUri = uri
                // NOTE: This is where you would launch the CROP activity
                // in a real app, passing 'uri' to it. The cropped result URI
                // would then update 'selectedImageUri'.
            }
        )
        Spacer(modifier = Modifier.height(32.dp))

        // ----------------------------------------------------
        // --- 2. Profile and Auth Details ---
        // ----------------------------------------------------

        // Full Name Input
        CustomTextField(
            value = fullName,
            onValueChange = { fullName = it },
            placeholderText = "Full name",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray) }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email Input
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            placeholderText = "abc@email.com",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // City Input (New field from requirements)
        CustomTextField(
            value = city,
            onValueChange = { city = it },
            placeholderText = "City (e.g., New York)",
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray) }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            placeholderText = "Your password",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle password visibility", tint = Color.Gray)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Input
        CustomTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholderText = "Confirm password",
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle confirm password visibility", tint = Color.Gray)
                }
            }
        )
        Spacer(modifier = Modifier.height(32.dp))

        // ----------------------------------------------------
        // --- 3. Hobby Tags Selection ---
        // ----------------------------------------------------
        Text(
            "Select your interests:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = EventHubDarkText,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )
        HobbyTagSelector(
            availableTags = availableHobbyTags,
            selectedTags = selectedHobbyTags,
            onTagSelected = { tag ->
                selectedHobbyTags = if (selectedHobbyTags.contains(tag)) {
                    selectedHobbyTags - tag
                } else {
                    selectedHobbyTags + tag
                }
            }
        )
        Spacer(modifier = Modifier.height(40.dp))

        // --- SIGN UP Button ---
        Button(
            onClick = { /* TODO: Handle full signup and profile creation */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
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

        // --- Sign In Link ---
        SignInLink(navController)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

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
@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    leadingIcon: @Composable () -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText, color = Color.Gray) },
        leadingIcon = leadingIcon,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        modifier = Modifier.fillMaxWidth().height(56.dp),
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
        )
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