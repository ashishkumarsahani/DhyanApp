package com.epilepto.dhyanapp.presentation.screens.auth.register

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState

@Composable
fun RegisterScreen(
    messageBarState: MessageBarState,
    loadingState: Boolean,
    navigateToSignIn: () -> Unit,
    showPrivacyPolicy: () -> Unit,
    onRegister: (String, String, String) -> Unit
) {
    ContentWithMessageBar(
        messageBarState = messageBarState,
        errorMaxLines = 3
    ) {
        RegisterScreenContent(
            modifier = Modifier.fillMaxSize(),
            messageBarState = messageBarState,
            loadingState = loadingState,
            navigateToSignUp = navigateToSignIn,
            onRegister = onRegister,
            showPrivacyPolicy = showPrivacyPolicy
        )
    }
}


