package com.tugoapp.mobile.utils

import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object Validation {
    fun isEmpty(editText: String): Boolean {
        return editText.isEmpty()
    }

    fun setError(editText: EditText, errorString: String?) {
        editText.error = errorString
    }

    fun clearError(editText: EditText) {
        editText.error = null
    }

    fun isValidEmail(email: String?): Boolean {
        return TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validateEmail(email: String?): Boolean {
        val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$"
        ///String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches()
    }

    fun isValidMobileOld(phone: String): Boolean {
        return if (!phone.isEmpty()) false else Patterns.PHONE.matcher(phone).matches()
    }

    fun isValidMobile(phone: String?): Boolean {
        if (phone == null || phone.isEmpty()) return false
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            phone.length > 6 && phone.length <= 13
        } else false
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    //pattern: Name start with letter and number contain hyphen and underscore min 3 letters or max 15 letter
    fun validateUsername(username: String?): Boolean {
        val USERNAME_PATTERN = "^[A-Za-z0-9_-]{3,15}$"
        return Pattern.compile(USERNAME_PATTERN).matcher(username).matches()
    }

    fun isValidPan(pan: String?): Boolean {
        val mPattern = Pattern.compile("[a-z]{5}[0-9]{4}[a-z]{1}")
        val mMatcher = mPattern.matcher(pan)
        return mMatcher.matches()
    }

    fun isAgeValid(input: String?): Boolean {
        // check if the age is >= 18
        try {
            val calendarBirthday = Calendar.getInstance()
            val calendarToday = Calendar.getInstance()
            calendarBirthday.time = SimpleDateFormat("d/MM/yyyy", Locale.US).parse(input)
            val yearOfToday = calendarToday[Calendar.YEAR]
            val yearOfBirthday = calendarBirthday[Calendar.YEAR]
            if (yearOfToday - yearOfBirthday > 18) {
                return true
            } else if (yearOfToday - yearOfBirthday == 18) {
                val monthOfToday = calendarToday[Calendar.MONTH]
                val monthOfBirthday = calendarBirthday[Calendar.MONTH]
                if (monthOfToday > monthOfBirthday) {
                    return true
                } else if (monthOfToday == monthOfBirthday) {
                    if (calendarToday[Calendar.DAY_OF_MONTH] >= calendarBirthday[Calendar.DAY_OF_MONTH]) {
                        return true
                    }
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    fun isIpAddressValid(ip: String?): Boolean {
        val IP_ADDRESS_STRING_PATTERN = ("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")
        return Pattern.compile(IP_ADDRESS_STRING_PATTERN).matcher(ip).matches()
    }

    fun isUrlValid(url: String?): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }
}