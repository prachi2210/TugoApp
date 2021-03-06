package com.tugoapp.mobile.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.SaveDeviceTokenRequestModel
import com.tugoapp.mobile.data.remote.model.response.PaymentConfigResponseModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import com.tugoapp.mobile.utils.CommonUtils.getAppVersion
import com.tugoapp.mobile.utils.LogUtils
import com.tugoapp.mobile.utils.SharedPrefsUtils
import kotlinx.android.synthetic.main.fragment_splash.*
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.math.log

class FragmentSplash : BaseFragment<SplashViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mSplashViewModel: SplashViewModel? = null
    var mContext: Context? = null
    private lateinit var auth: FirebaseAuth

    override val layoutId: Int
        get() = R.layout.fragment_splash

    override val viewModel: SplashViewModel
        get() {
            mSplashViewModel = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
            return mSplashViewModel!!
        }
    override val screenTitle: String
        get() = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onResume() {
        super.onResume()
        iniUI()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun iniUI() {
        mContext = requireContext()
        txtAppVersion.text = "App Version: " + getAppVersion(mContext!!)
        navigateToStartPage()
    }

    private fun navigateToStartPage() {
        val handler = Handler()
        handler.postDelayed({ navigate() }, AppConstant.SPLASH_TIME)
    }

    private fun navigate() {
//        if(auth.currentUser != null && !auth.currentUser?.email.isNullOrBlank()) {
//            var email = auth.currentUser?.email
//            var phoneNumber = auth.currentUser?.phoneNumber
//            if(!email.isNullOrBlank() && phoneNumber.isNullOrBlank()) {
//                SharedPrefsUtils.setStringPreference(mContext,AppConstant.FULL_NAME,auth?.currentUser?.displayName)
//                var bundle = bundleOf(AppConstant.IS_FROM_EDIT_PROFILE to false,AppConstant.FIREBASE_EMAIL_ADDRESS to email)
//                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSplash_to_fragmentAddPhoneNumber,bundle)
//            } else if(!email.isNullOrBlank() && !phoneNumber.isNullOrBlank()) {
//                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSplash_to_fragmentHome)
//                updatePushToken()
//            }
//        } else {
//            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSplash_to_fragmentWelcome)
//        }
        val isLoggedIn = SharedPrefsUtils.getBooleanPreference(mContext,AppConstant.IS_LOGGED_IN,false)
        val email = SharedPrefsUtils.getStringPreference(mContext,AppConstant.LOGGED_IN_EMAIL)
        if(isLoggedIn && !email.isNullOrBlank()) {
            val phoneNumber = SharedPrefsUtils.getStringPreference(mContext,AppConstant.LOGGED_IN_PHONE)
            if(phoneNumber.isNullOrBlank()) {
                SharedPrefsUtils.setStringPreference(mContext,AppConstant.FULL_NAME,auth?.currentUser?.displayName)
                val bundle = bundleOf(AppConstant.IS_FROM_EDIT_PROFILE to false,AppConstant.FIREBASE_EMAIL_ADDRESS to email)
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSplash_to_fragmentAddPhoneNumber,bundle)
            } else {
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSplash_to_fragmentHome)
                updatePushToken()
            }
        } else {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSplash_to_fragmentWelcome)
        }
    }

    private fun updatePushToken() {
        Thread(Runnable {
            try {
                FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var token = mContext?.let { SharedPrefsUtils.getPushToken(it,AppConstant.PREF_KEY_PUSH_TOKEN) }
                        mSplashViewModel?.doUpdateToken(SaveDeviceTokenRequestModel(mContext?.let { CommonUtils.getDeviceId(it) }, token , "android", TimeZone.getDefault().displayName))

                        mSplashViewModel?.doGetPaymentGatewayConfigs(task.result?.token)
                    }
                })

            } catch (e: IOException) {
                LogUtils.e("Error",e.localizedMessage)
            }
        }).start()
    }
}