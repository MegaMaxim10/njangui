package miu.sts.app.njangui.views.base

import android.os.Bundle

abstract class ProtectedBaseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkIfAuthenticated()
    }

    private fun checkIfAuthenticated() {
        if (!authManager.isAuthenticated()) {
            // If no user is logged in, redirect to login activity
            redirectToLogin()
        }
    }
}