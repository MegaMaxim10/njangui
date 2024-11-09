package miu.sts.app.njangui.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import miu.sts.app.njangui.R
import miu.sts.app.njangui.databinding.ActivityLoginBinding
import miu.sts.app.njangui.utils.AlertType
import miu.sts.app.njangui.utils.FieldValidator
import miu.sts.app.njangui.views.base.AuthBaseActivity

class LoginActivity : AuthBaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_login)
        binding = ActivityLoginBinding.bind(authBinding.contentFrame)

        splashBinding.title.text = getString(R.string.login_title)

        // Login button click listener
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString().trim()
            val password = binding.etPasswordLogin.text.toString()
            progressDialogHelper.showLoadingSpinner()
            if (validateEmailPassword(email, password)) {
                loginWithEmail(email, password)
            } else {
                progressDialogHelper.hideLoadingSpinner()
            }
        }

        // Register link
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Reset password link
        binding.tvResetPassword.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedOnce) {
                    finishAffinity()
                } else {
                    backPressedOnce = true
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT
                    ).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        backPressedOnce = false
                    }, 2000)
                }
            }
        })
    }

    private fun loginWithEmail(email: String, password: String) {
        authManager.login(email, password) { success, _ ->
            if (success) {
                showTemporaryAlert(AlertType.SUCCESS, getString(R.string.logged_in), true)
                startActivity(
                    Intent(
                        this,
                        HomeActivity::class.java
                    )
                )
            } else {
                showTemporaryAlert(
                    AlertType.ERROR,
                    getString(R.string.login_failure),
                    isPermanent = true
                )
            }
            progressDialogHelper.hideLoadingSpinner()
        }
    }

    private fun validateEmailPassword(email: String, password: String): Boolean {
        var valid = true
        if (!FieldValidator.isValidEmail(email)) {
            binding.tvEmailError.text = getString(R.string.error_invalid_email)
            binding.tvEmailError.visibility = View.VISIBLE
            valid = false
        } else {
            binding.tvEmailError.text = ""
            binding.tvEmailError.visibility = View.GONE
        }

        if (password.isBlank()) {
            binding.tvPasswordError.text = getString(R.string.error_empty_password)
            binding.tvPasswordError.visibility = View.VISIBLE
            valid = false
        } else {
            binding.tvPasswordError.text = ""
            binding.tvPasswordError.visibility = View.GONE
        }
        return valid
    }
}
