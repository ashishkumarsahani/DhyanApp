package com.epilepto.dhyanapp.navigation

sealed class Screen(val route:String){
    data object OnBoarding: Screen("onboarding_screen")
    data object SignIn: Screen("signin_screen")
    data object Register: Screen("register_screen")
    data object AdditionalDetails: Screen("additional_details_screen")
    data object PairingPermission: Screen("pairing_permission_screen")
    data object Pairing: Screen("pairing_screen")
    data object Loading: Screen("loading_screen")

    data object Home: Screen("home_screen")
}
