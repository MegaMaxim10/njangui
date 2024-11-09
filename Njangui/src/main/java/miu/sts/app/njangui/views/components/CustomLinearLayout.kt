package miu.sts.app.njangui.views.components

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class CustomLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}