package miu.sts.app.njangui.utils

import android.app.Activity
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
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

    fun updateUserDevice(onComplete: (Boolean, Exception?) -> Unit) {
        val user = auth.currentUser ?: return onComplete(false, Exception("No authenticated user"))
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            val uid = user.uid
            val firestore = FirebaseFirestore.getInstance()

            val deviceData = hashMapOf(
                "token" to token,
                "userId" to uid
            )

            // Save the user data in Firestore under the user's uid
            firestore.collection("devices").document(uid)
                .set(deviceData)
                .addOnCompleteListener { saveTask ->
                    Log.e("ERROR", saveTask.isSuccessful.toString(), saveTask.exception)
                    if (saveTask.isSuccessful) {
                        // All operations successful
                        onComplete(true, null)
                    } else {
                        // Firestore save failed
                        onComplete(false, saveTask.exception)
                    }
                }
        })
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