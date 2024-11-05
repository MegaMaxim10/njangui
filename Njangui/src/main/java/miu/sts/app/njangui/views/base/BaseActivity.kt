package miu.sts.app.njangui.views.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import miu.sts.app.njangui.utils.FirebaseAuthManager
import miu.sts.app.njangui.views.HomeActivity
import miu.sts.app.njangui.views.LoginActivity

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var authManager: FirebaseAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        authManager = FirebaseAuthManager(this, auth)
    }

    protected fun redirectToHome() {
        val homeIntent = Intent(this, HomeActivity::class.java)
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(homeIntent)
        finish()
    }

    protected fun redirectToLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(loginIntent)
        finish()
    }
}