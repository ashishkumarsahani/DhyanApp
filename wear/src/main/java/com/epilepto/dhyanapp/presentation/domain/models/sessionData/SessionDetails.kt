package com.epilepto.dhyanapp.presentation.domain.models.sessionData

data class SessionDetails(
    val type: String? = "",
    val dhyan: Float? = 0f,
    val asana: Float? = 0f,
    val prana: Float? = 0f,
    val duration: Float? = 0f,
    val time: String = "",
    val date: String = ""
)