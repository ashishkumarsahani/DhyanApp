package com.epilepto.dhyanapp.presentation.presentation.screens.home.meditationScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.epilepto.dhyanapp.presentation.presentation.theme.WearMessageSender
import com.epilepto.dhyanapp.R

@Composable
fun MeditationMusicSelectionScreen(
    navigateToTimer:(String)->Unit
) {
    val listState = rememberScalingLazyListState()
    val om by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.om))
    val silent by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.silent))
    val guided by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.guided))
    val data = WearMessageSender(LocalContext.current)
    data.sendMessage("/session_stated", "Demo".toByteArray())

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        item {
            Chip(
                onClick = {
                    navigateToTimer("Om Sound")
                },
                colors = ChipDefaults.secondaryChipColors(),
                label = { Text("Om Sound", color = Color.LightGray) },
                icon = {
                    LottieAnimation(
                        modifier = Modifier.size(30.dp),
                        composition = om,
                        iterations = LottieConstants.IterateForever
                    )
                })
        }

        //Calm
//        item {
//            Chip(
//                onClick = {
//                    navigateToTimer("Calm")
//                },
//                colors = ChipDefaults.secondaryChipColors(),
//                label = { Text("Calm Music", color = Color.LightGray) },
//                icon = {
//                    LottieAnimation(
//                        modifier = Modifier.size(40.dp),
//                        composition = calm,
//                        iterations = LottieConstants.IterateForever
//                    )
//                })
//        }

        //Guided Meditation
        item {
            Chip(
                onClick = {
                    navigateToTimer("Guided Meditation")
                },
                colors = ChipDefaults.secondaryChipColors(),
                label = { Text("Guided\nMeditation", color = Color.LightGray, textAlign = TextAlign.Center) },
                icon = {
                    LottieAnimation(
                        modifier = Modifier.size(40.dp),
                        composition = guided,
                        iterations = LottieConstants.IterateForever
                    )
                })
        }

        //Silent Meditation
        item {
            Chip(
                onClick = {
                    navigateToTimer("Silent Meditation")
                },
                colors = ChipDefaults.secondaryChipColors(),
                label = { Text("Silent\nMeditation", color = Color.LightGray, textAlign = TextAlign.Center) },
                icon = {
                    LottieAnimation(
                        modifier = Modifier.size(30.dp),
                        composition = silent,
                        iterations = LottieConstants.IterateForever
                    )
                })
        }
    }
}