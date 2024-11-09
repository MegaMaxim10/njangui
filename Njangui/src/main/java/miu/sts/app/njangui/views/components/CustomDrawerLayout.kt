package miu.sts.app.njangui.views.components

import android.content.Context
import android.util.AttributeSet
import androidx.drawerlayout.widget.DrawerLayout

class CustomDrawerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : DrawerLayout(context, attrs, defStyleAttr) {
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}