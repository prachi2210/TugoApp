package com.tugoapp.mobile.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils.getAppVersion
import com.tugoapp.mobile.utils.SharedPrefsUtils
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

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


    override fun onResume() {
        super.onResume()
        iniUI()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun iniUI() {
        mContext = context
        auth = FirebaseAuth.getInstance()
        txtAppVersion.text = "App Version: " + getAppVersion(mContext!!)
        navigateToStartPage()
    }

    private fun navigateToStartPage() {
        val handler = Handler()
        handler.postDelayed({ navigate() }, AppConstant.SPLASH_TIME)
    }

    private fun navigate() {
        if(auth.currentUser != null && !auth.currentUser?.email.isNullOrBlank()) {
            var email = auth.currentUser?.email
            var phoneNumber = auth.currentUser?.phoneNumber
            if(!email.isNullOrBlank() && phoneNumber.isNullOrBlank()) {
                SharedPrefsUtils.setStringPreference(mContext,AppConstant.FULL_NAME,auth?.currentUser?.displayName)
                var bundle = bundleOf(AppConstant.FIREBASE_EMAIL_ADDRESS to email)
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSplash_to_fragmentAddPhoneNumber,bundle)
            } else if(!email.isNullOrBlank() && !phoneNumber.isNullOrBlank()) {
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSplash_to_fragmentHome)
            }
        } else {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSplash_to_fragmentWelcome)
        }
    }
}