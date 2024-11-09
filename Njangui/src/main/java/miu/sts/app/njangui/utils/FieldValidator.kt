package miu.sts.app.njangui.utils

import android.util.Patterns

class FieldValidator {
    companion object {
        fun validatePassword(password: String): Boolean {
            val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
            return password.matches(passwordPattern)
        }

        fun isValidEmail(input: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(input).matches()
        }
    }
}