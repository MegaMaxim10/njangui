package com.tunjasoft.fullapp.views

import ProtectedBaseDrawerActivity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.tunjasoft.fullapp.R
import com.tunjasoft.fullapp.adapters.NotificationAdapter
import com.tunjasoft.fullapp.models.NotificationItem
import com.tunjasoft.fullapp.databinding.ActivityNotificationBinding
import com.tunjasoft.fullapp.utils.ProgressDialogHelper

class NotificationActivity : ProtectedBaseDrawerActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var adapter: NotificationAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var progressDialogHelper: ProgressDialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_notification)
        binding = ActivityNotificationBinding.bind(findViewById(R.id.content_frame))
        super.drawerBinding.toolbar.title = getString(R.string.notification_title)

        progressDialogHelper = ProgressDialogHelper(this)

        // Initialize RecyclerView
        adapter = NotificationAdapter()
        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notificationsRecyclerView.adapter = adapter

        // Load existing notifications from SharedPreferences
        sharedPreferences = getSharedPreferences("notifications", Context.MODE_PRIVATE)

        // Set toolbar title
        super.drawerBinding.toolbar.title = getString(R.string.notification_title)
        loadNotifications()
    }

    private fun loadNotifications() {
        progressDialogHelper.showLoadingSpinner()
        // Retrieve the stored notifications from SharedPreferences
        val existingNotifications = sharedPreferences.getString("notifications_list_" + authManager.currentUser()?.uid, "") ?: ""

        // Parse the notifications and convert them into NotificationItem objects
        val notificationList = existingNotifications.lines().mapNotNull {
            if (it.isNotBlank()) {
                val parts = it.split("|")
                NotificationItem(parts[0], parts[1]) // Create NotificationItem objects
            } else null
        }

        val defExistingNotifications = sharedPreferences.getString("notifications_list_def", "") ?: ""

        // Parse the notifications and convert them into NotificationItem objects
        val defNotificationList = defExistingNotifications.lines().mapNotNull {
            if (it.isNotBlank()) {
                val parts = it.split("|")
                NotificationItem(parts[0], parts[1]) // Create NotificationItem objects
            } else null
        }

        val totalNotifList = notificationList.plus(defNotificationList)

        // Update the adapter with the notification list
        adapter.submitList(totalNotifList)
        progressDialogHelper.hideLoadingSpinner()
    }

    override fun onResume() {
        super.onResume()
        loadNotifications()
    }
}
