package com.epilepto.dhyanapp.presentation.screens.auth.signin

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.epilepto.dhyanapp.presentation.components.GradientButton
import com.epilepto.dhyanapp.theme.NunitoSans
import com.epilepto.dhyanapp.theme.Yellow
import com.epilepto.dhyanapp.theme.primaryColor
import com.epilepto.dhyanapp.theme.textFieldContainer
import com.epilepto.dhyanapp.utils.SignInUtils
import com.stevdzasan.messagebar.MessageBarState

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

        GradientButton(
            title = "LOGIN",
            onClick = {
                onLoginWithEmail(email, password)
            }
        )

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


@Composable
fun AuthScreenContent(
    modifier: Modifier = Modifier,
    loadingState: Boolean,
    messageBarState: MessageBarState,
    onRegister: (String, String, String) -> Unit,
    onLoginWithGoogle: () -> Unit,
    onLoginWithEmail: (String, String) -> Unit,
    onForgotPassword: (String) -> Unit
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

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
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = ShapeDefaults.Large
                        )
                        .clip(ShapeDefaults.Large)
                        .align(Alignment.Center)

                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.blue_bg),
            contentDescription = "Blue Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                AuthTabItem(
                    title = "Login",
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                    }
                )
                AuthTabItem(
                    title = "Signup",
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                    }
                )
            }

            if (selectedItem == 0) {
                SignInContent(
                    modifier = Modifier.align(Alignment.Center),
                    messageBarState = messageBarState,
                    onLoginWithEmail = onLoginWithEmail,
                    onForgotPassword = onForgotPassword
                )
                SignInScreenBottomContent(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onLoginWithGoogle = onLoginWithGoogle,
                    navigateToRegister = {
                        selectedItem = 1
                    }
                )
            } else {
                SignUpContent(
                    modifier = Modifier.align(Alignment.Center),
                    messageBarState = messageBarState,
                    onRegister = onRegister
                )

                SignUpScreenBottomContent(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    navigateToLogin = {
                        selectedItem = 0
                    }
                )
            }
        }
    }
}

@Composable
fun SignInContent(
    modifier: Modifier = Modifier,
    messageBarState: MessageBarState,
    onLoginWithEmail: (String, String) -> Unit,
    onForgotPassword: (String) -> Unit
) {
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .width(160.dp)
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(Yellow)
                    .align(Alignment.CenterStart)
            )

            Text(
                text = "Login", style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.Center),
                fontWeight = FontWeight.SemiBold
            )
        }

        TextField(
            value = email,
            onValueChange = setEmail,
            placeholder = { Text(text = "Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = ShapeDefaults.Medium,
                    spotColor = Color.Gray
                ),
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
            shape = ShapeDefaults.Medium,
            trailingIcon = {
                Icon(imageVector = Icons.Outlined.Email, contentDescription = "email")
            }
        )

        TextField(
            value = password,
            onValueChange = setPassword,
            placeholder = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = ShapeDefaults.Medium,
                    spotColor = Color.Gray
                ),
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
            shape = ShapeDefaults.Medium,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Forgot Password?",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(12.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { onForgotPassword(email) }
                    ),
                color = Color(0xff8B8B8B)
            )
        }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            GradientButton(
                title = "Login",
                onClick = {
                    val errorMessage = SignInUtils.verifySignInDetails(email, password)
                    if (errorMessage.isNotEmpty()) {
                        messageBarState.addError(Exception(errorMessage))
                    } else {
                        onLoginWithEmail(email.trim(), password.trim())
                    }
                })
        }
    }
}

@Composable
fun SignInScreenBottomContent(
    modifier: Modifier = Modifier,
    onLoginWithGoogle: () -> Unit,
    navigateToRegister: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(12.dp)
            .padding(bottom = 24.dp)
    ) {
        Row {
            Text(
                text = "Don't have an account?",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Signup",
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clickable { navigateToRegister() }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Also Login With",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.padding(2.dp))

            IconButton(onClick = onLoginWithGoogle) {
                Icon(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "google",
                    tint = Color.Unspecified
                )
            }
        }

        Text(
            text = "By creating an account, I accept the Terms & Conditions & Privacy Policy",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    messageBarState: MessageBarState,
    onRegister: (String, String, String) -> Unit,
) {
    val (name, setName) = rememberSaveable { mutableStateOf("") }
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {

        Box(
            modifier = Modifier
                .width(160.dp)
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(Yellow)
                    .align(Alignment.CenterStart)
            )

            Text(
                text = "Signup", style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.Center),
                fontWeight = FontWeight.SemiBold
            )
        }

        TextField(
            value = name,
            onValueChange = setName,
            placeholder = { Text(text = "Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = ShapeDefaults.Medium,
                    spotColor = Color.Gray
                ),
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
            shape = ShapeDefaults.Medium,
            trailingIcon = {
                Icon(imageVector = Icons.Outlined.Person, contentDescription = "name")
            }
        )

        TextField(
            value = email,
            onValueChange = setEmail,
            placeholder = { Text(text = "Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = ShapeDefaults.Medium,
                    spotColor = Color.Gray
                ),
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
            shape = ShapeDefaults.Medium,
            trailingIcon = {
                Icon(imageVector = Icons.Outlined.Email, contentDescription = "email")
            }
        )

        TextField(
            value = password,
            onValueChange = setPassword,
            placeholder = { Text(text = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .shadow(
                    elevation = 18.dp,
                    shape = ShapeDefaults.Medium,
                    spotColor = Color.Gray
                ),
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
            shape = ShapeDefaults.Medium,
        )

        /*        Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "Forgot Password?",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { //onForgotPassword(email)
                                }
                            ),
                        color = Color(0xff8B8B8B)
                    )
                }*/

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
                    .clip(ShapeDefaults.Medium)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color(0xFFFEA53C),
                                Color(0xFFFB6A4F)
                            )
                        )
                    )
                    .clickable {
                        val errorMessage = SignInUtils.verifySignUpDetails(name, email, password)
                        if (errorMessage.isNotEmpty()) {
                            messageBarState.addError(Exception(errorMessage))
                        } else {
                            onRegister(name.trim(), email.trim(), password.trim())
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Signup",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun SignUpScreenBottomContent(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit
) {
    Column(modifier = modifier.padding(12.dp)) {
        Row {
            Text(
                text = "Already have an account?",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Login",
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clickable { navigateToLogin() }
            )
        }


        /*Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Also Signup With",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(5.dp))

            IconButton(onClick = {  }) {
                Icon(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "google",
                    tint = Color.Unspecified
                )
            }
        }*/

        Text(
            text = "By creating an account, I accept the Terms & Conditions & Privacy Policy",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun AuthTabItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(.3f)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    ) {
        Text(
            text = title, style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.W500
        )
        AnimatedVisibility(visible = selected) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Yellow)
            )
        }
    }
}

