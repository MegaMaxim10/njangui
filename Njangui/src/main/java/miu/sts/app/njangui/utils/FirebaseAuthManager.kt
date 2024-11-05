package miu.sts.app.njangui.utils

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class FirebaseAuthManager(private val activity: Activity?, private val auth: FirebaseAuth) {

    fun sendOtp(phoneNumber: String, onCodeSent: (verificationId: String) -> Unit,
                onVerificationCompleted: (credential: PhoneAuthCredential) -> Unit,
                onVerificationFailed: (e: FirebaseException) -> Unit
    ) {
        val options = activity?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(it)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        onVerificationCompleted(credential)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        onVerificationFailed(e)
                    }

                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        onCodeSent(verificationId)
                    }
                }).build()
        }

        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    fun sendOtp(phoneNumber: String, onCodeSent: (verificationId: String) -> Unit,
                onVerificationFailed: (e: FirebaseException) -> Unit
    ) {
        val options = activity?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(it)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Nothing to do
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        onVerificationFailed(e)
                    }

                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        onCodeSent(verificationId)
                    }
                }).build()
        }

        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    fun verifyOtp(verificationId: String, otp: String, onSuccess: (AuthResult) -> Unit, onFailure: (Exception) -> Unit) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        if (activity != null) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        onSuccess(task.result!!)
                    } else {
                        onFailure(task.exception!!)
                    }
                }
        }
    }

    fun login(credential: AuthCredential, onComplete: (Boolean, Exception?) -> Unit) {
        if (activity != null) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    onComplete(task.isSuccessful, task.exception)
                }
        }
    }

    fun logout() {
        auth.signOut();
    }

    fun isAuthenticated(): Boolean {
        return auth.currentUser !== null
    }

    fun currentUser(): FirebaseUser? {
        return auth.currentUser;
    }
}