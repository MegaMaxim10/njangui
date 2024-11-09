package miu.sts.app.njangui.views.base

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import miu.sts.app.njangui.BuildConfig
import miu.sts.app.njangui.R
import miu.sts.app.njangui.databinding.ActivityBaseDrawerBinding
import miu.sts.app.njangui.utils.UserSettingsHelper

open class ProtectedBaseDrawerActivity : ProtectedBaseActivity() {

    protected lateinit var drawerBinding: ActivityBaseDrawerBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var userAvatar: ImageView
    private lateinit var userEmail: TextView
    private lateinit var appInfos: TextView
    private lateinit var tvLogout: TextView
    private lateinit var languageSpinner: Spinner

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
                R.id.nav_tontine_groups -> {
                    authManager.logout()
                    redirectToLogin()
                }
            }
            drawerBinding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val headerView: View = drawerBinding.drawerMenu.getHeaderView(0)
        userAvatar = headerView.findViewById(R.id.userAvatar)
        userEmail = headerView.findViewById(R.id.userEmail)
        tvLogout = headerView.findViewById(R.id.tvLogout)

        tvLogout.setOnClickListener {
            authManager.logout()
            redirectToLogin()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerBinding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        })

        drawerBinding.root.setOnTouchListener { _, _ ->
            hideKeyboard(drawerBinding.root)
            drawerBinding.root.performClick()
            false
        }

        loadUserInformation()

        // Set up the language dropdown (Spinner)
        val background = ContextCompat.getDrawable(
            applicationContext,
            if (!BuildConfig.DEBUG) R.drawable.rounded_warning_background else R.drawable.rounded_success_background
        )
        val color = ContextCompat.getColor(
            applicationContext,
            if (!BuildConfig.DEBUG) R.color.warning else R.color.success
        )

        languageSpinner = drawerBinding.drawerMenu.findViewById(R.id.languageSpinner)
        val languages = listOf("English", "Français")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.background = background
        languageSpinner.adapter = adapter
        languageSpinner.setSelection(if (currentLanguage == "fr") 1 else 0)

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLanguageCode = if (position == 1) "fr" else "en"

                if (currentLanguage != selectedLanguageCode) {
                    UserSettingsHelper.setLanguage(
                        this@ProtectedBaseDrawerActivity,
                        selectedLanguageCode
                    )

                    relaunchActivity()
                }

                (parent!!.getChildAt(0) as TextView).setTextColor(color)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        appInfos = drawerBinding.drawerMenu.findViewById(R.id.appInfos)
        appInfos.setTextColor(color)
        appInfos.text = getString(
            R.string.version_text,
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE,
            BuildConfig.CODE_NAME
        )
    }

    private fun loadUserInformation() {
        val userInformation = authManager.getUserInformation()
        userEmail.text = userInformation.email ?: getString(R.string.not_available)
        userInformation.avatar?.let { uri ->
            Glide.with(this).load(uri).circleCrop().into(userAvatar)
        }
    }

    protected fun setContentLayout(layoutResID: Int) {
        val contentFrame = findViewById<LinearLayout>(R.id.content_frame)
        layoutInflater.inflate(layoutResID, contentFrame, true)
    }
}
