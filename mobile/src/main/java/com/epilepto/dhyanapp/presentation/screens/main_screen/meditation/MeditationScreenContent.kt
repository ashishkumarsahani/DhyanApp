package com.epilepto.dhyanapp.presentation.screens.main_screen.meditation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.presentation.screens.main_screen.components.AnimatedTimePicker
import com.epilepto.dhyanapp.presentation.screens.main_screen.components.CardHelperUtils
import com.epilepto.dhyanapp.presentation.screens.main_screen.components.HelperCard
import com.epilepto.dhyanapp.presentation.screens.main_screen.components.TimerCard
import com.epilepto.dhyanapp.theme.meditationCardColor
import com.epilepto.dhyanapp.theme.meditationCardColors

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MeditationScreenContent(
    pullRefreshState: PullRefreshState
) {
    var showTimerSection by remember { mutableStateOf(false) }
    var timer by remember {
        mutableIntStateOf(2)
    }

    AnimatedVisibility(visible = showTimerSection) {
        Dialog(onDismissRequest = { showTimerSection = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (timer > 2) timer -= 1
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBackIos,
                            contentDescription = "minus"
                        )
                    }

                    Spacer(modifier = Modifier.padding(10.dp))

                    AnimatedTimePicker(
                        time = timer,
                        textStyle = MaterialTheme.typography.displaySmall
                    )

                    Spacer(modifier = Modifier.padding(10.dp))


                    IconButton(onClick = {
                        if (timer < 20) timer += 1
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowForwardIos,
                            contentDescription = "minus"
                        )
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        TimerCard(
            title = "Meditation Timer",
            imagePainter = painterResource(id = R.drawable.meditation_timer_icon),
            time = 20,
            backgroundColor = meditationCardColor,
            onClick = {
                showTimerSection = true
            }
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(
                items = CardHelperUtils.meditationSounds,
                key = { index, item ->
                    item.title
                }) { index, item ->
                HelperCard(
                    title = item.title,
                    imagePainter = painterResource(id = item.icon),
                    time = 20,
                    backgroundColor = meditationCardColors[index],
                    onClick = {

                    }
                )
            }
        }
    }
}