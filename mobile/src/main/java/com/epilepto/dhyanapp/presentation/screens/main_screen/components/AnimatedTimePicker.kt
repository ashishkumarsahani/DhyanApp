package com.epilepto.dhyanapp.presentation.screens.main_screen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun AnimatedTimePicker(
    time: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium
) {

    var oldTime by remember { mutableIntStateOf(0) }

    SideEffect {
        oldTime = time
    }

    Row(modifier = modifier) {
        val timeString = time.toString()
        val oldTimeString = oldTime.toString()
        for (i in timeString.indices) {
            val oldChar = oldTimeString.getOrNull(i)
            val newChar = timeString[i]
            val char = if (oldChar == newChar) {
                oldTimeString[i]
            } else {
                timeString[i]
            }

            AnimatedContent(
                targetState = char,
                label = "",
                transitionSpec = {
                    ( slideInVertically{ -it })
                        .togetherWith(
                            (fadeOut() + slideOutVertically())
                        )
                }
            ) { timeChar ->

                Text(
                    text = timeChar.toString(),
                    style = textStyle,
                    softWrap = false
                )
            }
        }
    }
}