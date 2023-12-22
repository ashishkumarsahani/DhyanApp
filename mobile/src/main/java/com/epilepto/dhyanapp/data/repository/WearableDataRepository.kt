package com.epilepto.dhyanapp.data.repository

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable

class WearableDataRepository(
    context:Context
) {
    private val messageClient: MessageClient = Wearable.getMessageClient(context)

    fun addListener(listener:MessageClient.OnMessageReceivedListener){
        messageClient.addListener(listener)
    }
    fun removeListener(listener:MessageClient.OnMessageReceivedListener){
        messageClient.removeListener(listener)
    }

    private fun pickBestNodeId(nodes: Set<Node>): String? {
        // Find a nearby node or pick one arbitrarily.
        return nodes.firstOrNull { it.isNearby }?.id ?: nodes.firstOrNull()?.id
    }

//     fun sendMessage(nodeId:String,message:String): Task<Int> {
//        val payload = message.toByteArray()
//         return messageClient.sendMessage(nodeId, MESSAGE_PATH,payload)
//    }
    fun sendMessage(nodeId:String,message:String,path:String): Task<Int> {
        val payload = message.toByteArray()
        return messageClient.sendMessage(nodeId, path,payload)
    }

    companion object{
        const val MESSAGE_KEY = "com.epilepto.key.message"
        const val MESSAGE_PATH = "/message"
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


//fun showNotification(context: Context, title: String, lines: List<String>) {
//    val channelId = "your_channel_id"
//    val channelName = "Your Channel Name"
//    // ... [rest of your existing code for channel setup]
//
//    // Using InboxStyle to show three columns of data
//    val inboxStyle = NotificationCompat.InboxStyle().setBigContentTitle(title)
//    lines.forEach { line ->
//        inboxStyle.addLine(line)
//    }
//
//    // Intent to open the phone app
//    val intent = Intent(Intent.ACTION_DIAL)
//    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
//
//    // Build the notification
//    val notificationBuilder = NotificationCompat.Builder(context, channelId)
//        .setSmallIcon(androidx.core.R.drawable.notification_bg) // Replace with your own drawable
//        .setContentTitle(title)
//        .setContentText(lines.joinToString(" ")) // Show a summary text
//        .setStyle(inboxStyle)
//        .setContentIntent(pendingIntent)
//        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//    // Show the notification
//    with(NotificationManagerCompat.from(context)) {
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.d("", "Permission hi nahi di")
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        notify(1, notificationBuilder.build()) // 1 is a unique ID for this notification
//    }
//}
