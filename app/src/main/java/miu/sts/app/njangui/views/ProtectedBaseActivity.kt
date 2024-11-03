package com.tunjasoft.fullapp.views

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.tunjasoft.fullapp.utils.FirebaseAuthManager

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