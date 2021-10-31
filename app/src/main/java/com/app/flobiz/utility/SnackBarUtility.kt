package com.app.flobiz.utility

import android.view.View
import com.google.android.material.snackbar.Snackbar


class SnackBarUtility {
    companion object {
        private val TAG = "SnackBarUtility"
        private var snackbar: Snackbar? = null

        fun show(view: View, message: String) {
            snackbar?.dismiss()
            snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar?.show()
        }

        fun hide(){
            snackbar?.dismiss()
        }
    }
}