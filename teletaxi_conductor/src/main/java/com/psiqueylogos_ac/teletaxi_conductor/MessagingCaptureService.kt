package com.psiqueylogos_ac.teletaxi_conductor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.psiqueylogos_ac.teletaxi_lib.Settings

class MessagingCaptureService : FirebaseMessagingService() {

    private lateinit var settings: Settings

    override fun onCreate() {
        super.onCreate()
        settings = Settings(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendToken(this, token)
        settings.messageToken = token
    }


    override fun onMessageReceived(message: RemoteMessage) {
        createNotification(message)
        super.onMessageReceived(message)


    }

    private fun createNotification(message: RemoteMessage) {
        val notificationId = 1
        val CHANNEL_ID = "Teletaxi"
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //create channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel =
                NotificationChannel(CHANNEL_ID, "TLTC", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "TLTC"
                }

            notificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(com.google.android.material.R.drawable.abc_ic_go_search_api_material)
                .setContentTitle(message.data["title"])
                .setContentText(message.data["body"])
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build()

            NotificationManagerCompat.from(this).notify(notificationId, notification)
        }
    }
}