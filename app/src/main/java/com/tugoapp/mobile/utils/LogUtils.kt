package com.tugoapp.mobile.utils

import android.util.Log

object LogUtils {
    private const val DEBUG = true
    fun i(tag: String?, string: String?) {
        if (DEBUG) Log.i(tag, string)
    }

    fun e(tag: String?, string: String?) {
        if (DEBUG) Log.e(tag, string)
    }

    fun d(tag: String?, string: String?) {
        if (DEBUG) Log.d(tag, string)
    }

    fun v(tag: String?, string: String?) {
        if (DEBUG) Log.v(tag, string)
    }

    fun w(tag: String?, string: String?) {
        if (DEBUG) Log.w(tag, string)
    }
}