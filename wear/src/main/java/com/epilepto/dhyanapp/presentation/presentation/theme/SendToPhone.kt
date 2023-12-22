package com.epilepto.dhyanapp.presentation.presentation.theme

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable

class WearMessageSender(private val context: Context) {

    fun sendMessage(path: String, message: ByteArray) {
        val messageClient: MessageClient = Wearable.getMessageClient(context)

        // Get the connected nodes (devices)
        val task = Wearable.getNodeClient(context).connectedNodes
        task.addOnSuccessListener { nodes ->
            for (node in nodes) {
                messageClient.sendMessage(node.id, path, message).apply {
                    addOnSuccessListener {
                        Log.d("WearMessageSender", "Message sent successfully to ${node.displayName}")
                    }
                    addOnFailureListener {
                        Log.e("WearMessageSender", "Failed to send message", it)
                    }
                }
            }
        }.addOnFailureListener {
            Log.e("WearMessageSender", "Failed to get connected nodes", it)
        }
    }
}

