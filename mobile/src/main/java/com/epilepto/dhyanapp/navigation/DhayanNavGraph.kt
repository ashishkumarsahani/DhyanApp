package com.epilepto.dhyanapp.navigation

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.epilepto.dhyanapp.presentation.screens.additional_details.AdditionalDetailsScreen
import com.epilepto.dhyanapp.presentation.screens.auth.AuthenticationViewModel
import com.epilepto.dhyanapp.presentation.screens.auth.register.RegisterScreen
import com.epilepto.dhyanapp.presentation.screens.auth.signin.AdditionalDetailsResponse
import com.epilepto.dhyanapp.presentation.screens.auth.signin.SignInScreen
import com.epilepto.dhyanapp.presentation.screens.main_screen.MainScreen
import com.epilepto.dhyanapp.presentation.screens.main_screen.components.DisplayAlertDialog
import com.epilepto.dhyanapp.presentation.screens.main_screen.home.SessionViewModel
import com.epilepto.dhyanapp.presentation.screens.onboarding.OnBoardingScreen
import com.epilepto.dhyanapp.presentation.screens.pairing.PairingPermissionScreen
import com.epilepto.dhyanapp.utils.SignInUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.datalayer.phone.PhoneDataLayerAppHelper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState


@OptIn(ExperimentalHorologistApi::class)
@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
    phoneDataLayerAppHelper: PhoneDataLayerAppHelper,
) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        loadingScreen()

        onboardingScreen(
            onClick = {
                navController.popBackStack()
                navController.navigate(Screen.SignIn.route)
            }
        )

        signInScreen(
            navigateToHome = {
                authViewModel.setFirstTime(false)
                authViewModel.setAdditionalDetailsFilled(true)
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            },
            navigateToAdditionalDetails = {
                authViewModel.setFirstTime(false)
                navController.popBackStack()
                navController.navigate(Screen.AdditionalDetails.route)
            },
            navigateToRegister = {
                navController.navigate(Screen.Register.route) {
                    launchSingleTop = true
                }
            }
        )
        registerScreen(
            navigateToAdditionalDetails = {
                authViewModel.setFirstTime(false)
                navController.popBackStack()
                navController.navigate(Screen.AdditionalDetails.route)
            },
            navigateToSignIn = {
                navController.navigate(Screen.SignIn.route) {
                    launchSingleTop = true
                }
            }
        )

        additionalDetailsScreen(
            navigateToSignIn = {
                authViewModel.setAdditionalDetailsFilled(false)
                navController.popBackStack()
                navController.navigate(Screen.SignIn.route)
            },
            navigateToHome = {
                authViewModel.setAdditionalDetailsFilled(true)
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        )

        pairingPermissionScreen(
            navigateToPairing = {
                navController.popBackStack()
                navController.navigate(Screen.Pairing.route)
            }
        )


        homeScreen(
            navigateToAuthentication = {
                navController.popBackStack()
                navController.navigate(Screen.SignIn.route)
            }
        )
    }
}

fun NavGraphBuilder.loadingScreen() {
    composable(route = Screen.Loading.route) {
        Box(modifier = Modifier.fillMaxSize()) {
            //Leave it empty
        }
    }
}

fun NavGraphBuilder.onboardingScreen(
    onClick: () -> Unit
) {
    composable(route = Screen.OnBoarding.route) {
        OnBoardingScreen(onClick = onClick)
    }
}

