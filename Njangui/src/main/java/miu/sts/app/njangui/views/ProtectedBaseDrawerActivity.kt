package miu.sts.app.njangui.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import miu.sts.app.njangui.R
import miu.sts.app.njangui.databinding.ActivityBaseDrawerBinding

open class ProtectedBaseDrawerActivity : ProtectedBaseActivity() {

    protected lateinit var drawerBinding: ActivityBaseDrawerBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var userAvatar: ImageView
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userPhone: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerBinding = ActivityBaseDrawerBinding.inflate(layoutInflater)
        setContentView(drawerBinding.root)

        // Set up toolbar
        val toolbar: Toolbar = drawerBinding.toolbar
        setSupportActionBar(toolbar)

        // Set up drawer toggle
        toggle = ActionBarDrawerToggle(
            this, drawerBinding.drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawerBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up the drawer menu functionality
        drawerBinding.drawerMenu.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    authManager.logout()
                    redirectToLogin()
                }
                R.id.nav_push_notification -> {
                    startActivity(Intent(this, NotificationActivity::class.java))
                }
            }
            drawerBinding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Retrieve user information and set it in the drawer header
        val headerView: View = drawerBinding.drawerMenu.getHeaderView(0)
        userAvatar = headerView.findViewById(R.id.user_avatar)
        userName = headerView.findViewById(R.id.user_name)
        userEmail = headerView.findViewById(R.id.user_email)
        userPhone = headerView.findViewById(R.id.user_phone)

        loadUserInformation()
    }

    private fun loadUserInformation() {
        authManager.currentUser()?.let {
            // Set user's name and email from FirebaseAuth
            userName.text = it.displayName ?: getString(R.string.not_available)
            userEmail.text = it.email ?: getString(R.string.not_available)

            // Optionally, load avatar using Glide if the user has a profile picture
            it.photoUrl?.let { uri ->
                Glide.with(this).load(uri).circleCrop().into(userAvatar)
            }

            FirebaseFirestore.getInstance().collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userPhone.text = document.getString("phoneNumber") ?: getString(R.string.not_available)
                    }
                }
                .addOnFailureListener {
                    userPhone.text = getString(R.string.not_available)
                }
        }
    }

    override fun onBackPressed() {
        if (drawerBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    protected fun setContentLayout(layoutResID: Int) {
        val contentFrame = findViewById<LinearLayout>(R.id.content_frame)
        layoutInflater.inflate(layoutResID, contentFrame, true)
    }
}
