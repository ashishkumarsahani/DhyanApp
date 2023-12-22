package com.epilepto.dhyanapp.presentation.domain.models.sessionData


data class Score(
    val type: String? = "",
    val calender: Long? = 0,
    var dhyan: Float? = 0f,
    var aasana: Float? = 0f,
    var prana: Float? = 0f,
    var duration: Float? = 0f,
)



