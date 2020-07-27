package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_manage_address.*
import kotlinx.android.synthetic.main.fragment_payment_methods.*
import javax.inject.Inject

class FragmentPaymentMethods : BaseFragment<PaymentMethodsViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: PaymentMethodsViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_payment_methods

    override val viewModel: PaymentMethodsViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(PaymentMethodsViewModel::class.java)
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
        llAddCard.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentPaymentMethods_to_fragmentAddPaymentMethod)
        })
    }
}