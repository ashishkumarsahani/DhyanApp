package com.epilepto.dhyanapp.presentation.data.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService

class DataLayerService : WearableListenerService(){
    override fun onMessageReceived(event: MessageEvent) {
        if(event.path =="/message"){
            val data = String(event.data)
            Log.d("UserId", "onMessageReceived: $data")
            SharedPreferencesManager.setUserId(this, data)
        }
        else if(event.path =="/login"){
            val data = String(event.data)
            Log.d("Login synced", "onMessageReceived: $data")
        }
    }
    private fun sendMessageToPhone(path: String, message: ByteArray) {
        val client = Wearable.getMessageClient(this)
        Wearable.getNodeClient(this).connectedNodes.addOnSuccessListener { nodes ->
            val nodeId = nodes.firstOrNull()?.id // Gets the first connected node
            nodeId?.let {
                client.sendMessage(nodeId, path, message).apply {
                    addOnSuccessListener {
                        Log.d("DataLayerService", "Message sent successfully")
                    }
                    addOnFailureListener {
                        Log.e("DataLayerService", "Error sending message", it)
                    }
                }
            }
        }
    }
}

object SharedPreferencesManager {
    private const val PREFS_NAME = "WearablePrefs"
    private const val USER_ID_KEY = "UserId"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setUserId(context: Context, userId: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(USER_ID_KEY, userId)
        editor.apply()
    }

    fun getUserId(context: Context): String? {
        return getSharedPreferences(context).getString(USER_ID_KEY, null)
    }
}


fun showNotification(context: Context, title: String, content: String) {
    val channelId = "your_channel_id"
    val channelName = "Your Channel Name"

    // Create a notification channel for API 26+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "Your channel description"
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Build the notification
    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(androidx.core.R.drawable.notification_bg) // Replace with your own drawable
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    // Show the notification
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("", "Permission hi nahi di")
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(1, notificationBuilder.build()) // 1 is a unique ID for this notification
    }
}
