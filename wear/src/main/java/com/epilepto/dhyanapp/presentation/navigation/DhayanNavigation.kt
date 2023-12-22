package com.epilepto.dhyanapp.presentation.navigation

import android.app.Activity
import android.media.MediaPlayer
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.foundation.SwipeToDismissBox
import androidx.wear.compose.foundation.rememberSwipeToDismissBoxState
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import com.epilepto.dhyanapp.presentation.data.services.SharedPreferencesManager
import com.epilepto.dhyanapp.presentation.domain.models.sensor.SensorData
import com.epilepto.dhyanapp.presentation.domain.models.sessionData.Score
import com.epilepto.dhyanapp.presentation.domain.models.sessionData.SessionDetails
import com.epilepto.dhyanapp.presentation.presentation.screens.home.HomePage
import com.epilepto.dhyanapp.presentation.presentation.screens.home.SuccessAnimationScreen
import com.epilepto.dhyanapp.presentation.presentation.screens.home.TimerAnimation
import com.epilepto.dhyanapp.presentation.presentation.screens.home.meditationScreen.MeditationMusicSelectionScreen
import com.epilepto.dhyanapp.presentation.presentation.screens.home.meditationScreen.MeditationStopwatchScreen
import com.epilepto.dhyanapp.presentation.presentation.screens.home.meditationScreen.TestStopwatchScreen
import com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen.PranayamSessionScreen
import com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen.PranayamTimerScreen
import com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen.balanced_session.PranayamBalancedModeIESelectionScreen
import com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen.balanced_session.PranayamaBalancedModeStopwatch
import com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen.light_session.PranayamLightModeIESelectionScreen
import com.epilepto.dhyanapp.presentation.presentation.screens.home.sessionRecords.SessionRecordsDetails
import com.epilepto.dhyanapp.presentation.presentation.screens.home.sessionRecords.SessionRecordsScreen
import com.epilepto.dhyanapp.presentation.presentation.screens.home.userDetails.UserInfoScreen
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.ScoreViewModel
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.ScoresScreen
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.SharedScoreViewModel
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.SharedSessionDetailViewModel
import com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen.SharedUserInfoViewModel
import com.epilepto.dhyanapp.presentation.presentation.theme.SetTimerScreen
import com.epilepto.dhyanapp.presentation.presentation.theme.StartAnimation
import com.epilepto.dhyanapp.R
import com.epilepto.dhyanapp.presentation.presentation.screens.home.pranayamScreen.light_session.PranayamaLightModeStopwatchScreen
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun DhayanNavigation(
    navController: NavHostController,
) {
    val SharedScoreViewModel: SharedScoreViewModel = viewModel()
    val SharedUserInfoViewModel: SharedUserInfoViewModel = viewModel()
    val userID = SharedPreferencesManager.getUserId(LocalContext.current)      //Reading the UserID from the Shared Preferences from Data layer
    val SharedSessionDetailViewModel: SharedSessionDetailViewModel = viewModel()
    val swipeState = rememberSwipeToDismissBoxState()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route,
        modifier = Modifier.padding(5.dp),
    ) {
        //Navigation for SplashScreen and after this we are navigating to Homepage, where we can choose 4 option.
        composable(Routes.SplashScreen.route) {
            StartAnimation(
                navigateToMain = {
                    navController.navigate(Routes.Main.route)
                },
                SharedUserInfoViewModel = SharedUserInfoViewModel
            )
        }

        // Navigation for the Session Record Screen when user clicks on the "session records" option in the Homepage.
        composable(Routes.SessionRecords.route) {
            var loadingState by remember { mutableStateOf(true) }   //Variable to check the loading status of Firebase Data
            val scores = remember { mutableStateListOf<Score>() }      //Variable to Store the Scores value read from firebase, it is storing as "Score" Data class
            val sessionDetails: SessionDetails? by remember { mutableStateOf(null) }      //Making a variable to store the sessionDetails coming from Session Record Screen when user clicks on a specific sensor.
            LaunchedEffect(key1 = Unit) {
                try {
                    val db = Firebase.firestore
                    if (userID != null) {
                        db.collection("users")      //We are reading the data from firebase with the path user/UserID/Sessions. and inside Sessions we are having two records - MeditationRecords, PranayamRecords
                            .document(userID)                   //Storing USerID
                            .collection("Sessions")
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                loadingState = false                //When reading from FB, we are making the loadingState as False
                                for (document in querySnapshot.documents) {
                                    val meditationAnalysis = document.data?.get("meditationAnalysis") as? Map<*, *>     //Reading the data of "meditationAnalysis"
                                    val scoreMapMeditation = meditationAnalysis?.get("score") as? Map<*, *>
                                    val pranayamAnalysis = document.data?.get("pranayamAnalysis") as? Map<*, *>         //Reading the data of "pranayamAnalysis"
                                    val scoreMapPranayam = pranayamAnalysis?.get("score") as? Map<*, *>

                                    val scoreMap = if (document.data?.get("meditationAnalysis")!=null){
                                        meditationAnalysis?.get("score") as? Map<*, *>                  //We are reading the "scores" inside the meditationAnalysis
                                    } else {
                                        pranayamAnalysis?.get("score") as? Map<*, *>                    //We are reading the "scores" inside the pranayamAnalysis
                                    }
                                    if (scoreMap != null) {
                                        val type = scoreMap["type"] as? String
                                        val calendar = scoreMap["calender"] as? Long
                                        val dhyan = scoreMap["dhyan"] as? Double
                                        val aasana = scoreMap["aasana"] as? Double
                                        val duration = scoreMap["duration"] as? Double
                                        val prana = scoreMap["prana"] as? Double
                                        //Storing each sessionData into Score Data class
                                        val score = Score(
                                            type,
                                            calendar,
                                            dhyan?.toFloat(),
                                            aasana?.toFloat(),
                                            prana?.toFloat(),
                                            duration?.toFloat()
                                        )
                                        scores.add(score)
                                    }
                                }
                                scores.sortByDescending { it.calender }

                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Error getting documents: ", e)
                            }
                    }
                } catch (e: Exception) {
                    loadingState = false
                    Log.e("Session Records", e.message, e)
                }
            }
            SwipeToDismissBox(
                state = swipeState,
                onDismissed = { navController.navigate(Routes.Main.route) }
            ){
                AnimatedContent(targetState = sessionDetails, label = "Session Details") { data ->
                    if (data == null) {
                        SessionRecordsScreen(
                            navigateToSessionDetails= {
                                navController.navigate(Routes.SessionRecordsDetails.route)
                            },
                            loadingState,
                            scores,
                            sessionDetailViewModel = SharedSessionDetailViewModel
                        )
                    }
                }
            }
        }
        composable(Routes.UserInfo.route) {
            SwipeToDismissBox(
                state = swipeState,
                onDismissed = { navController.navigate(Routes.Main.route) }
            ){
                UserInfoScreen()
            }
        }

        //Navigation for Session Record Details when user clicks on a specific session to get the more details.
        composable(Routes.SessionRecordsDetails.route) {
            SwipeToDismissBox(
                state = swipeState,
                onDismissed = { navController.navigate(Routes.SessionRecords.route) }
            ){
                SessionRecordsDetails(
                    sessionDetailViewModel = SharedSessionDetailViewModel
                )
            }
        }

        //This is main HOMEPAGE Route which has 4 options to go.
        //Navigate to USerInfo, Navigate to Meditation, Navigate to Pranayam, Navigate to SessionRecords.
        composable(Routes.Main.route) {
            val context = LocalContext.current
            SwipeToDismissBox(
                state = swipeState,
                onDismissed = {
                    val activity = (context as? Activity)
                    activity?.finishAffinity()
                }
            ){
                HomePage(
                    navigateToMeditation = {
                        //This will navigate to meditation Session Screen to choose the session.
                        navController.navigate(Routes.MeditationSession.route)
                    },
                    navigateToPranayam = {
                        //This will navigate to Pranayam Session Screen to choose the mode - Light and Balanced .
                        navController.navigate(Routes.PranayamSession.route)
                    },
                    navigateToSessionRecords = {
                        //This will navigate to Session Records Screen which will have the previous session info
                        navController.navigate(Routes.SessionRecords.route)
                    },
                    navigateToUserInfo = {
                        //This will navigate to UserInfo Screen, which will have the basic info of user
                        navController.navigate(Routes.UserInfo.route)
                    }
                )
            }
        }


        // Navigation for Meditation Session Selection Screen
        //This is first Page where user lands after selecting the Meditation Option
        composable(Routes.MeditationSession.route) {
            SwipeToDismissBox(
                state = swipeState,
                onDismissed = { navController.navigate(Routes.Main.route) }
            ){
                MeditationMusicSelectionScreen(
                    //This Meditation selection returns a "session" which is a STRING type and indicates the type of music selected by user.
                    navigateToTimer = { session ->
                        //Here we are navigating it to the Meditation Timer
                        navController.navigate(Routes.MeditationTimer.passArguments(session))
                    }
                )
            }
        }

        // Navigation for Meditation Timer Selection Screen
        composable(
            route = Routes.MeditationTimer.route,
            arguments = listOf(navArgument("session") { type = NavType.StringType })
        ) { entry ->
            //We are getting session which is type of music selected by user, it is a STRING type.
            val session = entry.arguments?.getString("session")
            // Currently We are navigating to Timer Animation for development using navigateToTimerAnimation.
            if (session != null) {
                SwipeToDismissBox(
                    state = swipeState,
                    onDismissed = { navController.navigate(Routes.MeditationSession.route) }
                ){
                    SetTimerScreen(
                        session = session,
                        navigateToTimerAnimation = {duration ->
                            //Navigating it to Meditation Timer Animation
                            navController.navigate(
                                Routes.MeditationTimerAnimation.passArguments(
                                    duration,
                                    session
                                )
                            )
                        },
                        // We can also navigate to the TEST Meditation stopwatch page using navigatetoTest.
                        navigatetoTest = {
                                duration ->
                            navController.navigate(
                                Routes.MeditationTest.passArguments(
                                    duration,
                                    session
                                )
                            )
                        }
                    )
                }
            }
        }

        //Navigation for Meditation Timer Animation
        composable(
            route = Routes.MeditationTimerAnimation.route,
            arguments = listOf(
                navArgument("time") { type = NavType.IntType },
                navArgument("session") { type = NavType.StringType })
        ) { entry ->
            //It is taking two arguments which are sessions and the duration chosen by the user and navigating to Meditation Stopwatch page
            val time = entry.arguments?.getInt("time")
            val session = entry.arguments?.getString("session")
            if (time != null && session != null) {
                //pass time instead of duration after complete implementation
                //Here we are chosing animationTime as 3000 to navigate to Meditation Stopwatch, if it is 3010 then it will navigate it to Pranayam Stopwatch
                val animationTime = 3000
                SwipeToDismissBox(
                    state = swipeState,
                    onDismissed = { navController.navigate(
                        Routes.MeditationTimer.passArguments(
                            session
                        )
                    ) }
                ){
                    TimerAnimation(
                        animationTime = animationTime,
                        navigateToStopwatch = {
                            //Navigating it to Meditation Stopwatch
                            navController.navigate(
                                Routes.MeditationStopwatch.passArguments(
                                    time,
                                    session
                                )
                            )
                        },
                        navigateToPranayamStopwatch = {}
                    )
                }
                val activity = LocalContext.current as Activity
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }


        //Navigation for TEST Meditation Stopwatch
        composable(
            route = Routes.MeditationTest.route,
            arguments = listOf(
                navArgument("time") { type = NavType.IntType },
                navArgument("session") { type = NavType.StringType })
        ) { entry ->
            //It is taking session and duration values from the TimerAnimation.
            val time = entry.arguments?.getInt("time")
            val session = entry.arguments?.getString("session")
            var sensorData: SensorData? by remember { mutableStateOf(null) }   //variable to store the sensorData in a "SensorData" format, after the timer ends.

            if (time != null && session != null) {
                val context = LocalContext.current
                val mediaPlayer = when (session) {         //Choosing the Media Tracks based on the session value chosen by user.
                    "Om Sound" -> MediaPlayer.create(context, R.raw.omchants)


                    else -> MediaPlayer.create(context, R.raw.omchants)
                }
                //Making the Stopwatch starts when the sesnor data is empty and when it is not empty(timer is ended) we can navigate to the SuccessAnimationScreen.
                AnimatedContent(targetState = sensorData, label = "meditation") { data ->
                    if (data == null) {

                        TestStopwatchScreen(
                            mediaPlayer = mediaPlayer,
                            duration = time * 60 * 1000,
                            session = session,
                            onSuccess = {sensorData = it
                                    navController.navigate(
                                        Routes.MeditationSessionScore.passSensorData(
                                            sensorData!!
                                        )
                                )}
                        )
                    } else {
                        SuccessAnimationScreen(
                            sharedScoreViewModel = SharedScoreViewModel,
                            onFinish = {

                            }
                        )
                    }
                }

                val activity = LocalContext.current as Activity
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }




        //Navigation for Meditation Stopwatch
        composable(
            route = Routes.MeditationStopwatch.route,
            arguments = listOf(
                navArgument("time") { type = NavType.IntType },
                navArgument("session") { type = NavType.StringType })
        ) { entry ->
            val time = entry.arguments?.getInt("time")
            val session = entry.arguments?.getString("session")
            var sensorData: SensorData? by remember { mutableStateOf(null) }   //variable to store the sensorData in a "SensorData" format, after the timer ends.
//            val condition = SharedScoreViewModel.sensorData.observeAsState().value?.dhyanScore //variable to store the sensorData in a "SensorData" format, after the timer ends.
            if (time != null && session != null) {
                val context = LocalContext.current
                //Initializing the Media Player and passing it to Meditation Stopwatch.
                val mediaPlayer = when (session) {
                    "{Om Sound}" -> MediaPlayer.create(context, R.raw.omchants)

//                    "Calm" -> MediaPlayer.create(context, R.raw.calm_ani)

                    "{Guided Meditation}" -> {
                        MediaPlayer.create(context, R.raw.guided_meditation)
                    }

                    "{Silent Meditation}" -> null

                    else -> MediaPlayer.create(context, R.raw.omchants)
                }
                //Here we are checking if the sensorData(data) is empty or filled.
                //If it is empty then it should navigate to Stopwatch and take the sensorData(data) and
                //When it is filled the success animation is displayed and finally navigating it to Scoreboard
                AnimatedContent(targetState = sensorData, label = "pranayam") { data ->
                    if (data == null) {
//                        if(!mediaPlayer.isPlaying){
//                            mediaPlayer?.start()
//                        }
                        SwipeToDismissBox(
                            state = swipeState,
                            onDismissed = { navController.navigate(
                                Routes.MeditationTimer.passArguments(
                                    session
                                )
                            ) }
                        ){
                            MeditationStopwatchScreen(
                                mediaPlayer = mediaPlayer,
                                duration = time * 60 * 1000,
                                session = session,
                                onSucess = {sensorData = it }, //Storing the sensorData to the local variable in the navigation //Passing the sensorData after the aniamtion completed
                                sharedScoreViewModel = SharedScoreViewModel,
                            )
                        }
                    } else {
                        //Saving the sensorData and which we can access from anywhere from the code
                        SuccessAnimationScreen(
                            sharedScoreViewModel = SharedScoreViewModel,
                            onFinish = {
                                SharedScoreViewModel.updateSensorData(it)
                                //Navigating to the ScoreBoard after saving the sensorData
                                navController.navigate(
                                    Routes.MeditationSessionScore.route
                                )
                            }
                        )
                    }
                }
//                LaunchedEffect(Unit) {
//                    navController.currentBackStackEntryFlow.collect { entry ->
//                        if (entry.destination.route == Routes.MeditationStopwatch.route) {
//                            navController.popBackStack(Routes.MeditationTimer.route, inclusive = false)
//                        }
//                    }
//                }
                val activity = LocalContext.current as Activity
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)  //To keep the screen ON
            }
        }

        // Navigating to the ScoreBoard
        composable(
            route = Routes.MeditationSessionScore.route,
        ) { backStackEntry ->
            val viewModel: ScoreViewModel = viewModel()           //Initializing the ScoreViewModel
            val context = LocalContext.current
            val state = viewModel.state.collectAsState().value    //Taking the State Value to know is the data saved to FB or error
            val pranayamSession = SharedScoreViewModel.sensorData.value?.pranayamSession

            LaunchedEffect(key1 = state) {
                if (state.error.isNotEmpty()) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()           //Making the Toast as Error
                }
                if (state.isLoading) {
                    Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()      //When it is trying to save the data
                }
                if (state.isSuccessful) {
                    Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()     //When saved successfully
                }
            }
            //Going into the Score Screen Composable and taking inputs for OnDone Button, Save the Session Button
            SwipeToDismissBox(
                state = swipeState,
                onDismissed = { navController.navigate(Routes.Main.route) }
            ){
                ScoresScreen(
                    //At OnDone We are navigating to the Homepage
                    onDone = {
                        navController.navigate(Routes.Main.route)   //Navigation for HomePage
                    },
                    navigateToPranayamSession = {
                        navController.popBackStack()
                        navController.navigate(Routes.PranayamSession.route)
                    },
                    //When Clicked on Save The Session it is passing the Data to FB
                    onSave = {
                        if (userID != null) {
                            viewModel.saveAnalysisToFirebase(
                                userId =  userID,
                                sharedScoreViewModel = SharedScoreViewModel
                            )
                        }
                        navController.popBackStack(Routes.Main.route, inclusive = false)
                    },
                    ScoreviewModel = SharedScoreViewModel,             //Passing the ScoreViewModel to pass the Scores of session for displaying
                    UserInfoViewModel = SharedUserInfoViewModel       //Passing the ShareUSerViewModel to pass the user info
                )
            }
//            LaunchedEffect(Unit) {
//                navController.currentBackStackEntryFlow.collect { entry ->
//                    if (entry.destination.route == Routes.MeditationSessionScore.route) {
//                        navController.popBackStack(Routes.Main.route, inclusive = false)
//                    }
//                }
//            }
        }

        //Navigation for Pranayam Session
        //Here we are landing after we click the Paranyam Option in the Homepage
        //Here user chose the mode type (Light or Balanced)
        composable(Routes.PranayamSession.route) {
            //Going to Pranayam Session Screen
            SwipeToDismissBox(
                state = swipeState,
                onDismissed = { navController.navigate(Routes.Main.route) }
            ){
                PranayamSessionScreen(
                    //Navigating to Paranayam Timer
                    navigateToPranayamTimer = { pranayamaMode, pranayamaSession ->
                        navController.navigate(
                            Routes.PranayamTimer.passArguments(
                                pranayamaMode,
                                pranayamaSession
                            )
                        )           //Saving the PranayamMode chosen by user
                    }
                )
            }
        }

        //Navigating to Pranayam Timer where user chooses the number of sessions
        //Then based on the mode selected it is navigating to Different Inahle Exhale(IE selection) Ratio Page
        composable(
            route = Routes.PranayamTimer.route,
            arguments = listOf(
                navArgument("pranayamaMode") { type = NavType.StringType },
                navArgument("pranayamaSession") { type = NavType.IntType }
                )
        ) { entry ->
            val pranayamaSession = entry.arguments?.getInt("pranayamaSession")
            val pranayamaMode = entry.arguments?.getString("pranayamaMode")
            if (pranayamaSession != null && pranayamaMode != null) {
                //Passed Session as 0 because its not in use yet
                SwipeToDismissBox(
                    state = swipeState,
                    onDismissed = { navController.navigate(Routes.PranayamSession.route) }
                ){
                    PranayamTimerScreen(
                        pranayamaMode = pranayamaMode,
                        navigateToMeditation = {
                            navController.navigate(Routes.MeditationSession.route)
                        },
                        navigateToPranayamLightMode = { pranayamaSession ->          //Reading the Session Value chosen by user
                            //Navigating it to Light IE Selection Screen
                            navController.navigate(
                                Routes.PranayamLightModeIESelection.passArguments(
                                    pranayamaMode,
                                    pranayamaSession
                                )
                            )
                        },
                        navigateToPranayamBalancedMode = { pranayamaSession ->      //Reading the Session Value chosen by user
                            //Navigating it to Balanced IE Selection Screen
                            navController.navigate(
                                Routes.PranayamBalancedModeIESelection.passArguments(
                                    pranayamaMode,
                                    pranayamaSession
                                )
                            )
                        }
                    )
                }
            }
        }

        //Navigate to Light IE Selection Screen
        //Here user choosing different Light MOde IE Selection Ratio and navigate it to Pranayam Light Mode Stopwatch
        composable(
            route = Routes.PranayamLightModeIESelection.route,
            arguments = listOf(
                navArgument("pranayamaSession") { type = NavType.IntType },
                navArgument("pranayamaMode") { type = NavType.StringType },
            )
        ) { entry ->
            val pranayamaSession = entry.arguments?.getInt("pranayamaSession")
            val pranayamaMode = entry.arguments?.getString("pranayamaMode")
            if (pranayamaSession != null && pranayamaMode != null) {
                //Creating the IE Selection Screen
                SwipeToDismissBox(
                    state = swipeState,
                    onDismissed = { navController.navigate(
                        Routes.PranayamTimer.passArguments(
                            pranayamaMode,
                            pranayamaSession
                        )
                    )   }
                ){
                    PranayamLightModeIESelectionScreen(
                        pranayamaSession = pranayamaSession,
                        pranayamaMode = pranayamaMode,
                        navigateToPranayamTimerAnimation = { IEratio ->             //Reading the IE selection ratio chosen by user
                            //Navigating to Pranayam Timer Animation
                            navController.navigate(
                                Routes.PranayamTimerAnimation.passArguments(
                                    pranayamaMode, pranayamaSession, IEratio
                                )
                            )
                        }
                    )
                }
            }
        }

        //Navigate to Balanced Mode IE Selection Ratio
        //Here user choosing different balanced Mode IE Selection Ratio and navigate it to Pranayam Balnced Mode Stopwatch
        composable(
            route = Routes.PranayamBalancedModeIESelection.route,
            arguments = listOf(
                navArgument("pranayamaMode") { type = NavType.StringType },
                navArgument("pranayamaSession") { type = NavType.IntType },
            )
        ) { entry ->
            val pranayamaMode = entry.arguments?.getString("pranayamaMode")
            val pranayamaSession = entry.arguments?.getInt("pranayamaSession")
            if (pranayamaMode != null && pranayamaSession != null) {
                //Creating the Balanced Mode IE selection Screen
                SwipeToDismissBox(
                    state = swipeState,
                    onDismissed = { navController.navigate(
                        Routes.PranayamTimer.passArguments(
                            pranayamaMode,
                            pranayamaSession
                        )
                    )   }
                ){
                    PranayamBalancedModeIESelectionScreen(
                        pranayamaMode = pranayamaMode,
                        navigateToStopWatch = { IEratio ->       //Reading the IE Ratio chosen by user
                            //Navigating it to 321 Timer Animation Screen
                            navController.navigate(
                                Routes.PranayamTimerAnimation.passArguments(
                                    pranayamaMode,
                                    pranayamaSession,
                                    IEratio
                                )
                            )
                        }
                    )
                }
            }

        }

        //Navigating to Pranayam Timer 321 Aniamtion from Both IE Selection Screen of Light and Balanced Mode
        composable(
            route = Routes.PranayamTimerAnimation.route,
            arguments = listOf(
                navArgument("pranayamaMode") { type = NavType.StringType },
                navArgument("pranayamaSession") { type = NavType.IntType },
                navArgument("IEratio") { type = NavType.StringType })
        ) { entry ->
            val pranayamaMode = entry.arguments?.getString("pranayamaMode")
            val pranayamaSession = entry.arguments?.getInt("pranayamaSession")
            val IEratio = entry.arguments?.getString("IEratio")
            //Here we are chosing time as 3010 ms in order to navigate to Pranayam Stopwatch, if it is 3000 it will navigate it to Meditation Stopwatch
            val animationTime = 3010
            if (pranayamaMode != null && pranayamaSession != null && IEratio != null) {
                SwipeToDismissBox(
                    state = swipeState,
                    onDismissed = {
                        if(pranayamaMode == "LIGHT_MODE"){
                        //Navigating to Light Mode IESelection
                        navController.navigate(
                            Routes.PranayamLightModeIESelection.passArguments(
                                pranayamaMode,
                                pranayamaSession
                            )
                        )
                    }
                    else{
                        //Navigating to Balanced Mode IESelection
                        navController.navigate(
                            Routes.PranayamBalancedModeIESelection.passArguments(
                                pranayamaMode,
                                pranayamaSession
                            )
                        )
                    }   }
                ){
                    TimerAnimation(
                        animationTime = animationTime,
                        navigateToStopwatch = {},
                        navigateToPranayamStopwatch = {
                            //After Animation got completed
                            if(pranayamaMode == "LIGHT_MODE"){
                                //Navigating to Light Mode Stopwatch
                                navController.navigate(
                                    Routes.PranayamLightModeStopwatch.passArguments(
                                        pranayamaMode,
                                        pranayamaSession,
                                        IEratio
                                    )
                                )
                            }
                            else{
                                //Navigating to Balanced Mode Stopwatch
                                navController.navigate(
                                    Routes.PranayamBalancedModeStopwatch.passArguments(
                                        pranayamaMode,
                                        pranayamaSession,
                                        IEratio
                                    )
                                )
                            }
                        }
                    )
                }
            }
            val activity = LocalContext.current as Activity
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        //Naviagte it to Pranayam Light Mode Stopwatch
        composable(
            route = Routes.PranayamLightModeStopwatch.route,
            arguments = listOf(
                navArgument("pranayamaMode") { type = NavType.StringType },
                navArgument("pranayamaSession") { type = NavType.IntType },
                navArgument("IEratio") { type = NavType.StringType })
        ) { entry ->
            //Taking 3 arguments as Mode, number of session and IE Selection Ratio
            val pranayamaMode = entry.arguments?.getString("pranayamaMode")
            val pranayamaSession = entry.arguments?.getInt("pranayamaSession")
            val IEratio = entry.arguments?.getString("IEratio")
            var sensorData by remember { mutableStateOf<SensorData?>(null) }   //variable to store the sensorData in a "SensorData" format, after the timer ends.
            var flag by remember { mutableIntStateOf(0) }           //To make it only called once
            //Here we are checking if the sensorData(data) is empty or filled.
            //If it is empty then it should navigate to Stopwatch and take the sensorData(data) and
            //When it is filled the success animation is displayed and finally navigating it to Scoreboard
            SwipeToDismissBox(
                state = swipeState,
                onDismissed = {
                    navController.navigate(
                        Routes.PranayamLightModeIESelection.passArguments(
                            pranayamaMode!!,
                            pranayamaSession!!
                        )
                    )
                }
            ){
                AnimatedContent(targetState = sensorData, label = "pranayam") { data ->
                    if (data == null && flag == 0) {
                        if (pranayamaMode != null && pranayamaSession != null && IEratio != null) {
                            PranayamaLightModeStopwatchScreen(
                                pranayamaMode = pranayamaMode,
                                pranayamaSession = pranayamaSession,
                                IEratio = IEratio,
                                onSuccess = {
                                    flag = 1                      //To make it only called for once for updating sensor data
                                    sensorData = it },            //Storing the sensorData to the local variable in the navigation
                                scoreViewModel = SharedScoreViewModel
                            )
                        }
                    } else if (flag == 1) {
                        Log.d("", "At Success nav before on finish animation")
                        //Saving the sensorData and which we can access from anywhere from the code
                        SuccessAnimationScreen(
                            sharedScoreViewModel = SharedScoreViewModel,
                            onFinish = {
                                flag = 2                      //To make it only called for once for score calculation
                                SharedScoreViewModel.updateSensorData(it)
//                            sensorData?.let { Routes.PranayamSessionScore.passSensorData(it) }     //Passing the sensorData after the aniamtion completed
                                navController.navigate(Routes.PranayamSessionScore.route)
                            }
                        )
                    }
                    else if (flag ==2){             //if flag = 2 then it has sensor data and all the scores are calculated
                        navController.navigate(Routes.PranayamSessionScore.route)
                    }

                }
            }
            val activity = LocalContext.current as Activity
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }



        composable(
            route = Routes.PranayamBalancedModeStopwatch.route,
            arguments = listOf(navArgument("pranayamaMode") { type = NavType.StringType },
                navArgument("pranayamaSession") { type = NavType.IntType },
                navArgument("IEratio") { type = NavType.StringType })
        ) { entry ->
            val pranayamaMode = entry.arguments?.getString("pranayamaMode")
            val pranayamaSession = entry.arguments?.getInt("pranayamaSession")
            val IEratio = entry.arguments?.getString("IEratio")
            var sensorData by remember { mutableStateOf<SensorData?>(null) }         //variable to store the sensorData in a "SensorData" format, after the timer ends.
            var flag by remember { mutableIntStateOf(0) }           //To make it only called once

            //Here we are checking if the sensorData(data) is empty or filled.
            //If it is empty then it should navigate to Stopwatch and take the sensorData(data) and
            //When it is filled the success animation is displayed and finally navigating it to Scoreboard
            SwipeToDismissBox(
                state = swipeState,
                onDismissed = {
                    navController.navigate(
                        Routes.PranayamBalancedModeIESelection.passArguments(
                            pranayamaMode!!,
                            pranayamaSession!!
                        )
                    )
                }
            ){
                AnimatedContent(targetState = sensorData, label = "pranayam") { data ->
                    if (data == null && flag == 0) {
                        if (pranayamaMode != null && pranayamaSession != null && IEratio != null) {
                            PranayamaBalancedModeStopwatch(
                                pranayamaMode = pranayamaMode,
                                pranayamaSession = pranayamaSession,
                                IEratio = IEratio,
                                onSuccess = {
                                    flag = 1
                                    sensorData = it },             //Storing the sensorData to the local variable in the navigation
                                scoreViewModel = SharedScoreViewModel
                            )
                        }
                    } else if(flag == 1){
                        SuccessAnimationScreen(
                            sharedScoreViewModel = SharedScoreViewModel,
                            onFinish = {
                                flag = 2
                                SharedScoreViewModel.updateSensorData(it)
//                            sensorData?.let { Routes.PranayamSessionScore.passSensorData(it) }     //Passing the sensorData after the aniamtion completed
                                navController.navigate(Routes.PranayamSessionScore.route)
                            }
                        )
                    }
                    else if( flag ==2){
                        navController.navigate(Routes.PranayamSessionScore.route)
                    }
                }
            }
            val activity = LocalContext.current as Activity
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        //Navigation to Pranayam ScoreBoard
        //Here we are navigating from both of the Light and Balanced Pranayam Stopwatch and saving the data to the FB
        composable(
            route = Routes.PranayamSessionScore.route
        ) { backStackEntry ->
            val viewModel: ScoreViewModel = viewModel()             //Initializing the ScoreViewModel for saving the Data to FB
            val context = LocalContext.current
            val state = viewModel.state.collectAsState().value      //Reading the different States of FB

            LaunchedEffect(key1 = state) {
                if (state.error.isNotEmpty()) {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()           //Toast for error State
                }
                if (state.isLoading) {
                    Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()    //Toast for Loading State
                }
                if (state.isSuccessful) {
                    Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()   //Toast for Saved Sucessfully State
                }
            }
            //Creating the ScoreBoard Screen
            SwipeToDismissBox(
                state = swipeState,
                onDismissed = {
                    navController.navigate(Routes.Main.route)
                }
            ){
                ScoresScreen(
                    onDone = {
                        //Navigating it to the Main Screen After the Done Button Click
                        navController.popBackStack()
                        navController.navigate(Routes.Main.route)
                    },
                    navigateToPranayamSession = {
                        navController.popBackStack()
                        navController.navigate(Routes.PranayamSession.route)
                    },
                    onSave = {
                        //Saving the Session Score in the FB
                        if (userID != null) {
                            viewModel.saveAnalysisToFirebase(
                                userId = userID,
                                sharedScoreViewModel = SharedScoreViewModel
                            )
                        }
                    },
                    ScoreviewModel = SharedScoreViewModel,
                    UserInfoViewModel = SharedUserInfoViewModel()

                )
            }
        }
    }
}
