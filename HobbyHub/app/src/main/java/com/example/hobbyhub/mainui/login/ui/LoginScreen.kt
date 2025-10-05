package com.example.hobbyhub.mainui.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.graphics.Color
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
import com.example.hobbyhub.R
import com.example.hobbyhub.core.navigations.Screen

// Define colors to closely match the image
val EventHubPrimary = Color(0xFF5E54F3)
val EventHubLightGray = Color(0xFFE0E0E0)
val EventHubDarkText = Color(0xFF1E1E1E)

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // --- Logo and App Name ---
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

        // --- "Sign in" Header ---
        Text(
            "Sign in",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
            fontWeight = FontWeight.SemiBold,
            color = EventHubDarkText,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // --- Email Input Field ---
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            placeholderText = "abc@email.com",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Password Input Field ---
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

        // --- Remember Me & Forgot Password ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = EventHubPrimary,
                        uncheckedThumbColor = Color.LightGray,
                        uncheckedTrackColor = EventHubLightGray
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Remember Me", color = EventHubDarkText)
            }
            TextButton(
                onClick = { /* TODO */ },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Forgot Password?", color = EventHubPrimary, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        // --- SIGN IN Button ---
        Button(
            onClick = { /* TODO: Handle login logic */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = EventHubPrimary)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "SIGN IN",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Icon(Icons.Default.ArrowForward, contentDescription = "Sign In")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        // --- OR Divider ---
        DividerWithText()

        Spacer(modifier = Modifier.height(32.dp))

        // --- Social Login Buttons ---
        SocialLoginButton(
            icon = R.drawable.ic_google,
            text = "Login with Google",
            onClick = { /* TODO */ }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SocialLoginButton(
            icon = R.drawable.ic_facebook,
            text = "Login with Facebook",
            onClick = { /* TODO */ }
        )
        Spacer(modifier = Modifier.weight(1f))

        // --- Sign Up Link ---
        SignUpLink(navController)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// --- Helper Composable for Text Fields (To match the image's "filled" appearance) ---
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


// --- MODIFIED Social Login Button ---
@Composable
fun SocialLoginButton(
    icon: Int,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = EventHubDarkText
        ),
        border = androidx.compose.material3.ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(EventHubLightGray)
        ),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            // ⭐️ This change centers the icon and text within the button
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
                    navController.navigate(Screen.SignupScreen.route)
                }
        },
        style = LocalTextStyle.current.copy(color = EventHubDarkText)
    )
}

@Composable
fun DividerWithText() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Divider(modifier = Modifier.weight(1f), color = EventHubLightGray, thickness = 1.dp)
        Text("OR", modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray, fontWeight = FontWeight.SemiBold)
        Divider(modifier = Modifier.weight(1f), color = EventHubLightGray, thickness = 1.dp)
    }
}