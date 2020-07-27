package com.tugoapp.mobile.ui.orders

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

class FragmentOnGoingOrders : BaseFragment<OngoingOrdersViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: OngoingOrdersViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_ongoing_orders

    override val viewModel: OngoingOrdersViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(OngoingOrdersViewModel::class.java)
            return mViewModel!!
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
    }


    private fun navigate() {
    }
}