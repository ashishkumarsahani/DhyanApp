package com.epilepto.dhyanapp.model.session

data class SessionScoreData(
    val asanaScore:Double,
    val sessionDate:Long,
    val dhyanScore:Double,
    val duration:Double,
    val pranaScore:Double = 0.0
)

data class BPMPoints(
    val bpm: List<Float> = emptyList(),
    val time: List<Float> = emptyList()
)

data class SessionState(
    val sessionScoreData: List<SessionScoreData> = emptyList(),
    val bpmPoints: BPMPoints = BPMPoints(),
    val isLoading:Boolean = false,
    val error:String = ""
)