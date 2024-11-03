package com.tunjasoft.fullapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tunjasoft.fullapp.R
import com.tunjasoft.fullapp.utils.FirebaseAuthManager
import com.tunjasoft.fullapp.views.NotificationActivity

open class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "default_notification_channel"
        const val CHANNEL_NAME = "Tunja Soft"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: getString(R.string.not_available)
        val body = remoteMessage.notification?.body ?: getString(R.string.not_available)

        // Store the notification
        storeNotificationInSharedPreferences(title, body)

        // Display notification in system tray
        sendNotification(title, body)
    }

    override fun onNewToken(token: String) {
        Log.d("TAG", "Refreshed token: $token")
        val auth = FirebaseAuth.getInstance()
        val authManager = FirebaseAuthManager(null, auth)
        authManager.updateUserDevice { _, _ -> }
    }

    private fun storeNotificationInSharedPreferences(title: String, body: String) {
        val auth = FirebaseAuth.getInstance()

        val sharedPreferences = getSharedPreferences("notifications", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val existingNotifications = sharedPreferences.getString("notifications_list_" + (auth.currentUser?.uid ?: "def"), "") ?: ""
        val newNotification = "$title|$body\n"
        editor.putString("notifications_list_" + (auth.currentUser?.uid ?: "def"), existingNotifications + newNotification)
        editor.apply()
    }

    private fun sendNotification(title: String, body: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android 8.0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create intent to open NotificationActivity when the notification is clicked
        val intent = Intent(this, NotificationActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Use your app's icon
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent) // Open NotificationActivity when clicked
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification in the system's notification drawer
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
