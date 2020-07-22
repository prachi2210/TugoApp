package com.tugoapp.mobile.utils

import android.app.Activity
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {
    //Current Timestamp;
    fun currentTimestamp(activity: Activity?): Int {
        val timestamp = Timestamp(System.currentTimeMillis())
        return timestamp.time.toInt()
    }

    //Current Date/Time;
    fun currentDateTime(activity: Activity?): String {
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        //        String curentdate = sdf.format(c.getTime());
        return sdf.format(c.time)
    }

    //Converting Timestamp to Current Time/Date;
    fun convertTimestampToCurrenttime(activity: Activity?, timeStamp: Long): Date {
        val calendar = Calendar.getInstance()
        val tz = TimeZone.getDefault()
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
        val sdf = SimpleDateFormat(AppConstant.TIMESTAMP_FORMAT, Locale.getDefault())
        //        java.util.Date currenTimeZone = new java.util.Date(timeStamp * 1000);
        return Date(timeStamp * 1000)
    }

    //Difference between two dates;
    fun getDateDiff(dateOne: Date, dateTwo: Date): String {
        var diff = ""
        val timeDiff = Math.abs(dateOne.time - dateTwo.time)
        diff = String.format("%d hour(s) %d min(s)",
                TimeUnit.MILLISECONDS.toHours(timeDiff),
                TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)))
        return diff
    }
}