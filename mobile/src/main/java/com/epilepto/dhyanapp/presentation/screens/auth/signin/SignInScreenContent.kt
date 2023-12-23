package com.epilepto.dhyanapp.presentation.screens.auth.signin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.presentation.components.GoogleSignInButton
import com.epilepto.dhyanapp.theme.NunitoSans
import com.epilepto.dhyanapp.theme.onPrimary
import com.epilepto.dhyanapp.theme.primaryColor
import com.epilepto.dhyanapp.theme.textFieldContainer

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreenContent(
    modifier: Modifier = Modifier,
    loadingState: Boolean,
    navigateToRegister: () -> Unit,
    onLoginWithGoogle: () -> Unit,
    onLoginWithFacebook: () -> Unit,
    onLoginWithEmail: (String, String) -> Unit,
    onForgotPassword: (String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val (email, setEmail) = rememberSaveable { mutableStateOf("") }
        val (password, setPassword) = rememberSaveable { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current

        Text(
            text = "Welcome Back!",
            style = TextStyle(
                fontFamily = NunitoSans,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            ),
            modifier = Modifier.padding(12.dp)
        )

        GoogleSignInButton(
            text = "Continue With Google",
            iconPainter = painterResource(id = R.drawable.google_logo),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            onClick = onLoginWithGoogle,
            isLoading = loadingState
        )

/*        FacebookSignInButton(
            text = "Continue With Facebook",
            iconPainter = painterResource(id = R.drawable.facebook_logo),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            onClick = onLoginWithFacebook
        )*/

        Text(
            text = "OR LOGIN WITH EMAIL",
            style = TextStyle(
                fontFamily = NunitoSans,
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                color = Color.Gray
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(12.dp)
        )

        TextField(
            value = email,
            onValueChange = setEmail,
            placeholder = { Text(text = "Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() }),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = textFieldContainer,
                unfocusedContainerColor = textFieldContainer,
                focusedLabelColor = Color.Transparent
            ),
            shape = Shapes().medium,

            )
        TextField(
            value = password,
            onValueChange = setPassword,
            placeholder = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() }),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = textFieldContainer,
                unfocusedContainerColor = textFieldContainer,
                focusedLabelColor = Color.Transparent

            ),
            trailingIcon = {
                val icon: ImageVector =
                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                Icon(
                    imageVector = icon,
                    contentDescription = "Toggle password visibility",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { passwordVisible = !passwordVisible })
                )
            },
            shape = Shapes().medium,

            )

        Button(
            onClick = {
                onLoginWithEmail(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text(
                text = "LOG IN",
                modifier = Modifier.padding(12.dp),
                color = onPrimary,
            )
        }
        Text(
            text = "Forgot Password?",
            style = TextStyle(
                fontFamily = NunitoSans,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            ),
            modifier = Modifier
                .padding(12.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onForgotPassword(email) }
                )
        )

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account yet? ",
                style = TextStyle(
                    color = Color(0xffA1A4B2),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontFamily = NunitoSans
                )
            )
            Text(
                text = "SIGN UP ",
                style = TextStyle(
                    color = primaryColor,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontFamily = NunitoSans,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = navigateToRegister
                )

            )
        }
    }

    AnimatedVisibility(
        visible = loadingState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Dialog(
            onDismissRequest = { /* Do nothing */ },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = primaryColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}