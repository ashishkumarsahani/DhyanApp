package com.epilepto.dhyanapp.presentation.screens.auth.signin

import com.epilepto.dhyanapp.model.user.User


data class SignInResponse(
    val data: User? = null,
    val error: String? = null,
    val isNewUser:Boolean = true
)

data class AuthState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
    val isLoading: Boolean = false,
    val isNewUser: Boolean = true
)

sealed interface SignInResult{
    data object Loading: SignInResult
    data object Success: SignInResult
    data class Failed(val message:String): SignInResult
}

sealed interface AdditionalDetailsResponse{
    data object Loading: AdditionalDetailsResponse
    data object Success: AdditionalDetailsResponse
    data class Failed(val message:String): AdditionalDetailsResponse

    data object Idle: AdditionalDetailsResponse
}
