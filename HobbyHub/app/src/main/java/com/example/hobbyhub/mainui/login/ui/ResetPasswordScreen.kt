package com.example.hobbyhub.mainui.login.ui

import android.widget.Toast // Import Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext // Import LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hobbyhub.ui.theme.EventHubDarkText
import com.example.hobbyhub.ui.theme.EventHubPrimary
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hobbyhub.mainui.login.viewmodel.ResetPasswordViewModel
import com.example.hobbyhub.ui.theme.EventHubLightGray // Added for error field border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    navController: NavController,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current // Get context for Toast

    // Show success/error messages using LaunchedEffect and Toast
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            // Optionally navigate back after success
            // navController.popBackStack()
        }
    }
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Back Arrow ---
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

        // --- Title and Description ---
        Text(
            "Reset Password",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
            fontWeight = FontWeight.SemiBold,
            color = EventHubDarkText,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Text(
            "Please enter your email address to request a password reset",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp)
        )

        // --- Email Input Field ---
        CustomTextField( // Use the updated CustomTextField that accepts isError
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            placeholderText = "abc@email.com",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                viewModel.onSendClick() // Send on keyboard done
                focusManager.clearFocus()
            }),
            isError = uiState.emailError != null // Pass the error state
        )
        // Display validation error message below the text field
        uiState.emailError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp).fillMaxWidth() // Align start, add padding
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- SEND Button ---
        Button(
            onClick = viewModel::onSendClick,
            // Use the validated state from ViewModel
            enabled = uiState.isSendEnabled,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = EventHubPrimary)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "SEND",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Icon(Icons.Default.ArrowForward, contentDescription = "Send Reset Link")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// --- Make sure CustomTextField in ResetPasswordScreen.kt accepts isError ---
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
    isError: Boolean = false // ADD THIS (if not already added)
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText, color = Color.Gray) },
        leadingIcon = leadingIcon,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        isError = isError, // PASS IT HERE
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
            // Optional: Add error state colors
            errorContainerColor = Color.White,
            errorIndicatorColor = Color.Transparent, // No underline on error either
            errorCursorColor = MaterialTheme.colorScheme.error,
            errorLeadingIconColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray,
            errorTrailingIconColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true
    )
}