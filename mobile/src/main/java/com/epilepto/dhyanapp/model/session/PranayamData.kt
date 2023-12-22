package com.epilepto.dhyanapp.model.session

data class PranayamData(
    var bpm: List<Float>? = null,
    var ms_bpm: List<Float>?= null,
    var spo2: List<Float>?= null,
    var ax: List<Float>?= null,
    var ay: List<Float>?= null,
    var az: List<Float>?= null,
    var gx: List<Float>?= null,
    var gy: List<Float>?= null,
    var gz: List<Float>?= null,
    var arrayBreathWithBPM: List<String>?= null,
    var sessions: Int?= null,
    var IEratio: String?= null,
)