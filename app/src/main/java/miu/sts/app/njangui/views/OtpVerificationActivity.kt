package com.tunjasoft.fullapp.views

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.tunjasoft.fullapp.R
import com.tunjasoft.fullapp.databinding.ActivityOtpVerificationBinding
import com.tunjasoft.fullapp.utils.ProgressDialogHelper

class OtpVerificationActivity : AuthBaseActivity() {

    private lateinit var binding: ActivityOtpVerificationBinding
    private lateinit var progressDialogHelper: ProgressDialogHelper
    private var action: String? = null
    private var phoneNumber: String? = null
    private var password: String? = null
    private var email: String? = null
    private lateinit var verificationId: String

    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_otp_verification)
        binding = ActivityOtpVerificationBinding.bind(findViewById(R.id.content_frame))

        progressDialogHelper = ProgressDialogHelper(this)

        verificationId = intent.getStringExtra("verificationId") ?: ""
        action = intent.getStringExtra("action")
        phoneNumber = intent.getStringExtra("phoneNumber")
        password = intent.getStringExtra("password")
        email = intent.getStringExtra("email")

        // Start countdown for 60 seconds
        startOtpCountdown()

        binding.btnVerifyOtp.setOnClickListener {
            val otp = binding.etOtp.text.toString().trim()
            if (otp.isNotEmpty()) {
                progressDialogHelper.showLoadingSpinner()
                authManager.verifyOtp(verificationId, otp, {
                    countdownTimer?.cancel()
                    progressDialogHelper.hideLoadingSpinner()
                    when (action) {
                        "login" -> {
                            showTemporaryAlert(true, getString(R.string.logged_in))
                            redirectToHome()
                        }
                    }
                }, {
                    progressDialogHelper.hideLoadingSpinner()
                    showTemporaryAlert(false, getString(R.string.otp_verif_failure))
                })
            }
        }

        binding.btnResendOtp.setOnClickListener {
            if (phoneNumber != null) {
                progressDialogHelper.showLoadingSpinner()
                authManager.sendOtp(phoneNumber!!,
                    {newVerificationId ->
                        progressDialogHelper.hideLoadingSpinner()
                        verificationId = newVerificationId
                        startOtpCountdown() // Restart countdown after OTP resend
                        binding.btnResendOtp.visibility = View.GONE // Hide resend button again
                    },
                    {
                        progressDialogHelper.hideLoadingSpinner()
                        showTemporaryAlert(false, getString(R.string.otp_send_error))
                    }
                )
            }
        }
    }

    private fun startOtpCountdown() {
        countdownTimer?.cancel()
        binding.tvCountdown.visibility = View.VISIBLE
        binding.tvCountdown.text = "60s remaining"

        countdownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCountdown.text = "${millisUntilFinished / 1000}s remaining"
            }

            override fun onFinish() {
                binding.tvCountdown.visibility = View.GONE
                binding.btnResendOtp.visibility = View.VISIBLE // Show resend button when countdown ends
            }
        }.start()
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