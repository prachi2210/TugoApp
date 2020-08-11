package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import javax.inject.Inject

class FragmentAddPaymentMethod : BaseFragment<AddPaymentMethodViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: AddPaymentMethodViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_add_payment_methods

    override val viewModel: AddPaymentMethodViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(AddPaymentMethodViewModel::class.java)
            return mViewModel!!
        }

    override val screenTitle: String
        get() = getString(R.string.txt_add_a_new_card)

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context
    }
}