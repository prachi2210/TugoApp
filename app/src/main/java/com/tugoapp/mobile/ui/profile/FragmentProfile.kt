package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class FragmentProfile : BaseFragment<ProfileViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: ProfileViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_profile

    override val viewModel: ProfileViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
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

        llPersonalInfo.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentPersonalInformation) })

        llChangePswd.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentChangePswd) })

        llDeliveryAddress.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentManageAddress) })

        llPaymentMethods.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentPaymentMethods) })


    }
}