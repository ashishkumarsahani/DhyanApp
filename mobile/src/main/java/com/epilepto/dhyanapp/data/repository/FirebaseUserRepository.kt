package com.epilepto.dhyanapp.data.repository

import android.util.Log
import coil.network.HttpException
import com.epilepto.dhyanapp.data.remote.FirebaseMessagingResponse
import com.epilepto.dhyanapp.model.user.User
import com.epilepto.dhyanapp.model.user.toUser
import com.epilepto.dhyanapp.presentation.screens.auth.signin.AdditionalDetailsResponse
import com.epilepto.dhyanapp.presentation.screens.auth.signin.SignInResult
import com.epilepto.dhyanapp.utils.NotificationData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject


class FirebaseRepository @Inject constructor(
    private val fcmService: FirebaseMessagingResponse
) {

    private val firestore = Firebase.firestore
    private val userRef = firestore.collection("users")
    private val dataRef = firestore.collection("data")
    private val auth = Firebase.auth


    suspend fun addUser(user: User): Flow<SignInResult> = flow {
        emit(SignInResult.Loading)
        try {
            userRef.document(user.userId).set(user).await()
            emit(SignInResult.Success)
        } catch (e: Exception) {
            emit(SignInResult.Failed(e.message ?: "oops, an unknown error occurred"))
            e.printStackTrace()
            println("Add user Error: " + e.message)
        }
    }

    suspend fun fillAdditionalDetails(
        userId: String,
        gender: String,
        age: Int,
        goal: String
    ): Flow<AdditionalDetailsResponse> = flow {
        emit(AdditionalDetailsResponse.Loading)
        try {
            val details = mapOf(
                "gender" to gender,
                "age" to age,
                "goal" to goal
            )
            userRef.document(userId).update(details).await()
            emit(AdditionalDetailsResponse.Success)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emit(AdditionalDetailsResponse.Failed(e.message ?: "oops, an unknown error occurred"))
            e.printStackTrace()
            println("Additional Details Error: " + e.message)
        }
    }

    suspend fun getUser(userId: String): User? {
        return userRef.document(userId).get().await().data?.toUser()
    }

    private suspend fun getAllUsers(): List<User?> {
        return userRef.get().await().documents.map { doc ->
            doc.toObject<User>()
        }
    }

    suspend fun registerWithEmail(
        email: String,
        password: String
    ): Flow<SignInResult> = flow {
        emit(SignInResult.Loading)
        try {
            val userList = getAllUsers()
            if (isEmailAlreadyExists(userList, email)) {
                emit(SignInResult.Failed("Email is already in use."))
                return@flow
            }

            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            if (user != null) {
                emit(SignInResult.Success)
            }
        } catch (e: HttpException) {
            emit(SignInResult.Failed(message = "Oops, something went wrong"))
        } catch (e: IOException) {
            emit(SignInResult.Failed(message = "Couldn't reach server check your internet connection"))
        } catch (e: Exception) {
            emit(SignInResult.Failed(message = e.message ?: "oops, an unknown error occurred"))
        }
    }

    suspend fun signInWithEmail(email: String, password: String): Flow<SignInResult> = flow {
        emit(SignInResult.Loading)
        try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            if (user != null) emit(SignInResult.Success)
            else
                emit(SignInResult.Failed("No user exist with this email. Please Sign up to make a new account"))

        } catch (e: HttpException) {
            emit(SignInResult.Failed(message = "Oops, something went wrong"))
        } catch (e: IOException) {
            emit(SignInResult.Failed(message = "Couldn't reach server check your internet connection"))
        } catch (e: Exception) {
            emit(SignInResult.Failed(message = e.message ?: "oops, an unknown error occurred"))
        }
    }

    private fun isEmailAlreadyExists(userList: List<User?>, email: String): Boolean {
        //If email already exists return true
        return userList.count { it?.userEmail == email } != 0
    }

    //Use in android
    suspend fun getTokenFromFirebase(userId: String): String? {
        return try {
            val tokenData = dataRef.document(userId).get().await().data ?: return ""
            return tokenData["deviceToken"] as String?
        } catch (e: Exception) {
            Log.e(TAG, "An Error Occurred", e)
            null
        }
    }

    //Use in watch
    suspend fun setTokenToFirebase(userId: String, token: String?): String? {
        val tokenData = dataRef.document(userId).get().await().data
        try {
            if (token != null) {
                if (tokenData != null) {
                    val settingToken = mapOf("deviceToken" to token)
                    dataRef.document(userId).update(settingToken).await()
                } else {
                    dataRef.document(userId).set(SyncData(deviceToken = token)).await()
                }
                return token
            } else {
                val oldToken = FirebaseMessaging.getInstance().token.await()
                if (tokenData != null) {
                    val settingToken = mapOf("deviceToken" to oldToken)
                    dataRef.document(userId).update(settingToken).await()
                } else {
                    dataRef.document(userId).set(SyncData(deviceToken = oldToken)).await()
                }
                return oldToken
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Add user Error: " + e.message)
            return null
        }
    }

//    suspend fun isUsernameAlreadyExists(userList: List<User?>?=null, username: String): Boolean {
//        //If username already exists return true
//        //val list = userList ?:
//        return getAllUsers().count { it?.username == username } != 0
//    }

    suspend fun sendNotification(notificationData: NotificationData):String?{
        val response = fcmService.sendNotification(
            hashMapOf("message" to notificationData)
        )
        return if(response.isSuccessful){
            val jsonData = Gson().toJson(response.body()?.string())
            Log.e(TAG, "Success Json: $jsonData")

            jsonData
        }else{
            val jsonData = Gson().toJson(response.errorBody()?.string())
            Log.e(TAG, "Error Json: $jsonData")
            response.errorBody()?.toString()
        }
    }
    companion object {
        private const val TAG = "Firebase Repository"
    }

}

data class SyncData(
    val deviceToken: String? = null,
    val isWatchSynced: Boolean = false
)