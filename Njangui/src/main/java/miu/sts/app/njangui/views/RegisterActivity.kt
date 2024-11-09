package miu.sts.app.njangui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import miu.sts.app.njangui.R
import miu.sts.app.njangui.databinding.ActivityRegisterBinding
import miu.sts.app.njangui.utils.AlertType
import miu.sts.app.njangui.utils.FieldValidator
import miu.sts.app.njangui.views.base.AuthBaseActivity

class RegisterActivity : AuthBaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_register)
        binding = ActivityRegisterBinding.bind(findViewById(R.id.content_frame))

        splashBinding.title.text = getString(R.string.register_title)

        binding.btnRegisterUser.setOnClickListener {
            val email = binding.etEmailRegister.text.toString().trim()
            val password = binding.etPasswordRegister.text.toString()
            val confirmPassword = binding.etConfirmPasswordRegister.text.toString()

            progressDialogHelper.showLoadingSpinner()
            if (validateInputs(email, password, confirmPassword)) {
                authManager.register(email, password) { result, _ ->
                    if (!result) {
                        showTemporaryAlert(
                            AlertType.ERROR,
                            getString(R.string.registration_failure),
                            isPermanent = true
                        )
                    } else {
                        showTemporaryAlert(AlertType.SUCCESS, getString(R.string.registered), true)
                        redirectToLogin()
                    }
                    progressDialogHelper.hideLoadingSpinner()
                }
            } else {
                progressDialogHelper.hideLoadingSpinner()
            }
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(email: String, password: String, confirmPassword: String): Boolean {
        var valid = true

        // Validate email
        if (!FieldValidator.isValidEmail(email)) {
            binding.tvEmailError.text = getString(R.string.error_invalid_email)
            binding.tvEmailError.setTextColor(getColor(android.R.color.holo_red_dark))
            binding.tvEmailError.visibility = View.VISIBLE
            valid = false
        } else {
            binding.tvEmailError.text = ""
            binding.tvEmailError.visibility = View.GONE
        }

        if (!FieldValidator.validatePassword(password)) {
            binding.tvPasswordError.text = getString(R.string.error_invalid_password)
            binding.tvPasswordError.setTextColor(getColor(android.R.color.holo_red_dark))
            binding.tvPasswordError.visibility = View.VISIBLE
            valid = false
        } else {
            binding.tvPasswordError.text = ""
            binding.tvPasswordError.visibility = View.GONE
        }

        // Validate password confirmation
        if (password != confirmPassword) {
            binding.tvConfirmPasswordError.text = getString(R.string.error_password_mismatch)
            binding.tvConfirmPasswordError.setTextColor(getColor(android.R.color.holo_red_dark))
            binding.tvConfirmPasswordError.visibility = View.VISIBLE
            valid = false
        } else {
            binding.tvConfirmPasswordError.text = ""
            binding.tvConfirmPasswordError.visibility = View.GONE
        }

        return valid
    }
}
