package com.epilepto.dhyanapp.presentation.screens.main_screen.home

import android.graphics.Picture
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.model.user.User
import com.epilepto.dhyanapp.presentation.components.LoadingScreen
import com.epilepto.dhyanapp.presentation.screens.main_screen.home.session_result.SessionCard
import com.epilepto.dhyanapp.theme.BlackCardColor
import com.epilepto.dhyanapp.theme.Roboto
import com.epilepto.dhyanapp.theme.WhiteCardColor
import com.epilepto.dhyanapp.utils.capture
import com.epilepto.dhyanapp.utils.format
import com.epilepto.dhyanapp.utils.saveAndShareSession
import com.google.android.gms.wearable.Node
import kotlinx.coroutines.launch

@Composable
fun HomeScreenContent(
    node: Node?,
    user: User?,
    sessionState: SessionScoreDataState,
    onConnectWatch: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = sessionState.error) {
        if (sessionState.error.isNotEmpty())
            Toast.makeText(context, sessionState.error, Toast.LENGTH_SHORT).show()
    }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var showScrollToTopButton by remember { mutableStateOf(false) }
    var lastScrollIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(listState) {
        listState.interactionSource.interactions.collect {
            if (listState.firstVisibleItemIndex > lastScrollIndex) {
                // Scrolling down
                showScrollToTopButton = true
            } else if (listState.firstVisibleItemScrollOffset < lastScrollIndex ) {
                // Scrolling up and at the top
                showScrollToTopButton = false
            }
            lastScrollIndex = listState.firstVisibleItemIndex
        }
    }

    LoadingScreen(loadingState = sessionState.isLoading)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val meditationScore =
            sessionState.meditationAnalysis.mapNotNull { it.score }

        val pranayamScore =
            sessionState.pranayamAnalysis.mapNotNull { it.score }

        val avgMedDuration =
            meditationScore.mapNotNull { it.duration }.filter { it > 0 }.let { it.sum() / it.size }
                .toDouble() / 1000


        val avgPranaDuration =
            pranayamScore.mapNotNull { it.duration }.filter { it > 0 }.let { it.sum() / it.size }
                .toDouble() / 1000

        val sortedScores = (meditationScore + pranayamScore).filter {
            it.duration?.let { dur -> dur > 0 } ?: false
        }.sortedByDescending { it.calender }

        val avgDhyanScore =
            sortedScores.mapNotNull { it.dhyan }.filter { !it.isNaN() && it > 0 }
                .map { it / 100 }
                .filter { it <= 1 }
                .let { it.sum() / it.size }

        val avgAasanScore =
            sortedScores.mapNotNull { it.aasana }.filter { !it.isNaN() && it > 0 }
                .map { it / 100 }
                .filter { it <= 1 }
                .let { it.sum() / it.size }

        val avgPranaScore =
            sortedScores.mapNotNull { it.prana }.filter { !it.isNaN() && it > 0 }
                .map { it / 100 }
                .filter { it <= 1 }
                .let { it.sum() / it.size }

        Log.e(
            "Home Screen",
            "Dhyan :$avgDhyanScore\n" +
                    "Aasana :$avgAasanScore\n" +
                    "Prana :$avgPranaScore\n"
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = listState
        ) {

            /* item {
                 CircularScoreBoard(
                     modifier = Modifier
                         .fillMaxWidth()
                         .background(Color.Black)
                         .padding(12.dp),
                     dhyanScore = 0.9f,
                     aasanScore = 0.6f,
                     pranaScore = 0.3f,
                     ringSize = 200.dp
                 )
             }*/

            item {
                if (user != null) {
                    UserProfileCard(
                        user = user,
                        avgMedDuration = avgMedDuration,
                        avgPranaDuration = avgPranaDuration
                    )
                }
            }

            /*            item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                shape = ShapeDefaults.Medium
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {

                                }
                            }
                        }*/

            item {
                WatchConnectionCard(
                    node = node,
                    onRetry = onConnectWatch
                )
            }

            item {
                Text(
                    text = "Previous Sessions",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Medium
                )
            }


            itemsIndexed(sortedScores) { index, score ->
                val picture = remember { Picture() }

                SessionCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .capture(picture)
                        .padding(8.dp),
                    score = score,
                    onClick = {
                        saveAndShareSession(
                            context = context,
                            picture = picture,
                            index = index,
                            score = score
                        )
                    },
                    backgroundColor = WhiteCardColor
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AnimatedVisibility(visible = showScrollToTopButton) {

            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                containerColor = BlackCardColor,
                content = {
                    Icon(
                        imageVector = Icons.Outlined.ArrowUpward,
                        contentDescription = "scroll to top",
                        tint = Color.White
                    )
                }
            )
        }
    }
}

@Composable
fun WatchConnectionCard(
    node: Node?,
    onRetry: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = (0.15 * screenHeight).dp)
                .background(color = BlackCardColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.smart_watch_2),
                contentDescription = "watch",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .scale(.8f)
                    .padding(6.dp)
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {

                Text(
                    text = node?.displayName ?: "WearOS Not Connected",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (node == null) Arrangement.SpaceAround else Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(ShapeDefaults.Small.topStart))
                            .background(color = Color.Gray)
                    ) {
                        Text(
                            text = if (node != null) "Connected"
                            else "Not Connected",
                            color = Color.White,
                            modifier = Modifier.padding(6.dp)
                        )
                    }

                    if (node == null) {
                        Button(
                            onClick = onRetry,
                            shape = ShapeDefaults.Small
                        ) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UserProfileCard(
    user: User,
    avgMedDuration: Double,
    avgPranaDuration: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhiteCardColor
        )

    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = (0.3 * screenHeight).dp)
                .padding(8.dp)

        ) {
            Column(
                modifier = Modifier.padding(start = 6.dp)
            ) {

                Text(
                    text = "${user.username}, ${user.age} ",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    fontFamily = Roboto
                )
                Text(
                    text = user.goal,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = Roboto

                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Row(
                    modifier = Modifier.weight(0.6f),
                ) {
                    Card(
                        modifier = Modifier
                            .padding(6.dp)
                            .weight(.5f),
                        shape = ShapeDefaults.Small
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Meditation",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(6.dp)
                            )

                            Text(
                                text = "${avgMedDuration.format(1)} sec",
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }


                    Card(
                        modifier = Modifier
                            .padding(6.dp)
                            .weight(.5f),
                        shape = ShapeDefaults.Small
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Pranayam",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(6.dp)
                            )

                            Text(
                                text = "${avgPranaDuration.format(1)} sec",
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


