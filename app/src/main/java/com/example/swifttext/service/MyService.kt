package com.example.swifttext.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.swifttext.R
import com.example.swifttext.ui.presentation.MainActivity
import com.example.swifttext.utils.Constants

class MyService : Service() {

    override fun onCreate() {
        super.onCreate()

        Toast.makeText(
            this,
            "Service started",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


//        val data = intent?.getStringExtra("EXTRA_DATA") ?: "Empty"
//
//        CoroutineScope(Dispatchers.Default).launch {
//            while(true){
//                delay(2000)
//                Log.d(Constants.DEBUG,"Running $data")
//            }
//        }


//        handleSensorData()

        startForegroundService()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopNotificationListenerService()
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show()
    }

    private fun startForegroundService() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        val notification = NotificationCompat.Builder(this, Constants.NOTIFICATION_ID)
            .setContentTitle("Auto Reply BOT")
            .setContentText("Notification listener is running")
            .setSmallIcon(R.drawable.logo)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .build()

        startForeground(1, notification)
        startNotificationListenerService()
    }

    private fun startNotificationListenerService() {
        Intent(this, NotificationListenerService::class.java).also {
            it.action = "android.service.notification.NotificationListenerService"
            startService(it)
        }
    }

    private fun stopNotificationListenerService() {
        Intent(this, NotificationListenerService::class.java).also {
            stopService(it)
        }
    }

//    private fun handleSensorData() {
//        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)
//            sensorManager.registerListener(
//                this,
//                SensorManager.SENSOR_DELAY_NORMAL,
//                SensorManager.SENSOR_DELAY_UI
//            )
//
//
//        val rotation = FloatArray(0)
//        SensorManager.getRotationMatrix(rotation, null, )
//        Log.d(Constants.DEBUG, sensor.name)
//    }
}