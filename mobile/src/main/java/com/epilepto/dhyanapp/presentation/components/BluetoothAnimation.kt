package com.epilepto.dhyanapp.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BluetoothSearching
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun BluetoothPairingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BluetoothPairingAnimation()
    }
}

@Composable
fun BluetoothPairingAnimation() {

    val infiniteTransition = rememberInfiniteTransition(label = "Infinite Anim")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(
            delayMillis = 1000
        ), RepeatMode.Restart),
        label = "scale"
    )

    val radius by infiniteTransition.animateFloat(
            initialValue = 50f,
            targetValue = 300f,
            animationSpec = infiniteRepeatable(tween(
                easing = FastOutSlowInEasing,
                durationMillis = 2000
            ), RepeatMode.Restart),
            label = "scale"
        )


    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(tween(
            easing = LinearEasing,
            durationMillis = 2000
        ), RepeatMode.Restart),
        label = "scale"
    )


    val painter = rememberVectorPainter(image = Icons.Outlined.BluetoothSearching)

    Canvas(modifier = Modifier.fillMaxSize()){

        with(painter) {
            draw(painter.intrinsicSize)
        }

        drawCircle(
            color = Color.Blue,
            radius =radius,
            alpha = alpha,
            style = Stroke(width = 6f)
        )
    }
}
