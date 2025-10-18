package com.example.hobbyhub.mainui.login.ui

import android.app.Activity // Keep Activity import if needed by other parts not shown
import android.widget.Toast
import androidx.compose.foundation.BorderStroke // Keep if SocialLoginButton is used elsewhere
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import com.example.hobbyhub.R
import com.example.hobbyhub.core.navigations.Screen
import com.example.hobbyhub.mainui.login.viewmodel.LoginViewModel
import com.example.hobbyhub.ui.theme.EventHubDarkText
import com.example.hobbyhub.ui.theme.EventHubLightGray
import com.example.hobbyhub.ui.theme.EventHubPrimary
import kotlinx.coroutines.launch // Keep

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val signInState by viewModel.signInState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    // Handle Sign In Success/Error
    LaunchedEffect(signInState) {
        if (signInState.isSuccess) {
            Toast.makeText(context, "Sign in successful!", Toast.LENGTH_SHORT).show()
            // Navigate to Home screen, clearing the back stack
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
            viewModel.resetState() // Reset login state after navigation
        }
        signInState.error?.let {
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
            viewModel.resetState() // Reset login state after showing error
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical)) // Respect system bars
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp)) // Space from top

            // Logo and App Name
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "HobbyHub Logo",
                modifier = Modifier.size(50.dp)
            )
            Text(
                "HobbyHub",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                fontWeight = FontWeight.SemiBold,
                color = EventHubDarkText
            )
            Spacer(modifier = Modifier.height(64.dp))

            // Title
            Text(
                "Sign in",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                fontWeight = FontWeight.SemiBold,
                color = EventHubDarkText,
                modifier = Modifier
                    .fillMaxWidth() // Align text start
                    .padding(bottom = 16.dp)
            )

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
            // Optional: Display Email Error
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
                placeholderText = "Your password",
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (uiState.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = viewModel::togglePasswordVisibility) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility", tint = Color.Gray)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    viewModel.onLoginClick() // Attempt login on keyboard done
                    focusManager.clearFocus()
                }),
                isError = uiState.passwordError != null // Pass error state
            )
            // Optional: Display Password Error
            uiState.passwordError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Remember Me and Forgot Password Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = uiState.rememberMe,
                        onCheckedChange = viewModel::onRememberMeToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = EventHubPrimary,
                            uncheckedThumbColor = Color.LightGray, // More subtle unchecked colors
                            uncheckedTrackColor = EventHubLightGray
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Remember Me", color = EventHubDarkText)
                }
                TextButton(
                    onClick = { navController.navigate(Screen.ResetPasswordScreen.route) },
                    contentPadding = PaddingValues(0.dp) // Remove default padding
                ) {
                    Text("Forgot Password?", color = EventHubPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            // Sign In Button
            Button(
                onClick = viewModel::onLoginClick,
                // Use the validated state from ViewModel, also check loading state
                enabled = uiState.isLoginEnabled && !signInState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50), // Pill shape
                colors = ButtonDefaults.buttonColors(
                    containerColor = EventHubPrimary,
                    // Optional: Dim button when disabled
                    disabledContainerColor = EventHubPrimary.copy(alpha = 0.5f)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "SIGN IN",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White // Ensure text is white
                    )
                    Icon(Icons.Default.ArrowForward, contentDescription = "Sign In", tint = Color.White)
                }
            }

            // Removed Social Login Buttons and Divider

            Spacer(modifier = Modifier.weight(1f)) // Pushes SignUpLink to bottom

            // Sign Up Link
            SignUpLink(navController)
            Spacer(modifier = Modifier.height(24.dp)) // Padding at the very bottom
        }

        // Loading Indicator Overlay
        if (signInState.isLoading) {
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


// --- CustomTextField Composable (Updated with isError) ---
// (Ensure this definition is present and matches the one from SignupScreen.kt)
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
    isError: Boolean = false // Make sure this parameter exists
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText, color = Color.Gray) },
        leadingIcon = leadingIcon,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        isError = isError, // Pass error state
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

// --- SocialLoginButton Composable (Kept in case needed later, but not called) ---
@Composable
fun SocialLoginButton(
    icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = EventHubDarkText
        ),
        border = BorderStroke(1.dp, EventHubLightGray),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

// --- SignUpLink Composable (No changes needed) ---
@Composable
private fun SignUpLink(navController: NavController) {
    val annotatedString = buildAnnotatedString {
        append("Don't have an account? ")
        pushStringAnnotation(tag = "SignUp", annotation = "SignUp")
        withStyle(style = SpanStyle(color = EventHubPrimary, fontWeight = FontWeight.Bold)) {
            append("Sign up")
        }
        pop()
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "SignUp", start = offset, end = offset)
                .firstOrNull()?.let {
                    navController.navigate(Screen.SignupScreen.route) {
                        launchSingleTop = true
                    }
                }
        },
        style = LocalTextStyle.current.copy(color = EventHubDarkText, textAlign = TextAlign.Center),
        modifier = Modifier.fillMaxWidth()
    )
}