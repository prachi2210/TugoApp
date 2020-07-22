package com.tugoapp.mobile.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tugoapp.mobile.utils.NetworkUtils

class NetworkStateChangeListener(protected var listeners: NetworkStateReceiverListener) : BroadcastReceiver() {
    private val TAG = "NetworkStateChange"
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Intent broadcast received")
        if (intent?.extras == null) return
        if (NetworkUtils.isNetworkConnected(context)) {
            listeners.networkAvailable()
        } else {
            listeners.networkUnavailable()
        }
    }

    interface NetworkStateReceiverListener {
        fun networkAvailable()
        fun networkUnavailable()
    }
}