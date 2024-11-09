package miu.sts.app.njangui.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import miu.sts.app.njangui.R
import miu.sts.app.njangui.databinding.ActivityHomeBinding
import miu.sts.app.njangui.views.base.ProtectedBaseDrawerActivity

class HomeActivity : ProtectedBaseDrawerActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_home)
        binding = ActivityHomeBinding.bind(findViewById(R.id.content_frame))

        // Set title for the activity
        super.drawerBinding.toolbar.title = getString(R.string.home_title)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerBinding.drawerLayout.closeDrawer(GravityCompat.START)
                    backPressedOnce = false
                } else if (backPressedOnce) {
                    finishAffinity()
                } else {
                    backPressedOnce = true
                    Toast.makeText(
                        this@HomeActivity,
                        getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT
                    ).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        backPressedOnce = false
                    }, 2000)
                }
            }
        })
    }
}
