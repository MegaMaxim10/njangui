package com.tunjasoft.fullapp.views

import ProtectedBaseDrawerActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.tunjasoft.fullapp.R
import com.tunjasoft.fullapp.databinding.ActivityHomeBinding

class HomeActivity : ProtectedBaseDrawerActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_home)
        binding = ActivityHomeBinding.bind(findViewById(R.id.content_frame))

        // Set title for the activity
        super.drawerBinding.toolbar.title = getString(R.string.home_title)

        FirebaseMessaging.getInstance().subscribeToTopic("default_notification_channel")
    }

    override fun onResume() {
        super.onResume()
        authManager.updateUserDevice { _, _ ->  }
    }
}
