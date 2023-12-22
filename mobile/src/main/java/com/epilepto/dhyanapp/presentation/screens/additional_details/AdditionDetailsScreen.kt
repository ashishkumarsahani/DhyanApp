package com.epilepto.dhyanapp.presentation.screens.additional_details

import androidx.compose.runtime.Composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AdditionalDetailsScreen(
    loadingState:Boolean,
    pagerState: PagerState,
    messageBarState: MessageBarState,
    onFinish: (String, Int, Goal) -> Unit
) {
    ContentWithMessageBar(messageBarState = messageBarState) {


        AdditionalDetailsScreenContent(
            loadingState = loadingState,
            pagerState = pagerState,
            messageBarState = messageBarState,
            onFinish = onFinish
        )
    }
}