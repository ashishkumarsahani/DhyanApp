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
import androidx.compose.ui.graphics.Color
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState

@Composable
fun AuthScreen(
    messageBarState: MessageBarState,
    loadingState: Boolean,
    onRegister: (String,String,String) -> Unit,
    onLoginWithGoogle: () -> Unit,
    onLoginWithEmail: (String, String) -> Unit,
    onForgotPassword:(String)->Unit
) {

    Scaffold(
        modifier = Modifier
            .background(Color(0xff2BB2F7))

    ) { padding ->
        ContentWithMessageBar(
            messageBarState = messageBarState,
            errorMaxLines = 3,
            successMaxLines = 3
        ) {
            AuthScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                loadingState = loadingState,
                messageBarState = messageBarState,
                onLoginWithGoogle = onLoginWithGoogle,
                onLoginWithEmail = onLoginWithEmail,
                onRegister = onRegister,
                onForgotPassword = onForgotPassword
            )
        }
    }

}