package com.example.hobbyhub.mainui.login.ui

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    navController: NavController,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    // Collect UI state from ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Back Arrow (Modified to use padding from WindowInsets) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), // Kept this padding for visual separation from the top edge
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) { // Ensure back navigation works
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

        // --- Email Input Field (Reusing CustomTextField structure) ---
        CustomTextField(
            value = uiState.email, // UPDATED: uses ViewModel state
            onValueChange = viewModel::onEmailChange, // UPDATED: uses ViewModel handler
            placeholderText = "abc@email.com",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- SEND Button ---
        Button(
            onClick = viewModel::onSendClick, // UPDATED: uses ViewModel handler
            enabled = uiState.isSendEnabled, // ADDED: Button is enabled/disabled by ViewModel state
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

// Reused Helper Composable for Text Fields (Pill-shaped, no outline)
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
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true
    )
}