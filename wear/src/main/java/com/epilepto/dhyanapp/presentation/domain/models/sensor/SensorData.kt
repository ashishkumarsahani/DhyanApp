package com.epilepto.dhyanapp.presentation.domain.models.sensor

data class SensorData(
    val duration: Float,
    val dhyanScore: Float,
    val asanaScore: Float,
    val pranaScore: Float,
    val arrayBpm: ArrayList<Float>,
    val arrayMsBpm: ArrayList<Int>,
    val arraySPO2: ArrayList<Float>,
    val arrayMsSPO2: ArrayList<Int>,
    val arrayAx: ArrayList<Float>,
    val arrayAy: ArrayList<Float>,
    val arrayAz: ArrayList<Float>,
    val arrayGx: ArrayList<Float>,
    val arrayGy: ArrayList<Float>,
    val arrayGz: ArrayList<Float>,
    val arrayInhaleExhale: ArrayList<String>,
    val breaths: Int,
    val IEratio: String,
    val pranayamSession: Int
)
