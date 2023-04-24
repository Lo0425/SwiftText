package com.example.swifttext.service

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.swifttext.data.model.Rules
import com.example.swifttext.data.model.WearableNotification
import com.example.swifttext.data.repositories.FireStoreRulesRepository
import com.example.swifttext.utils.Constants.DEBUG
import com.example.swifttext.utils.NotificationUtils
import com.google.common.collect.ComparisonChain.start
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class NotificationService : NotificationListenerService() {
    private lateinit var intent: Intent
    lateinit var bundle: Bundle
    private lateinit var title: String
    private lateinit var wNotification: WearableNotification
    private lateinit var msg: String
    private var replyText: String = ""
    private lateinit var repo: FireStoreRulesRepository

    override fun onCreate() {
        super.onCreate()
        repo = FireStoreRulesRepository(Firebase.firestore.collection("rules"))
        start()

    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.d(DEBUG, "Found a notification")

        wNotification = NotificationUtils.getWearableNotification(sbn) ?: return
        title = wNotification.bundle?.getString("android.title") ?: "Empty"
        Log.d(DEBUG, "Title: $title")

        //terminate when isRunning and checkTitle are false
        if (!isRunning) return
        if (!checkTitle()) return

        checkMsg {
            createIntentBundle()
            wNotificationPendingIntent(sbn)
        }
    }


    private fun checkTitle(): Boolean {
        if (title.contains(
                Regex(
                    "caaron|ching|justin|yan|xiang|vikram|khayrul|601606|joel|quan|Joel",
                    RegexOption.IGNORE_CASE
                )
            )
        ) {
            return true
        }
        return false
    }

    //check if notification body matches rule's keyword then reply according to rule's msg
    private fun checkMsg(callback: () -> Unit) {
        msg = wNotification.bundle?.getString("android.text") ?: "Empty"
        var ruleFound = false

        val rules = getRules()

        for (i in rules) {
            if (msg.contains(Regex(i.textIncludes, RegexOption.IGNORE_CASE))) {
                replyText = i.reply
                ruleFound = true
                val notifName = wNotification.name

                if (replyIfAppIsSelected(i.status, "com.whatsapp", notifName, callback)) break
                if (replyIfAppIsSelected(i.status, "com.facebook.orca", notifName, callback)) break
            }
        }
        if (!ruleFound) return
    }

    //check if user's selected app option matches wNotification.name, if true then fire callback fn
    private fun replyIfAppIsSelected(
        isSelected: Boolean,
        userSelectedApp: String,
        notifyName: String,
        callback: () -> Unit
    ): Boolean {
        if (isSelected && hasAppName(notifyName, userSelectedApp)) {
            callback()
            return true
        }

        return false
    }

    //utilize regex to check if wNotification.name(string) contains user's selected app option
    private fun hasAppName(notifyName: String, appName: String): Boolean {
        return notifyName.contains(Regex(appName, RegexOption.IGNORE_CASE))
    }

    //fetch all rules that matches current user's id from FireStore
    private fun getRules(): MutableList<Rules> {
        val rules: MutableList<Rules> = mutableListOf()

        val job = CoroutineScope(Dispatchers.Default).launch {
            val res = repo.getRules()
            res.let {
                it.forEach { rule ->
                    rules.add(rule)
                }
            }
        }
        runBlocking {
            job.join()
        }
        return rules.filter { rule -> rule.status }.toMutableList()
    }

    //create intent bundle
    private fun createIntentBundle() {
        intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        bundle = Bundle()
        bundle.putCharSequence(wNotification.remoteInputs[0].resultKey, replyText)

        RemoteInput.addResultsToIntent(wNotification.remoteInputs.toTypedArray(), intent, bundle)
    }

    //send reply
    private fun wNotificationPendingIntent(sbn: StatusBarNotification?) {
        try {
            wNotification.pendingIntent?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    isRunning = false
                    cancelNotification(sbn?.key)

                    it.send(this@NotificationService, 0, intent)
                    delay(500)
                    isRunning = true
                }
            }
        } catch (e: Exception) {
            isRunning = true
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        Log.d(DEBUG, "Destroyed")
        super.onDestroy()
    }

    companion object {
        private var isRunning: Boolean = false
        fun start() {
            isRunning = true
        }

        fun stop() {
            isRunning = false
        }
    }

}