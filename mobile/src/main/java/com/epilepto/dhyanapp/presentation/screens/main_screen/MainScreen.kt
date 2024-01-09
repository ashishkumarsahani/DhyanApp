package com.epilepto.dhyanapp.presentation.screens.main_screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EnergySavingsLeaf
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.model.user.User
import com.epilepto.dhyanapp.presentation.screens.main_screen.home.HomeScreenContent
import com.epilepto.dhyanapp.presentation.screens.main_screen.home.SessionScoreDataState
import com.epilepto.dhyanapp.presentation.screens.main_screen.meditation.MeditationScreen
import com.epilepto.dhyanapp.theme.Roboto
import com.epilepto.dhyanapp.presentation.screens.main_screen.pranayama.PranayamaScreen
import com.google.android.gms.wearable.Node
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    node: Node?,
    user: User?,
    onConnectToWatch: () -> Unit,
    sessionState: SessionScoreDataState,
    signOut: () -> Unit,
    deleteUser: () -> Unit,
) {

    var isDropDownExpanded by remember { mutableStateOf(false) }
    val activity = LocalContext.current as Activity
    val bottomNavController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    AnimatedVisibility(visible = user != null) {
        if (user != null) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//        containerColor = Color(0xFFf2f2f2),
                floatingActionButton = {
                    //   MainScreenBottomNavigation(navController = bottomNavController)
                },
                floatingActionButtonPosition = FabPosition.Center,
                topBar = {
                    TopAppBar(
                        title = {

/*                            val localDate = LocalDate.now()
                            val date = "${localDate.dayOfMonth} ${
                                localDate.month.name.take(3).lowercase()
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                            }*/

                            val topBarTitle = buildAnnotatedString {

                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                        color = Color.DarkGray
                                    )
                                ) {
                                    append("Welcome back,\n")
                                }

                                withStyle(
                                    style = SpanStyle(
                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                        fontFamily = Roboto
                                    )
                                ) {
                                    append(user.username)
                                }
                            }

                            Text(
                                text = topBarTitle, textAlign = TextAlign.Start,
                                modifier = Modifier.padding(start = 4.dp)
                            )

                        },
                        navigationIcon = {
                            Image(
                                painter = painterResource(
                                    id = if (user.gender == "Male") {
                                        R.drawable.male_avatar
                                    } else R.drawable.female_avatar
                                ),
                                contentDescription = "watch",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .padding(6.dp)

                            )
                        },
                        actions = {
                            IconButton(onClick = { isDropDownExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
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
                                /*                        DropdownMenuItem(
                                                            text = { Text(text = "Settings") },
                                                            onClick = { *//*TODO: To be planned later*//* }
                        )*/
                                DropdownMenuItem(
                                    text = { Text(text = "Delete Account") },
                                    onClick = deleteUser
                                )
                                DropdownMenuItem(
                                    text = { Text(text = "Sign Out") },
                                    onClick = signOut
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    MainScreenContent(
                        navController = bottomNavController,
                        sessionState = sessionState,
                        node = node,
                        user = user,
                        onConnectWatch = onConnectToWatch
                    )
                }
            }
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        } else {
            // repeat the permission or open app details
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreenContent(
    navController: NavHostController,
    sessionState: SessionScoreDataState,
    node: Node?,
    user: User?,
    onConnectWatch: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.HomeScreen.route
    ) {
        composable(Destinations.HomeScreen.route) {
            HomeScreenContent(
                user = user,
                sessionState = sessionState,
                node = node,
                onConnectWatch = onConnectWatch
            )
        }
        composable(Destinations.Favourite.route) {
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
        composable(Destinations.Notification.route) {
            PranayamaScreen()
        }
    }
}
