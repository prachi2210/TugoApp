package com.tugoapp.mobile.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils

object SharedPrefsUtils {
    fun clearAllsharedPrefData(context: Context?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences?.edit()?.clear()?.commit()
    }

    fun clearSharedPrefData(context: Context?, key: String?): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null && !TextUtils.isEmpty(key)) {
            val editor = preferences.edit()
            editor.remove(key)
            return editor.commit()
        }
        return false
    }

    /**
     * Helper method to retrieve a String value from [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @return The value from shared preferences, or null if the value could not be read.
     */
    fun getStringPreference(context: Context?, key: String?): String? {
        var value: String? = null
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getString(key, null)
        }
        return value
    }

    fun getStringPreference(context: Context?, key: String?, defvalue : String?): String? {
        var value: String? = null
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getString(key, defvalue)
        }
        return value
    }


    /**
     * Helper method to write a String value to [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    fun setStringPreference(context: Context?, key: String?, value: String?): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null && !TextUtils.isEmpty(key)) {
            val editor = preferences.edit()
            editor.putString(key, value)
            return editor.commit()
        }
        return false
    }

    /**
     * Helper method to retrieve a float value from [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    fun getFloatPreference(context: Context?, key: String?, defaultValue: Float): Float {
        var value = defaultValue
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getFloat(key, defaultValue)
        }
        return value
    }

    /**
     * Helper method to write a float value to [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    fun setFloatPreference(context: Context?, key: String?, value: Float): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putFloat(key, value)
            return editor.commit()
        }
        return false
    }

    /**
     * Helper method to retrieve a long value from [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    fun getLongPreference(context: Context?, key: String?, defaultValue: Long): Long {
        var value = defaultValue
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getLong(key, defaultValue)
        }
        return value
    }

    /**
     * Helper method to write a long value to [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    fun setLongPreference(context: Context?, key: String?, value: Long): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putLong(key, value)
            return editor.commit()
        }
        return false
    }

    /**
     * Helper method to retrieve an integer value from [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    fun getIntegerPreference(context: Context?, key: String?, defaultValue: Int): Int {
        var value = defaultValue
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getInt(key, defaultValue)
        }
        return value
    }

    /**
     * Helper method to write an integer value to [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    fun setIntegerPreference(context: Context?, key: String?, value: Int): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putInt(key, value)
            return editor.commit()
        }
        return false
    }

    /**
     * Helper method to retrieve a boolean value from [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param defaultValue A default to return if the value could not be read.
     * @return The value from shared preferences, or the provided default.
     */
    fun getBooleanPreference(context: Context?, key: String?, defaultValue: Boolean): Boolean {
        var value = defaultValue
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            value = preferences.getBoolean(key, defaultValue)
        }
        return value
    }

    /**
     * Helper method to write a boolean value to [SharedPreferences].
     *
     * @param context a [Context] object.
     * @param key
     * @param value
     * @return true if the new value was successfully written to persistent storage.
     */
    @JvmStatic
    fun setBooleanPreference(context: Context?, key: String?, value: Boolean): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putBoolean(key, value)
            return editor.commit()
        }
        return false
    }

    fun setWalkthroughForUser(context: Context, key: String?): Boolean {
        val preferences = context.getSharedPreferences(AppConstant.WALKTHROUGH_PREFS_NAME, Context.MODE_PRIVATE)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putBoolean(key, true)
            return editor.commit()
        }
        return false
    }

    fun savePushToken(context: Context, key: String?, value: String?): Boolean {
        val preferences = context.getSharedPreferences(AppConstant.WALKTHROUGH_PREFS_NAME, Context.MODE_PRIVATE)
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putString(key, value)
            return editor.commit()
        }
        return false
    }

    fun getPushToken(context: Context, key: String?): String {
        val preferences = context.getSharedPreferences(AppConstant.WALKTHROUGH_PREFS_NAME, Context.MODE_PRIVATE)
        var data = preferences.getString(key,"")
        return data.toString()
    }

    fun didUserSeenWalkthrough(context: Context, key: String?): Boolean {
        val preferences = context.getSharedPreferences(AppConstant.WALKTHROUGH_PREFS_NAME, Context.MODE_PRIVATE)
        return preferences?.contains(key) ?: false
    }
}