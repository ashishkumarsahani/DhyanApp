package com.epilepto.dhyanapp.presentation.screens.main_screen.meditation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MeditationScreen(
    refreshing:Boolean,
    pullRefreshState:PullRefreshState
) {
    Box(modifier = Modifier.fillMaxSize()) {

        MeditationScreenContent(
            pullRefreshState = pullRefreshState,
        )

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}