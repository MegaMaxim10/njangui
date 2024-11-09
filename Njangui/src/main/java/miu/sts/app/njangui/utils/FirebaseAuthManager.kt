package miu.sts.app.njangui.utils

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import miu.sts.app.njangui.models.UserInformation

class FirebaseAuthManager(private val activity: Activity?, private val auth: FirebaseAuth) {
    fun login(email: String, password: String, onComplete: (Boolean, Exception?) -> Unit) {
        if (activity != null) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    onComplete(task.isSuccessful, task.exception)
                }
        }
    }

    fun register(email: String, password: String, onComplete: (Boolean, Exception?) -> Unit) {
        if (activity != null) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    onComplete(task.isSuccessful, task.exception)
                }
        }
    }

    fun sendPasswordResetEmail(email: String, onComplete: (Boolean, Exception?) -> Unit) {
        if (activity != null) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(activity) { task ->
                    onComplete(task.isSuccessful, task.exception)
                }
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun isAuthenticated(): Boolean {
        return auth.currentUser !== null
    }

    fun getUserInformation(): UserInformation {
        val userInformation = UserInformation()
        auth.currentUser?.let {
            userInformation.email = it.email
            userInformation.avatar = it.photoUrl
        }
        return userInformation
    }
}