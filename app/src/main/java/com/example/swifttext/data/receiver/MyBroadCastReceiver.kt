package com.example.swifttext.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("debugging", "Found a broadcast event")

        val message = intent?.getStringExtra("message") ?: ""

        Log.d("debugging", message)

    }
}