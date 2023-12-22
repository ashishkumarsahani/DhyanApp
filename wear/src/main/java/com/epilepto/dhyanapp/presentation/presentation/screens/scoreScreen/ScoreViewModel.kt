package com.epilepto.dhyanapp.presentation.presentation.screens.scoreScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epilepto.dhyanapp.presentation.di.DhyanApp
import com.epilepto.dhyanapp.presentation.domain.models.sensor.SensorData
import com.epilepto.dhyanapp.presentation.domain.models.sessionData.SessionDetails
import com.epilepto.dhyanapp.presentation.domain.repository.FirebaseSessionRepository
import com.epilepto.dhyanapp.presentation.navigation.Routes
import com.epilepto.dhyanapp.presentation.presentation.theme.UserDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScoreViewModel() : ViewModel() {
    private val firebaseSessionRepository: FirebaseSessionRepository
        get() = DhyanApp.appModule.firebaseSessionRepository

    private val _state = MutableStateFlow(SessionScoreState())
    val state = _state.asStateFlow()
    val scoreView = SharedScoreViewModel ()
    fun saveAnalysisToFirebase(
        userId: String,
        sharedScoreViewModel: SharedScoreViewModel
    ) {
        val duration = sharedScoreViewModel.sensorData.value?.duration?:0f
        val dhyanScore = sharedScoreViewModel.sensorData.value?.dhyanScore?:0f
        val asanaScore = sharedScoreViewModel.sensorData.value?.asanaScore?:0f
        val pranaScore = sharedScoreViewModel.sensorData.value?.pranaScore?:0f
        val arrayBpm = sharedScoreViewModel.sensorData.value?.arrayBpm
        val arrayMsBpm = sharedScoreViewModel.sensorData.value?.arrayMsBpm?: emptyList<Int>()
        val arraySPO2 = sharedScoreViewModel.sensorData.value?.arraySPO2
        val arrayMsSPO2 = sharedScoreViewModel.sensorData.value?.arrayMsSPO2?: emptyList<Int>()
        val arrayAx = sharedScoreViewModel.sensorData.value?.arrayAx
        val arrayAy = sharedScoreViewModel.sensorData.value?.arrayAy
        val arrayAz = sharedScoreViewModel.sensorData.value?.arrayAz
        val arrayGx = sharedScoreViewModel.sensorData.value?.arrayGx
        val arrayGy = sharedScoreViewModel.sensorData.value?.arrayGy
        val arrayGz = sharedScoreViewModel.sensorData.value?.arrayGz
        val pranayamSession = sharedScoreViewModel.sensorData.value?.pranayamSession
        val breath_withBPM = sharedScoreViewModel.sensorData.value?.arrayInhaleExhale
        val IEratio = sharedScoreViewModel.sensorData.value?.IEratio

        if (sharedScoreViewModel.sensorData.value?.pranayamSession == -1) {

            Log.d("", "Please work - ${Routes.MeditationSessionScore.dhyanScore}")
                addMeditationAnalysisToFirebase(
                    dhyanScore,
                    asanaScore,
                    pranaScore,
                    duration,
                    arrayBpm,
                    arrayMsBpm,
                    arraySPO2,
                    arrayMsSPO2,
                    arrayAx,
                    arrayAy,
                    arrayAz,
                    arrayGx,
                    arrayGy,
                    arrayGz,
                    userId
                )

        }
        else {
            addPranayamAnalysisToFirebase(
                    dhyanScore,
                    asanaScore,
                    pranaScore,
                    duration,
                    arrayBpm,
                    arrayMsBpm,
                    arraySPO2,
                    arrayMsSPO2,
                    arrayAx,
                    arrayAy,
                    arrayAz,
                    breath_withBPM,
                    arrayGx,
                    arrayGy,
                    arrayGz,
                    pranayamSession,
                    IEratio,
                    userId
                )
            }
    }


    fun addMeditationAnalysisToFirebase(
        dhyan: Float,
        aasana: Float,
        prana: Float,
        duration: Float,
        bpm: List<Float>?,
        ms_bpm: List<Int>,
        spo2: List<Float>?,
        ms_spo2: List<Int>,
        ax: List<Float>?,
        ay: List<Float>?,
        az: List<Float>?,
        gx: List<Float>?,
        gy: List<Float>?,
        gz: List<Float>?,
        userId: String?
    ) {
        Log.d("", "Saving Meditation Data to FB - $dhyan")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val res = firebaseSessionRepository.addMeditationAnalysisToFirebase(
                dhyan, aasana, prana, duration, bpm, ms_bpm, spo2, ms_spo2 ,ax, ay, az,
                gx, gy, gz, userId
            )

            if (res.isFailure) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = res.exceptionOrNull()?.message ?: "Oops! Something went wrong."
                    )
                }
            }

            if (res.isSuccess) {
                _state.update {
                    it.copy(
                        isSuccessful = true,
                        isLoading = false,
                        error = ""
                    )
                }
            }
        }
    }

    fun addPranayamAnalysisToFirebase(
        dhyan: Float,
        aasana: Float,
        prana: Float,
        duration: Float,
        bpm: List<Float>?,
        ms_bpm: List<Int>,
        spo2: List<Float>?,
        ms_spo2: List<Int>,
        ax: List<Float>?,
        ay: List<Float>?,
        az: List<Float>?,
        arrayBreathWithBPM: List<String>?,
        gx: List<Float>?,
        gy: List<Float>?,
        gz: List<Float>?,
        pranayamSession: Int?,
        IEratio: String?,
        userId: String?
    ) {
        Log.d("", "Saving Pranayam Data to FB - ${pranayamSession}")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val res = firebaseSessionRepository.addPranayamAnalysisToFirebase(
                dhyan, aasana, prana, duration, bpm, ms_bpm, spo2, ms_spo2, ax,
                ay, az, arrayBreathWithBPM, gx, gy, gz, pranayamSession, IEratio,
                userId
            )

            if (res.isFailure) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = res.exceptionOrNull()?.message ?: "Oops! Something went wrong."
                    )
                }
            }

            if (res.isSuccess) {
                _state.update {
                    it.copy(
                        isSuccessful = true,
                        isLoading = false,
                        error = ""
                    )
                }
            }

        }
    }

}


data class SessionScoreState(
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val error: String = ""
)

class SharedScoreViewModel : ViewModel() {
    private val _sensorData = MutableLiveData<SensorData>()
    val sensorData: LiveData<SensorData> = _sensorData

    fun updateSensorData(newData: SensorData) {
        _sensorData.value = newData
    }
}

class SharedUserInfoViewModel : ViewModel() {
    private val _userInfo = MutableLiveData<UserDetails>()
    val userInfo: LiveData<UserDetails> = _userInfo

    fun updateUserInfo(newData: UserDetails) {
        _userInfo.value = newData
    }
}
class SharedSessionDetailViewModel : ViewModel() {
    private val _sessionDetail = MutableLiveData<SessionDetails>()
    val sessionDetail: LiveData<SessionDetails> = _sessionDetail
    fun updateSessionDetail(newData: SessionDetails) {
        Log.d("", "updateSessionDetail View Model: $newData")
        _sessionDetail.value = newData
    }
}