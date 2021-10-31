package com.app.flobiz.utility

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import com.app.flobiz.R

class IntentUtility {
    companion object {
        private const val TAG = "ShareUtility"

        fun openURL(context: Context, url:String, view: View?) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            val chooserIntent = Intent.createChooser(intent, "Open With")

            if (intent.resolveActivity(context.packageManager) != null) {
                ContextCompat.startActivity(context, chooserIntent, null)
            } else {
                if (view != null) {
                    SnackBarUtility.show(
                        view,
                        context.resources.getString(R.string.no_browser_found)
                    )
                }
            }
        }
    }
}