fun NavGraphBuilder.signInScreen(
    navigateToHome: () -> Unit,
    navigateToRegister: () -> Unit,
    navigateToAdditionalDetails: () -> Unit,
) {
    composable(route = Screen.SignIn.route) {
        val authViewModel: AuthenticationViewModel = hiltViewModel()
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val loadingState = authViewModel.loadingState.collectAsStateWithLifecycle().value
        val coroutineScope = rememberCoroutineScope()

        val signInState = authViewModel.signInState.collectAsStateWithLifecycle().value
        val signUpState = authViewModel.signupState.collectAsStateWithLifecycle().value

        when {
            signInState.isLoading -> {
                authViewModel.setLoading(true)
            }

            !signInState.signInError.isNullOrEmpty() -> {
                authViewModel.setLoading(false)
                messageBarState.addError(Exception(signInState.signInError))
            }

            signInState.isSignInSuccessful -> {
                authViewModel.setLoading(false)
                messageBarState.addSuccess("Login Successfully")
                LaunchedEffect(key1 = Unit) {
                    delay(700L)
                    if (signInState.isNewUser) {
                        navigateToAdditionalDetails()
                    } else {
                        navigateToHome()
                    }
                }
            }
        }

        when {
            signUpState.isLoading -> {
                authViewModel.setLoading(true)
            }

            !signUpState.signInError.isNullOrEmpty() -> {
                authViewModel.setLoading(false)
                messageBarState.addError(Exception(signUpState.signInError))
            }

            signUpState.isSignInSuccessful -> {
                authViewModel.setLoading(false)
                messageBarState.addSuccess("Login Successfully")
                LaunchedEffect(key1 = Unit) {
                    delay(700L)
                    if (signUpState.isNewUser) {
                        navigateToAdditionalDetails()
                    } else {
                        navigateToHome()
                    }
                }
            }
        }

        SignInScreen(
            oneTapSignInState = oneTapSignInState,
            messageBarState = messageBarState,
            loadingState = loadingState,
            onLoginWithGoogle = {
                authViewModel.setLoading(true)
                oneTapSignInState.open()
            },
            onLoginWithFacebook = {
                authViewModel.setLoading(true)
                //TODO : Login with Facebook
            },
            onLoginWithEmail = { email, password ->
                val errorMessage = SignInUtils.verifySignInDetails(email, password)
                if (errorMessage.isEmpty())
                    authViewModel.signInWithEmail(email, password)
                else
                    messageBarState.addError(Exception(errorMessage))
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                authViewModel.setLoading(false)
            },
            onTokenIdReceived = { token ->
                authViewModel.signInWithGoogle(token)
            },
            navigateToHome = navigateToHome,
            navigateToRegister = navigateToRegister,
            onForgotPassword = { email ->
                var message = SignInUtils.isEmailValid(email)

                if (message.isNotEmpty()) {
                    messageBarState.addError(Exception(message))
                } else {
                    coroutineScope.launch {
                        message = authViewModel.forgotPassword(email).let { success: Boolean ->
                            if (success)
                                "A link has been sent to your email to reset your password"
                            else
                                "An unknown error occurred. Please try again later"
                        }
                        messageBarState.addSuccess(message)
                    }
                }
            }
        )
    }
}

