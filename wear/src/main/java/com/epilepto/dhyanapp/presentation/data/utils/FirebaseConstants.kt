package com.epilepto.dhyanapp.presentation.data.utils


import com.epilepto.dhyanapp.presentation.domain.models.sessionData.MeditationData
import com.epilepto.dhyanapp.presentation.domain.models.sessionData.PranayamData
import com.epilepto.dhyanapp.presentation.domain.models.sessionData.Score

object FirebaseConstants {
    const val SESSION_REF = "Sessions"
}

data class MeditationAnalysis(
    val meditationData: MeditationData?= null,
    val score: Score? = null
)
data class PranayamAnalysis(
    val pranayamData: PranayamData?= null,
    val score: Score? = null
)

data class UserSessionData(
    val meditationAnalysis: MeditationAnalysis? = null,
    val pranayamAnalysis: PranayamAnalysis?= null
)