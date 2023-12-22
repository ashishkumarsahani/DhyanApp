package com.epilepto.dhyanapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.epilepto.dhyanapp.theme.primaryColor

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    loadingState:Boolean,
    tint: Color = primaryColor
) {
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
            Box(modifier = modifier) {
                CircularProgressIndicator(
                    color = tint,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
