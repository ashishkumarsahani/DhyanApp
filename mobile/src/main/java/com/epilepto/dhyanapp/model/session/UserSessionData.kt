package com.epilepto.dhyanapp.model.session


data class UserSessionData(
    val meditationAnalysis: MeditationAnalysis? = null,
    val pranayamAnalysis: PranayamAnalysis?= null
)

data class MeditationAnalysis(
    val meditationData: MeditationData?= null,
    val score: Score? = null
)
data class PranayamAnalysis(
    val pranayamData: PranayamData?= null,
    val score: Score? = null
)