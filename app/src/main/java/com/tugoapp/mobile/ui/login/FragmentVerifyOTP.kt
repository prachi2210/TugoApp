package com.tugoapp.mobile.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_verify_otp.*
import javax.inject.Inject

class FragmentVerifyOTP : BaseFragment<VerifyOTPViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: VerifyOTPViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_verify_otp

    override val viewModel: VerifyOTPViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(VerifyOTPViewModel::class.java)
            return mViewModel!!
        }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context
        initControls()
    }

    private fun initControls() {
        btnOtpContinue.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentVerifyOTP_to_fragmentWalkthrough)
        })
    }
}