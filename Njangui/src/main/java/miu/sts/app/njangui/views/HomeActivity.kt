package miu.sts.app.njangui.views

import android.os.Bundle
import miu.sts.app.njangui.R
import miu.sts.app.njangui.databinding.ActivityHomeBinding
import miu.sts.app.njangui.views.base.ProtectedBaseDrawerActivity

class HomeActivity : ProtectedBaseDrawerActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout(R.layout.activity_home)
        binding = ActivityHomeBinding.bind(findViewById(R.id.content_frame))

        // Set title for the activity
        super.drawerBinding.toolbar.title = getString(R.string.home_title)
    }
}