fun NavGraphBuilder.registerScreen(
    navigateToAdditionalDetails: () -> Unit,
    navigateToSignIn: () -> Unit
) {

    composable(route = Screen.Register.route) {
        val authViewModel: AuthenticationViewModel = hiltViewModel()
        val messageBarState = rememberMessageBarState()
        val loadingState = authViewModel.loadingState.collectAsStateWithLifecycle().value
        val context = LocalContext.current
        val signUpState = authViewModel.signupState.collectAsStateWithLifecycle().value


        when {
            signUpState.isLoading -> {
                authViewModel.setLoading(true)
            }

            !signUpState.signInError.isNullOrEmpty() -> {
                authViewModel.setLoading(false)
                messageBarState.addError(Exception(signUpState.signInError))
            }

            signUpState.isSignInSuccessful -> {
                authViewModel.setLoading(false)
                messageBarState.addSuccess("Registered Successfully")
                LaunchedEffect(key1 = Unit) {
                    delay(700L)
                    navigateToAdditionalDetails()
                }
            }
        }

        RegisterScreen(
            messageBarState = messageBarState,
            loadingState = loadingState,
            navigateToSignIn = navigateToSignIn,
            onRegister = { name, email, password ->
                authViewModel.signUpWithEmail(name, email, password)
            },
            showPrivacyPolicy = {
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse("https://www.freeprivacypolicy.com/live/7b1de32c-dbd3-44db-af92-746118c869da")
                }.also {
                    context.startActivity(it)
                }
            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.additionalDetailsScreen(
    navigateToHome: () -> Unit,
    navigateToSignIn: () -> Unit
) {
    composable(route = Screen.AdditionalDetails.route) {
        val pagerState = rememberPagerState()
        val messageBarState = rememberMessageBarState()
        val viewModel: AuthenticationViewModel = hiltViewModel()
        val uid = Firebase.auth.currentUser?.uid
        if (uid == null) {
            navigateToSignIn()
        }
        val state = viewModel.additionalDetailsState.collectAsStateWithLifecycle().value
        var loadingState by rememberSaveable {
            mutableStateOf(false)
        }

        when (state) {
            is AdditionalDetailsResponse.Failed -> {
                loadingState = false
                messageBarState.addError(Exception(state.message))

            }

            AdditionalDetailsResponse.Loading -> {
                loadingState = true
            }

            AdditionalDetailsResponse.Success -> {
                loadingState = false

                messageBarState.addSuccess("Details Filled Successfully")
                LaunchedEffect(key1 = Unit) {
                    delay(600)
                    navigateToHome()
                }
            }

            else -> Unit
        }

        AdditionalDetailsScreen(
            loadingState = loadingState,
            pagerState = pagerState,
            messageBarState = messageBarState,
            onFinish = { gender, age, goal ->
                viewModel.addUserAdditionalDetails(uid!!, gender, age, goal.title)
            }
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
fun NavGraphBuilder.pairingPermissionScreen(
    navigateToPairing: () -> Unit
) {
    composable(route = Screen.PairingPermission.route) {
        val permissionState = if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            rememberMultiplePermissionsState(
                permissions = listOf(
                    android.Manifest.permission.BLUETOOTH_SCAN,
                    android.Manifest.permission.BLUETOOTH_CONNECT,
                    android.Manifest.permission.BLUETOOTH
                )
            )
        } else {
            rememberMultiplePermissionsState(
                permissions = listOf(
                    android.Manifest.permission.BLUETOOTH,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        PairingPermissionScreen(
            permissionState = permissionState,
            onGetPermissionClicked = {
                permissionState.launchMultiplePermissionRequest()

                if (permissionState.allPermissionsGranted) {
                    navigateToPairing()
                }
            }
        )
    }
}



fun NavGraphBuilder.homeScreen(
    navigateToAuthentication: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val authViewModel: AuthenticationViewModel = hiltViewModel()
        var showSignOutDialog by remember { mutableStateOf(false) }
        var showDeleteUserDialog by remember { mutableStateOf(false) }
        val context = LocalContext.current

        val sessionViewModel: SessionViewModel = hiltViewModel()
        val sessionState by
        sessionViewModel.sessionDataState.collectAsStateWithLifecycle()
        val coroutineScope = rememberCoroutineScope()

        var node by remember { mutableStateOf<Node?>(null) }
        val user by authViewModel.currentUser.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = Unit) {
            try {
                val nodes = Wearable.getNodeClient(context).connectedNodes.await()
                node = nodes.firstOrNull()
            } catch (e: Exception) {
                Log.e("Dhyan Nav Graph", e.message, e)
                Toast.makeText(context, "No watch found", Toast.LENGTH_SHORT).show()
            }
        }


        MainScreen(
            user = user,
            sessionState = sessionState,
            node = node,
            onConnectToWatch = {
                if (node != null) {
                    Log.d("User ID", "homeScreen: ${Firebase.auth.currentUser?.uid}")
                    Firebase.auth.currentUser?.uid?.let { userId ->
                        coroutineScope.launch {
                            Wearable.getMessageClient(context)
                                .sendMessage(node!!.id, "/message", userId.toByteArray())
                                .await()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "${node!!.displayName} is Paired",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    try {
                        coroutineScope.launch {
                            val nodes = Wearable.getNodeClient(context).connectedNodes.await()
                            node = nodes.firstOrNull()
                            Log.e("Dhyan Nav Graph", node?.displayName ?: "No watch")

                        }
                    } catch (e: Exception) {
                        Log.e("Dhyan Nav Graph", e.message, e)
                        Toast.makeText(context, "No watch found", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            signOut = {
                showSignOutDialog = true
            },
             deleteUser = {
                 showDeleteUserDialog = true
             }
        )

        DisplayAlertDialog(
            title = "Sign Out",
            message = "Do you really want to sign out?",
            dialogOpened = showSignOutDialog,
            onCloseDialog = { showSignOutDialog = false },
            onConfirmClicked = {
                authViewModel.signOut()
                navigateToAuthentication()
            })
        DisplayAlertDialog(
            title = "Delete Account",
            message = "Do you really want to delete your account?",
            dialogOpened = showDeleteUserDialog,
            onCloseDialog = { showDeleteUserDialog = false },
            onConfirmClicked = {
                authViewModel.deleteUser()
                navigateToAuthentication()
            })
    }
}