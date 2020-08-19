package com.tugoapp.mobile.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.tugoapp.mobile.R
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {
    private const val TICKS_AT_EPOCH = 621355968000000000L
    private const val TICKS_PER_MILLISECOND: Long = 10000

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    val timestamp: String
        get() = SimpleDateFormat(AppConstant.TIMESTAMP_FORMAT, Locale.US).format(Date())

    @JvmStatic
    fun isEmailValid(email: String?): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isUrlValid(url: String?): Boolean {
        Log.d("DATA", Patterns.WEB_URL.matcher(url).matches().toString() + "  ||  ")
        return Patterns.WEB_URL.matcher(url).matches()
    }

    fun showDialog(context: Context, title: Int, message: Int): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(title))
        builder.setMessage(context.getString(message))
        builder.setCancelable(false)
        return builder
    }

    fun showToast(mContext: Context?, message: String?) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
    }

    @JvmStatic
    fun showSnakeBar(view: View?, message: String?) {
        val formattedMessage = "Message: $message"
        val snackbar = Snackbar.make(view!!, formattedMessage, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(Color.parseColor("#27AE60"))
        val textView = snackbar.view.findViewById<TextView>(R.id.snackbar_text)
        if (message != null && message.length > 100) {
            textView.setLines(4)
            snackbar.duration = 6000
        } else {
            textView.setLines(3)
            snackbar.duration = 3000
        }
        textView.setTextColor(Color.WHITE)
        textView.setTextAppearance(R.style.StyleSnackbar)
        textView.gravity = Gravity.CENTER
        snackbar.show()
    }

    fun formatDate(time: Long, formateDate: String?): String {
        if (time != 0L) {
            val formatter = SimpleDateFormat(formateDate)
            return formatter.format(time)
        }
        return "0"
    }

    fun getDatefromticks(value: Long): String {
        val date = Date((value - TICKS_AT_EPOCH) / TICKS_PER_MILLISECOND)
        val utc = TimeZone.getTimeZone("UTC")
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, +1)
        return formatDate(calendar.timeInMillis, "MM/dd/yyyy")
    }

    fun getDate(milliSeconds: Long): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat("MM/dd/yyyy")

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    @JvmStatic
    fun getAppVersion(context: Context): String? {
        var version: String? = null
        version = try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
        return version
    }

    fun showLoadingDialog(context: Context, message: String): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        progressDialog.isIndeterminate = true
        if (!message.isEmpty()) {
            progressDialog.setMessage(message)
        } else {
            progressDialog.setMessage(context.getString(R.string.txt_loading))
        }
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    fun doSendMessageToWhatsApp(context: Context, view: View?) {
        try {
            var toNumber = "919824123970";
            var sendIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$toNumber?body="));
            sendIntent.setPackage("com.whatsapp");
            context.startActivity(sendIntent);
        } catch (e : Exception) {
            showSnakeBar(view,context.getString(R.string.whatsapp_not_found))
        }
    }
}