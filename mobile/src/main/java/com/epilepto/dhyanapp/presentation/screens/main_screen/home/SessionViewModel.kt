package com.epilepto.dhyanapp.presentation.screens.main_screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epilepto.dhyanapp.data.repository.FirebaseSessionRepository
import com.epilepto.dhyanapp.data.utils.Response
import com.epilepto.dhyanapp.model.session.BPMPoints
import com.epilepto.dhyanapp.model.session.MeditationAnalysis
import com.epilepto.dhyanapp.model.session.PranayamAnalysis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val firebaseSessionRepository: FirebaseSessionRepository
) : ViewModel() {

    private val _sessionDataState = MutableStateFlow(SessionScoreDataState())
    val sessionDataState = _sessionDataState.asStateFlow()

    private val _bpmPoints = MutableStateFlow(BPMDataState())

    /*    val sessionState: Flow<SessionState> = combine(
            _sessionDataState,
            _bpmPoints
        ) { sessionScore, bpmPoints ->
            if (sessionScore.isLoading || bpmPoints.isLoading) {
                SessionState(isLoading = true, error = "")
            } else if (sessionScore.error.isNotEmpty()) {
                SessionState(isLoading = false, error = sessionScore.error)

            } else if (bpmPoints.error.isNotEmpty()) {
                SessionState(isLoading = false, error = bpmPoints.error)
            } else {
                SessionState(
                    sessionScoreData = sessionScore.sessionScoreData,
                    bpmPoints = bpmPoints.bpm,
                    isLoading = false,
                    error = ""
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SessionState(isLoading = true))*/

    init {
//        fetchBPMData()
        fetchSessionScoreData()
    }


    private fun fetchSessionScoreData() {
        viewModelScope.launch {
            firebaseSessionRepository.getSessionData().collectLatest { res ->
                when (res) {
                    is Response.Success -> {
                        val meditationAnalysis =
                            res.data?.mapNotNull { it.meditationAnalysis } ?: emptyList()

                        val pranayamAnalysis =
                            res.data?.mapNotNull { it.pranayamAnalysis } ?: emptyList()

                        _sessionDataState.update {
                            it.copy(
                                meditationAnalysis = meditationAnalysis,
                                pranayamAnalysis = pranayamAnalysis,
                                isLoading = false,
                                error = ""
                            )
                        }
                    }

                    is Response.Loading -> {
                        _sessionDataState.update {
                            it.copy(
                                isLoading = true,
                                error = ""
                            )
                        }
                    }

                    is Response.Error -> {
                        _sessionDataState.update {
                            it.copy(
                                isLoading = false,
                                error = res.message
                            )
                        }
                    }
                }
            }
        }
    }

    /*
        private fun fetchBPMData() {
            viewModelScope.launch {
                firebaseSessionRepository.getBPM().collectLatest { res ->
                    when (res) {
                        is Response.Success -> {
                            _bpmPoints.update {
                                it.copy(
                                    bpm = res.data ?: BPMPoints(),
                                    isLoading = false,
                                    error = ""
                                )
                            }
                        }

                        is Response.Loading -> {
                            _bpmPoints.update {
                                it.copy(
                                    bpm = res.data ?: BPMPoints(),
                                    isLoading = true,
                                    error = ""
                                )
                            }
                        }

                        is Response.Error -> {
                            _bpmPoints.update {
                                it.copy(
                                    bpm = res.data ?: BPMPoints(),
                                    isLoading = false,
                                    error = res.message
                                )
                            }
                        }
                    }
                }
            }
        }

    */

}

data class SessionScoreDataState(
    val meditationAnalysis: List<MeditationAnalysis> = emptyList(),
    val pranayamAnalysis: List<PranayamAnalysis> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)

data class BPMDataState(
    val bpm: BPMPoints = BPMPoints(),
    val isLoading: Boolean = false,
    val error: String = ""
)