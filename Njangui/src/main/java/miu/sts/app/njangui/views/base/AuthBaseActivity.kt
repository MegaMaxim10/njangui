package miu.sts.app.njangui.views.base

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import miu.sts.app.njangui.R
import miu.sts.app.njangui.databinding.ActivityAuthBaseBinding
import miu.sts.app.njangui.databinding.SplashHeaderBinding
import miu.sts.app.njangui.utils.AlertType

abstract class AuthBaseActivity : BaseActivity() {

    protected lateinit var authBinding: ActivityAuthBaseBinding
    protected lateinit var splashBinding: SplashHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfAuthenticated()

        authBinding = ActivityAuthBaseBinding.inflate(layoutInflater)
        setContentView(authBinding.root)
        setSplashLayout(R.layout.splash_header)
        splashBinding = SplashHeaderBinding.bind(authBinding.splash)

        authBinding.root.setOnTouchListener { _, _ ->
            hideKeyboard(authBinding.root)
            authBinding.root.performClick()
            false
        }
    }

    override fun onResume() {
        super.onResume()
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

    private fun setSplashLayout(layoutResID: Int) {
        val contentFrame = findViewById<LinearLayout>(R.id.splash)
        layoutInflater.inflate(layoutResID, contentFrame, true)
    }

    protected fun showTemporaryAlert(
        type: AlertType,
        message: String,
        onlyToast: Boolean = false,
        isPermanent: Boolean = false
    ) {
        val toastMessage = when (type) {
            AlertType.SUCCESS -> "${getString(R.string.operation_success)}: $message"
            AlertType.ERROR -> "${getString(R.string.operation_failed)}: $message"
            AlertType.WARNING -> "${getString(R.string.warning)}: $message"
            AlertType.INFO -> message
        }
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()

        if (!onlyToast) {
            var background = R.drawable.rounded_success_background
            var color = R.color.success
            if (type == AlertType.SUCCESS) {
                background = R.drawable.rounded_success_background
                color = R.color.success
            } else if (type == AlertType.ERROR) {
                background = R.drawable.rounded_error_background
                color = R.color.error
            } else if (type == AlertType.WARNING) {
                background = R.drawable.rounded_warning_background
                color = R.color.warning
            } else if (type == AlertType.INFO) {
                background = R.drawable.rounded_info_background
                color = R.color.info
            }
            splashBinding.tvResult.text = toastMessage
            splashBinding.tvResult.background =
                ContextCompat.getDrawable(applicationContext, background)
            splashBinding.tvResult.setTextColor(ContextCompat.getColor(applicationContext, color))
            splashBinding.tvResult.visibility = View.VISIBLE

            if (!isPermanent) {
                splashBinding.tvResult.postDelayed({
                    clearAlert()
                }, 30000)
            }
        } else {
            clearAlert()
        }
    }

    private fun clearAlert() {
        splashBinding.tvResult.text = ""
        splashBinding.tvResult.visibility = View.GONE
    }
}