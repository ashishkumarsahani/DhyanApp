package com.epilepto.dhyanapp.presentation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EnergySavingsLeaf
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import com.epilepto.dhyanapp.model.session.SessionState
import com.epilepto.dhyanapp.presentation.screens.main_screen.meditation.MeditationScreen
import com.epilepto.dhyanapp.presentation.screens.main_screen.pranayama.PranayamaScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.android.gms.wearable.Node
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreenBackup(
    pagerState: PagerState,
    signOut: () -> Unit,
    node: Node?,
    onConnectToWatch:()->Unit,
    sessionState: SessionState,
) {
    var isDropDownExpanded by remember { mutableStateOf(false) }
    val tabTitles = listOf("Home", "Meditation", "Pranayama")
    val activity = LocalContext.current as Activity
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hi, Harsh",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        modifier = Modifier.padding(12.dp)
                    )
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xffffe8ec),
                                shape = RoundedCornerShape(30)
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.EnergySavingsLeaf,
                            contentDescription = "leaf icon",
                            tint = Color(0xff83bb9e)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isDropDownExpanded = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "hamburger menu"
                        )
                    }
                    DropdownMenu(
                        expanded = isDropDownExpanded,
                        onDismissRequest = { isDropDownExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text(text = "Connect to Watch") },
                            onClick = onConnectToWatch
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Settings") },
                            onClick = { /*TODO: To be planned later*/ }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Sign Out") },
                            onClick = signOut
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            var selectedIndex by remember { mutableIntStateOf(0) }

            LaunchedEffect(selectedIndex) {
                pagerState.scrollToPage(selectedIndex)
            }

            LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
                if (!pagerState.isScrollInProgress) {
                    selectedIndex = pagerState.currentPage
                }
            }

            val indicator = @Composable { tabPositions: List<TabPosition> ->
                AnimatedIndicator(
                    tabPositions = tabPositions,
                    selectedIndex = selectedIndex
                )
            }

            TabRow(
                selectedTabIndex = selectedIndex,
                indicator = indicator,
                containerColor = Color.Transparent,
                divider = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)

            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = index == selectedIndex,
                        onClick = {
                            selectedIndex = index
                        },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedIndex == index)
                                    Color.White
                                else
                                    Color.Black
                            )
                        },
                        modifier = Modifier.zIndex(6f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))


            HorizontalPager(
                count = tabTitles.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> {
//                        HomeScreen(node = node,onConnectToWatch = onConnectToWatch)
//                        HomeScreenContent(node,sessionState = sessionState)
                    }
                    1 -> {
                        val coroutineScope = rememberCoroutineScope()
                        var refreshing by remember { mutableStateOf(false) }
                        MeditationScreen(
                            pullRefreshState = rememberPullRefreshState(
                                refreshing = refreshing,
                                onRefresh = {
                                    coroutineScope.launch {
                                        refreshing = true
                                        delay(2000)
                                        refreshing = false
                                    }
                                }),
                            refreshing = refreshing
                        )
                    }

                    2 -> PranayamaScreen()
                }
            }
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1);
        }
        else {
            // repeat the permission or open app details
        }
    }
}

@Composable
fun AnimatedIndicator(
    selectedIndex: Int,
    tabPositions: List<TabPosition>
) {
    val transition = updateTransition(selectedIndex, label = "indicator transition")
    val indicatorStart by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 50f)
            } else {
                spring(dampingRatio = 1f, stiffness = 600f)
            }
        },
        label = "indicator animation"
    ) {
        tabPositions[it].left
    }

    val indicatorEnd by transition.animateDp(
        label = "indicator end",
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 600f)
            } else {
                spring(dampingRatio = 1f, stiffness = 50f)
            }
        }
    ) {
        tabPositions[it].right
    }

    Box(
        modifier = Modifier
            .offset(x = indicatorStart)
            .wrapContentSize(align = Alignment.BottomStart)
            .width(indicatorEnd - indicatorStart)
            .padding(2.dp)
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(50)
            )
            .zIndex(1f)
            .padding(horizontal = 6.dp),
    )
}


