package miu.sts.app.njangui.views

import android.os.Bundle
import android.widget.LinearLayout
import miu.sts.app.njangui.R
import miu.sts.app.njangui.databinding.ActivityAuthBaseBinding

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