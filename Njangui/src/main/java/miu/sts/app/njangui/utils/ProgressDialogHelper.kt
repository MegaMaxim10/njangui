package miu.sts.app.njangui.utils

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import miu.sts.app.njangui.R

class ProgressDialogHelper(private val activity: Activity) {
    private var dialog: AlertDialog? = null

    fun showLoadingSpinner() {
        if (dialog == null) {
            val builder = AlertDialog.Builder(activity)
            val inflater = LayoutInflater.from(activity)
            val view = inflater.inflate(R.layout.dialog_loading_spinner, null)
            builder.setView(view)
            builder.setCancelable(false) // Prevents closing the dialog on back press
            dialog = builder.create()
        }
        dialog?.show()
    }

    fun hideLoadingSpinner() {
        dialog?.dismiss()
        dialog = null
    }
}