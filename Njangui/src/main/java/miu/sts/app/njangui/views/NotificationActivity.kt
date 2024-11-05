package miu.sts.app.njangui.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import miu.sts.app.njangui.R
import miu.sts.app.njangui.adapters.NotificationAdapter
import miu.sts.app.njangui.databinding.ActivityNotificationBinding
import miu.sts.app.njangui.models.NotificationItem
import miu.sts.app.njangui.utils.ProgressDialogHelper

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
