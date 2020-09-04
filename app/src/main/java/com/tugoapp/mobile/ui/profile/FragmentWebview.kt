package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.UserDetailModel
import com.tugoapp.mobile.ui.RootActivity
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import com.tugoapp.mobile.utils.NetworkUtils
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_webview.*
import javax.inject.Inject

class FragmentWebview : BaseFragment<ProfileViewModel?>() {
    private var mIsTermsCond: Boolean = false

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: ProfileViewModel? = null
    lateinit var mContext: Context

    override val layoutId: Int
        get() = R.layout.fragment_webview

    override val viewModel: ProfileViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
            return mViewModel!!
        }
    override val screenTitle: String
        get() = getString(R.string.title_account_settings)

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = this!!.requireContext()
        (activity as RootActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as RootActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mIsTermsCond = arguments?.getBoolean(AppConstant.IS_FROM_TERMS_COND)!!

        if(mIsTermsCond) {
            (activity as RootActivity).supportActionBar?.setTitle(getString(R.string.txt_terms_condition))
        } else {
            (activity as RootActivity).supportActionBar?.setTitle(getString(R.string.txt_privacy_policy))
        }

        initController()
    }

    private fun initController() {

        if(mIsTermsCond) {
            webview.loadUrl("https://tugo.ae/terms")
        } else {
            webview.loadUrl("https://tugo.ae/privacy")
        }
    }

}