package miu.sts.app.njangui.views.base

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import miu.sts.app.njangui.utils.FirebaseAuthManager
import miu.sts.app.njangui.utils.ProgressDialogHelper
import miu.sts.app.njangui.utils.UserSettingsHelper
import miu.sts.app.njangui.views.HomeActivity
import miu.sts.app.njangui.views.LoginActivity
import java.util.Locale

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var authManager: FirebaseAuthManager
    protected lateinit var progressDialogHelper: ProgressDialogHelper
    protected lateinit var currentLanguage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentLanguage = UserSettingsHelper.getLanguage(this)
        UserSettingsHelper.setLanguage(this, currentLanguage)

        val auth = FirebaseAuth.getInstance()
        authManager = FirebaseAuthManager(this, auth)

        progressDialogHelper = ProgressDialogHelper(this)
    }

    override fun attachBaseContext(newBase: Context) {
        val locale = Locale(UserSettingsHelper.getLanguage(newBase))
        val context = newBase.updateLocale(locale)
        super.attachBaseContext(context)
    }

    private fun Context.updateLocale(locale: Locale): Context {
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        return createConfigurationContext(config)
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

    protected fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    protected fun relaunchActivity() {
        val intent = intent
        finish()
        startActivity(intent)
    }
}