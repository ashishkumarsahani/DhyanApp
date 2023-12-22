package com.epilepto.dhyanapp.data.remote

import com.epilepto.dhyanapp.utils.Constants
import com.epilepto.dhyanapp.utils.NotificationData
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FirebaseMessagingResponse {
        @Headers(
            "Authorization: Bearer ${Constants.ACCESS_TOKEN}",
            "Content-Type: ${Constants.CONTENT_TYPE}"
        )
        @POST("v1/projects/dhyanapp-10c6e/messages:send")
        suspend fun sendNotification(
            @Body notification: HashMap<String, NotificationData>
        ): Response<ResponseBody>
}