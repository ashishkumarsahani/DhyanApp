package com.epilepto.dhyanapp.presentation.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epilepto.dhyanapp.data.repository.FirebaseRepository
import com.epilepto.dhyanapp.model.user.User
import com.epilepto.dhyanapp.navigation.Screen
import com.epilepto.dhyanapp.presentation.screens.auth.signin.AdditionalDetailsResponse
import com.epilepto.dhyanapp.presentation.screens.auth.signin.AuthState
import com.epilepto.dhyanapp.presentation.screens.auth.signin.SignInResponse
import com.epilepto.dhyanapp.presentation.screens.auth.signin.SignInResult
import com.epilepto.dhyanapp.utils.DataStoreUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val dataStoreUtils: DataStoreUtils
) : ViewModel() {

    private val auth = Firebase.auth

    val loadingState = MutableStateFlow(false)

    private val _signInState = MutableStateFlow(AuthState())
    val signInState = this._signInState.asStateFlow()

    private val _signupState = MutableStateFlow(AuthState())
    val signupState = _signupState.asStateFlow()

    val currentUser: MutableStateFlow<User?> = MutableStateFlow(null)

    private val _additionalDetailsState =
        MutableStateFlow<AdditionalDetailsResponse>(AdditionalDetailsResponse.Idle)
    val additionalDetailsState = _additionalDetailsState.asStateFlow()

    val startDestination = MutableStateFlow(Screen.Loading.route)
    private suspend fun getStartDestination() {
        dataStoreUtils.getIsFirstTime()
            .zip(dataStoreUtils.isAdditionalDetailsFilled())
            { isFirstTime, isDetailsFilled ->
                startDestination.value = Firebase.auth.currentUser.let { firebaseUser ->
                    if (firebaseUser == null) {
                        if (isFirstTime) Screen.OnBoarding.route
                        else Screen.SignIn.route
                    } else {
                        //Change to pairing permission
                        if (isDetailsFilled) Screen.Home.route
                        else Screen.AdditionalDetails.route
                    }
                }
            }.stateIn(viewModelScope)
    }


    init {
        viewModelScope.launch {
            getStartDestination()

            val userId = auth.currentUser?.uid
            if (userId != null)
                currentUser.value = firebaseRepository.getUser(userId)
        }
    }

    fun setLoading(isLoading: Boolean) {
        loadingState.update { isLoading }
    }

    private suspend fun signInWithGoogleUsingToken(googleIdToken: String): SignInResponse {

        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        try {
            val firebaseUser = auth.signInWithCredential(googleCredentials).await().user
                ?: return SignInResponse(
                    data = null,
                    error = "An unknown error occurred while signing in with google. " +
                            "Please try again later"
                )

            if (firebaseUser.email.isNullOrEmpty()) {
                return SignInResponse(
                    data = null,
                    error = "No email is linked with this email. Please try a different account"
                )
            }

            var user = firebaseRepository.getUser(firebaseUser.uid)
            if (user == null) {
                user = firebaseUser.run {
                    User(
                        userId = uid,
                        username = displayName?:"",
                        userEmail = email?:""
                    )
                }

                return SignInResponse(
                    data = user,
                    error = null,
                    isNewUser = true
                )
            }

            return SignInResponse(
                data = user,
                error = null,
                isNewUser = user.gender.isEmpty() || user.age == 0
            )

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is java.util.concurrent.CancellationException) throw e
            return SignInResponse(
                data = null,
                error = e.message
            )
        }

    }


    fun signInWithGoogle(token: String) {
        viewModelScope.launch {
            _signInState.update { it.copy(isLoading = true) }
            val result = signInWithGoogleUsingToken(token)
            when {
                !result.error.isNullOrEmpty() -> _signInState.update {
                    it.copy(
                        isSignInSuccessful = false,
                        signInError = result.error,
                        isLoading = false
                    )
                }

                result.data != null -> {
                    if (result.isNewUser) {
                        addUser(result.data)

                    } else {
                        _signInState.update {
                            it.copy(
                                isSignInSuccessful = true,
                                isLoading = false,
                                signInError = null,
                                isNewUser = false
                            )
                        }
                    }
                }
            }
        }
    }


    fun signInWithEmail(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.signInWithEmail(email, pass).collectLatest { res ->
                when (res) {
                    is SignInResult.Failed -> _signInState.update {
                        it.copy(
                            isSignInSuccessful = false,
                            signInError = res.message,
                            isLoading = false
                        )
                    }

                    SignInResult.Loading -> _signInState.update {
                        it.copy(
                            isSignInSuccessful = false,
                            signInError = "",
                            isLoading = true
                        )
                    }

                    SignInResult.Success -> {
                        currentUser.value = auth.currentUser?.uid?.let { id ->
                            val user = firebaseRepository.getUser(id) ?: return@let null
                            _signInState.update {
                                it.copy(
                                    isSignInSuccessful = true,
                                    signInError = "",
                                    isLoading = false,
                                    isNewUser = user.gender.isEmpty() || user.age == 0
                                )
                            }
                            user
                        }
                    }
                }
            }
        }
    }

    fun signUpWithEmail(
        username: String,
        email: String,
        pass: String
    ) {
        viewModelScope.launch {
            firebaseRepository.registerWithEmail(
                email = email, password = pass
            ).collectLatest { res ->
                    when (res) {
                        is SignInResult.Failed -> _signupState.update {
                            it.copy(
                                isLoading = false,
                                signInError = res.message
                            )
                        }

                        SignInResult.Loading -> _signupState.update {
                            it.copy(
                                isLoading = true,
                                signInError = ""
                            )
                        }

                        SignInResult.Success -> {
                            val uid = auth.currentUser?.uid!!

                            val user = User(
                                userId = uid,
                                username = username,
                                userEmail = email
                            )
                            currentUser.value = user
                            addUser(user)
                        }
                    }
                }
        }
    }

    private fun addUser(user: User) {
        viewModelScope.launch {
            firebaseRepository.addUser(user).collectLatest { res ->
                when (res) {
                    is SignInResult.Failed -> _signupState.update {
                        it.copy(
                            isLoading = false,
                            signInError = res.message,
                            isSignInSuccessful = false
                        )
                    }

                    SignInResult.Loading -> _signupState.update {
                        it.copy(
                            isLoading = true,
                            signInError = ""
                        )
                    }

                    SignInResult.Success -> _signupState.update {
                        it.copy(
                            isSignInSuccessful = true,
                            isLoading = false,
                            signInError = "",
                            isNewUser = true
                        )
                    }
                }
            }
        }
    }

    fun addUserAdditionalDetails(uid: String, gender: String, age: Int, goal: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.fillAdditionalDetails(uid, gender, age, goal).collectLatest { res ->
                _additionalDetailsState.update { res }
            }
        }
    }

    suspend fun forgotPassword(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            false
        }
    }

    fun signOut() {
        auth.signOut()
        currentUser.value = null
        this._signInState.update { AuthState() }
        _signupState.update { AuthState() }
    }
    fun deleteUser(
        onSuccess:()->Unit,
        onError:(Exception)->Unit
    ) {
        viewModelScope.launch {
            try {
                val userRef = Firebase.firestore.collection("users")
                auth.currentUser?.let { user->
                    user.delete().await()
                    userRef.document(user.uid).delete()
                    onSuccess()
                }
            }
            catch (e:Exception){
                Log.e("DeleteUser", "Failed to delete user: ${e.message}")

                onError(e)
            }
        }
    }

    fun setFirstTime(isFirstTime: Boolean) {
        viewModelScope.launch {
            dataStoreUtils.saveUsersFirstTime(isFirstTime)
        }
    }

    fun setAdditionalDetailsFilled(filled: Boolean) {
        viewModelScope.launch {
            dataStoreUtils.setAdditionalDetailsFilled(filled)
        }
    }
}