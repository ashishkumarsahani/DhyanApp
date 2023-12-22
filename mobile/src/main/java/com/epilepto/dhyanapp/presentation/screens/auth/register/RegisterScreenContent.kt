package com.epilepto.dhyanapp.presentation.screens.auth.register


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.epilepto.dhyanapp.presentation.components.LoadingScreen
import com.epilepto.dhyanapp.theme.NunitoSans
import com.epilepto.dhyanapp.theme.onPrimary
import com.epilepto.dhyanapp.theme.primaryColor
import com.epilepto.dhyanapp.theme.textFieldContainer
import com.epilepto.dhyanapp.utils.Constants
import com.epilepto.dhyanapp.utils.SignInUtils
import com.stevdzasan.messagebar.MessageBarState

@Composable
fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    loadingState: Boolean,
    messageBarState: MessageBarState,
    onRegister: (String, String, String) -> Unit,
    navigateToSignUp: () -> Unit,
    showPrivacyPolicy:()->Unit
) {
    var showPolicy by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val (name, setName) = rememberSaveable { mutableStateOf("") }
        val (email, setEmail) = rememberSaveable { mutableStateOf("") }
        val (password, setPassword) = rememberSaveable { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var isChecked by rememberSaveable { mutableStateOf(false) }
        val focusManager = LocalFocusManager.current
        Text(
            text = "Register, To Get Started!",
            style = TextStyle(
                //fontFamily = NunitoSans,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            ),
            modifier = Modifier.padding(12.dp)
        )

        Column {
            TextField(
                value = name,
                onValueChange = setName,
                placeholder = { Text(text = "Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
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
                value = email,
                onValueChange = setEmail,
                placeholder = { Text(text = "Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
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
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "I have read the ",
                    style = TextStyle(
                        color = Color(0xffA1A4B2),
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontFamily = NunitoSans
                    )
                )
                Text(
                    text = "Privacy Policy",
                    style = TextStyle(
                        color = primaryColor,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontFamily = NunitoSans,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = showPrivacyPolicy
/*                            onClick = {
                                //TODO: Implement logic to show privacy policy
                                showPolicy = true
                            }*/
                        )

                )
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                    }
                )

            }
        }

        Button(
            onClick = {
                val errorMessage = SignInUtils.verifySignUpDetails(name, email, password)
                if (errorMessage.isNotEmpty()) {
                    messageBarState.addError(Exception(errorMessage))
                } else if (!isChecked) {
                    messageBarState.addError(Exception("Please agree with the privacy policy to continue"))
                } else {
                    onRegister(name.trim(), email.trim(), password.trim())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text(
                text = "REGISTER",
                modifier = Modifier.padding(12.dp),
                color = onPrimary,
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account ? ",
                style = TextStyle(
                    color = Color(0xffA1A4B2),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontFamily = NunitoSans
                )
            )
            Text(
                text = "SIGN IN ",
                style = TextStyle(
                    color = primaryColor,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontFamily = NunitoSans,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = navigateToSignUp
                )

            )
        }
    }

    AnimatedVisibility(
        visible = showPolicy,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Dialog(
            onDismissRequest = { showPolicy = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Privacy Policy",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    IconButton(onClick = { showPolicy = false }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Close Policy"
                        )
                    }
                }
                Divider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.fillMaxWidth())

                Text(
                    text = Constants.DEMO_POLICY,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }

    LoadingScreen(loadingState = loadingState, modifier = Modifier.fillMaxSize())
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun PrevRegisterContent() {

    RegisterScreenContent(loadingState = false,
        navigateToSignUp = {},
        messageBarState = MessageBarState(),
        onRegister = { name, email, password ->

        },
        showPrivacyPolicy = {}
    )
}