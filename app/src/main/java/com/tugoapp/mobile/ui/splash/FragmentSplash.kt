package com.tugoapp.mobile.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.databinding.FragmentSplashBinding
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils.getAppVersion
import kotlinx.android.synthetic.main.fragment_splash.*
import javax.inject.Inject

class FragmentSplash : BaseFragment<SplashViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mSplashViewModel: SplashViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_splash

    override val viewModel: SplashViewModel
        get() {
            mSplashViewModel = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
            return mSplashViewModel!!
        }

    override fun onResume() {
        super.onResume()
        iniUI()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun iniUI() {
        mContext = context
        txtAppVersion.text = "App Version: " + getAppVersion(mContext!!)
        navigateToStartPage()
    }

    private fun navigateToStartPage() {
        val handler = Handler()
        handler.postDelayed({ navigate() }, AppConstant.SPLASH_TIME)
    }

    private fun navigate() {
        Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSplash_to_fragmentWelcome)
    }
}