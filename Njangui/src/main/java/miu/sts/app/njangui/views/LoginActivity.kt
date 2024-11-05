package miu.sts.app.njangui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import miu.sts.app.njangui.R
import miu.sts.app.njangui.databinding.ActivityLoginBinding
import miu.sts.app.njangui.utils.ProgressDialogHelper

class LoginActivity : AuthBaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialogHelper: ProgressDialogHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_login)
        binding = ActivityLoginBinding.bind(findViewById(R.id.content_frame))

        progressDialogHelper = ProgressDialogHelper(this)

        // Login button click listener
        binding.btnLogin.setOnClickListener {
            val phoneNumber = binding.etPhoneLogin.text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                loginWithPhone(phoneNumber)
            } else {
                showTemporaryAlert(false, getString(R.string.error_empty_phone))
            }
        }
    }

    private fun loginWithPhone(phoneNumber: String) {
        progressDialogHelper.showLoadingSpinner()
        authManager.sendOtp(phoneNumber,
            { verificationId ->
                progressDialogHelper.hideLoadingSpinner()
                val intent = Intent(this, OtpVerificationActivity::class.java)
                intent.putExtra("verificationId", verificationId)
                intent.putExtra("action", "login")
                intent.putExtra("phoneNumber", phoneNumber)
                startActivity(intent)
            },
            { credential ->
                authManager.login(credential) { success, _ ->
                    progressDialogHelper.hideLoadingSpinner()
                    if (success) {
                        showTemporaryAlert(true, getString(R.string.logged_in))
                        redirectToHome()
                    } else {
                        showTemporaryAlert(false, getString(R.string.login_failure))
                    }
                }
            },
            {
                progressDialogHelper.hideLoadingSpinner()
                showTemporaryAlert(false, getString(R.string.otp_send_error))
            }
        )
    }

    private fun showTemporaryAlert(success: Boolean, message: String) {
        val backgroundColor = if (success) android.R.color.holo_green_light else android.R.color.holo_red_light
        val toastMessage = if (success) "Operation successful" else "Operation failed: $message"
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()

        binding.tvResult.text = toastMessage
        binding.tvResult.setBackgroundColor(resources.getColor(backgroundColor))
        binding.tvResult.visibility = View.VISIBLE

        // Hide the alert after 15 seconds
        binding.tvResult.postDelayed({
            binding.tvResult.visibility = View.GONE
        }, 15000)
    }
}
