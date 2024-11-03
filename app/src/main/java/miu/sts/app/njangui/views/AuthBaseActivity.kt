package com.tunjasoft.fullapp.views

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.tunjasoft.fullapp.R
import com.tunjasoft.fullapp.databinding.ActivityAuthBaseBinding
import com.tunjasoft.fullapp.databinding.ActivityBaseDrawerBinding

abstract class AuthBaseActivity : BaseActivity() {

    protected lateinit var authBinding: ActivityAuthBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authBinding = ActivityAuthBaseBinding.inflate(layoutInflater)
        setContentView(authBinding.root)

        checkIfAuthenticated()
    }

    private fun checkIfAuthenticated() {
        if (authManager.isAuthenticated()) {
            // If a user is logged in, redirect to home activity
            redirectToHome()
        }
    }

    protected fun setContentLayout(layoutResID: Int) {
        val contentFrame = findViewById<LinearLayout>(R.id.content_frame)
        layoutInflater.inflate(layoutResID, contentFrame, true)
    }
}