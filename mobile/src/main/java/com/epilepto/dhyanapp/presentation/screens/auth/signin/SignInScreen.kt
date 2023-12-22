package com.epilepto.dhyanapp.presentation.screens.auth.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.epilepto.dhyanapp.utils.Constants
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@Composable
fun SignInScreen(
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
    loadingState: Boolean,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
    onLoginWithGoogle: () -> Unit,
    onLoginWithFacebook: () -> Unit,
    onLoginWithEmail: (String, String) -> Unit,
    onForgotPassword:(String)->Unit
) {

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding()

    ) { padding ->
        ContentWithMessageBar(
            messageBarState = messageBarState,
            errorMaxLines = 3
        ) {
            SignInScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                loadingState = loadingState,
                onLoginWithGoogle = onLoginWithGoogle,
                onLoginWithFacebook = onLoginWithFacebook,
                onLoginWithEmail = onLoginWithEmail,
                navigateToRegister = navigateToRegister,
                onForgotPassword = onForgotPassword
            )
        }
    }

    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId = Constants.CLIENT_ID,
        onTokenIdReceived = onTokenIdReceived,
        onDialogDismissed = onDialogDismissed
    )

}