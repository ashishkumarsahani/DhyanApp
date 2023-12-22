package com.epilepto.dhyanapp.presentation.screens.main_screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epilepto.dhyanapp.data.repository.FirebaseRepository
import com.epilepto.dhyanapp.utils.NotificationData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _watchToken = MutableStateFlow<String?>(null)
    val watchToken = _watchToken.asStateFlow()

    private val _notificationResponse = MutableStateFlow<String?>(null)
    val notificationResponse = _watchToken.asStateFlow()


    fun sendNotification(notificationData: NotificationData){
        viewModelScope.launch {
            val res = firebaseRepository.sendNotification(notificationData)
            _notificationResponse.update { res }
        }
    }
    fun getTokenFromFirebase() {
        viewModelScope.launch {
            val userId = Firebase.auth.currentUser?.uid ?: return@launch
            val token = firebaseRepository.getTokenFromFirebase(userId)
            _watchToken.update { token }
        }

    }

    fun setTokenToFirebase(
        token: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val userId = Firebase.auth.currentUser?.uid ?: return@launch
            val data = firebaseRepository.setTokenToFirebase(userId, token)
            if (data == null) {
                onError("An unknown error occurred !")
            } else {
                onSuccess()
            }
        }
    }
}