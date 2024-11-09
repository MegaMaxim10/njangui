package miu.sts.app.njangui.views

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import miu.sts.app.njangui.R
import miu.sts.app.njangui.databinding.ActivityResetPasswordBinding
import miu.sts.app.njangui.utils.AlertType
import miu.sts.app.njangui.views.base.AuthBaseActivity

class ResetPasswordActivity : AuthBaseActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_reset_password)
        binding = ActivityResetPasswordBinding.bind(findViewById(R.id.content_frame))

        splashBinding.title.text = getString(R.string.reset_password_title)

        binding.btnResetPassword.setOnClickListener {
            val input = binding.etEmailReset.text.toString().trim()

            progressDialogHelper.showLoadingSpinner()
            if (validateInputs(input)) {
                handleEmailReset(input)
            } else {
                progressDialogHelper.hideLoadingSpinner()
            }
        }

        binding.tvLogin.setOnClickListener {
            redirectToLogin()
        }
    }

    private fun validateInputs(email: String): Boolean {
        var valid = true

        // Validate email
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        ) {
            binding.tvEmailError.text = getString(R.string.error_invalid_email)
            binding.tvEmailError.setTextColor(getColor(android.R.color.holo_red_dark))
            binding.tvEmailError.visibility = View.VISIBLE
            valid = false
        } else {
            binding.tvEmailError.text = ""
            binding.tvEmailError.visibility = View.GONE
        }

        return valid
    }

    private fun handleEmailReset(email: String) {
        authManager.sendPasswordResetEmail(email) { success, _ ->
            if (success) {
                showTemporaryAlert(
                    AlertType.SUCCESS,
                    getString(R.string.email_reset_success),
                    isPermanent = true
                )
            } else {
                showTemporaryAlert(
                    AlertType.ERROR,
                    getString(R.string.email_reset_failure),
                    isPermanent = true
                )
            }
            progressDialogHelper.hideLoadingSpinner()
        }
    }
}
