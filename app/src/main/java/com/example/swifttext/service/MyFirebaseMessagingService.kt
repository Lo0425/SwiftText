package com.example.swifttext.service

import android.content.Intent
import android.util.Log
import com.example.swifttext.data.model.Token
import com.example.swifttext.utils.Constants.DEBUG
import com.example.swifttext.utils.NotificationUtils
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(DEBUG, "Token: $token")

        Firebase.firestore.collection("tokens")
            .add(Token(token)).addOnSuccessListener {
                Log.d(DEBUG, "Token saved successfully")
            }
            .addOnFailureListener {
                Log.d(DEBUG, "Failed to save token")
                it.printStackTrace()
            }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.data["title"].toString()
        val body = message.data["body"].toString()

        if (title == "Broadcast") {
            val intent = Intent()
            intent.action = "com.replyBot.MyBroadcast"
            intent.putExtra("message", body)
            sendBroadcast(intent)
        }

        NotificationUtils.createNotification(
            this, title, body
        )
    }
}