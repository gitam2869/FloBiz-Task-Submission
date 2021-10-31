package com.app.flobiz.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.app.flobiz.common.FloBizApplication

class NetworkUtility {

    companion object {
        fun isInternetAvailable(): Boolean {
            var connected = false
            try {
                val connectivityManager = FloBizApplication.applicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                connected =
                    (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED
                            || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            return connected
        }
    }
}