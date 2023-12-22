package com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen.balanced_session

import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.CurvedModifier
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.foundation.basicCurvedText
import androidx.wear.compose.foundation.padding
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.presentation.presentation.utils.ProgressIndicatorSegment
import com.epilepto.dhyanapp.presentation.presentation.utils.SegmentedProgressIndicator
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import kotlinx.coroutines.delay
import com.epilepto.dhyanapp.R

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun Bal_SW_UI(IEratio: String, totalTime: Int, pranayamaSession:Int, timeRemainingInSeconds:Int): String {
    val (p, q, r, s) = IEratio.split(":").map { it.toFloat() }
    val innerProgress = remember { Animatable(0f) }
    var currentTime by remember { mutableStateOf(0f) }
    var breathState by remember { mutableStateOf("Inhale") }
    var segment = List(pranayamaSession) { ProgressIndicatorSegment(1f, Color.Green) }
    val meditation by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.meditation))

    var playTrackOne by remember { mutableStateOf("Inhale") } // This boolean determines which track to play
    var mediaPlayer: MediaPlayer? = null
    val context = LocalContext.current
    LaunchedEffect(playTrackOne) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(
            context,
            when(playTrackOne){
              "Inhale" ->  R.raw.inhale
                "Hold" -> R.raw.hold
              else ->  R.raw.exhale
            }
        )
        mediaPlayer?.start()
    }


    LaunchedEffect(key1 = IEratio) {
        while (currentTime < totalTime) {
            playTrackOne = "Inhale"
            breathState = "Inhale"
            // Animate to 1
            if (currentTime + p <= totalTime) {
                innerProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = TweenSpec(durationMillis = (p * 1000).toInt())
                )
                currentTime += p
            }

            // Hold at 1
            if (currentTime + q <= totalTime) {
                playTrackOne = "Hold"
                breathState = "Inhale Hold"
                delay((q * 1000).toLong())
                currentTime += q
            }

            // Animate to 0
            if (currentTime + r <= totalTime) {
                playTrackOne = "Exhale"
                breathState = "Exhale"
                innerProgress.animateTo(
                    targetValue = 0f,
                    animationSpec = TweenSpec(durationMillis = (r * 1000).toInt())
                )
                currentTime += r
            }

            // Hold at 0
            if (currentTime + s <= totalTime) {
                playTrackOne = "Hold"
                breathState = "Exhale Hold"
                delay((s * 1000).toLong())
                currentTime += s
            }
        }
    }
    var sessionsLeft by remember { mutableStateOf(1f) }  //number of session remaining scaled between (0 to 1)
    val totalAnimationTime = (p+q+r+s)
    if (((timeRemainingInSeconds) % totalAnimationTime).toInt() == 0) {
        sessionsLeft = ((timeRemainingInSeconds / totalAnimationTime)/pranayamaSession).toFloat()
    }

    SegmentedProgressIndicator(
        trackSegments = segment,
        progress = sessionsLeft,
        modifier = Modifier.fillMaxSize(),
        strokeWidth = 10.dp,
        paddingAngle = 2f,
        trackColor = Color.DarkGray
    )

    androidx.wear.compose.material.CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 13.dp),
        startAngle = 295.5f,
        endAngle = 245.5f,
        progress = innerProgress.value, // Ensure progress is within [0, 1]
        strokeWidth = 10.dp,
        indicatorColor = Color(0xFFFFBD48),
        trackColor = Color.DarkGray
    )

    Box(modifier = Modifier.fillMaxSize()){
        LottieAnimation(
            composition = meditation,
            progress = (innerProgress.value)*0.5f,
//            dynamicProperties = dynamicProperties,
            // You can add other parameters like Modifier if needed
        )

        // Align the Column to the bottom of the Box
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Align the column to the bottom center
                .fillMaxWidth() // Fill the maximum width of the Box
                .padding(25.dp), // Add some padding at the bottom
            horizontalAlignment = Alignment.CenterHorizontally // Center the children horizontally
        ) {
            CurvedLayout(modifier = Modifier.fillMaxSize()) {
                basicCurvedText(
                    breathState,
                    CurvedModifier.padding(10.dp),
                    style = {
                        CurvedTextStyle(
                            fontSize = 12.sp,
                            color = Color(0xFFFFBD48),
                            background = Color.Black
                        )
                    }
                )
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }
    return breathState
}

