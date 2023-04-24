package com.example.swifttext.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.service.notification.StatusBarNotification
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.example.swifttext.R
import com.example.swifttext.data.model.WearableNotification
import com.example.swifttext.ui.presentation.MainActivity
import com.example.swifttext.utils.Constants.NOTIFICATION_ID
import com.example.swifttext.utils.Constants.NOTIFICATION_NAME

object NotificationUtils {
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_ID,
                NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun createNotification(context: Context, title: String, msg: String) {
        val notification = getNotificationBuilder(context, title, msg)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(0, notification)
    }


    private fun getNotificationBuilder(context: Context, title: String, msg: String) =
        NotificationCompat.Builder(context, NOTIFICATION_ID)
            .setContentTitle(title)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.logo)


    fun createNotificationWithPendingIntent(context: Context, title: String, msg: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val remoteInput = RemoteInput.Builder(Constants.REPLY)
            .setLabel("Reply")
            .build()


        val replyAction = NotificationCompat.Action.Builder(
            R.drawable.send,
            "Reply",
            pendingIntent
        ).addRemoteInput(remoteInput)
            .setAllowGeneratedReplies(true)
            .build()


        val notification = getNotificationBuilder(context, title, msg)
            .addAction(replyAction)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        NotificationManagerCompat.from(context).notify(0, notification)
    }

    fun getWearableNotification(sbn: StatusBarNotification?): WearableNotification? {
        return sbn?.notification?.let { notification ->
//            val wearableExt = NotificationCompat.WearableExtender(notification)
            val actions = notification.actions ?: return null
            val remoteInputs = ArrayList<android.app.RemoteInput>(actions.size)
            var pendingIntent: PendingIntent? = null

            for(action in actions){
                action?.remoteInputs?.let{ remoteInps ->
                    remoteInps.forEach {
                        remoteInputs.add(it as android.app.RemoteInput)
                        pendingIntent = action.actionIntent
                    }

                }
            }

            WearableNotification(
                sbn.tag,
                sbn.packageName,
                pendingIntent,
                remoteInputs,
                notification.extras
            )
        }
    }
}