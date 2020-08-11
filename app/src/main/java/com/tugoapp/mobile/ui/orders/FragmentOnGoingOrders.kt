package com.tugoapp.mobile.ui.orders

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import javax.inject.Inject

class FragmentOnGoingOrders : Fragment() {

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: OrdersViewModel? = null
    var mContext: Context? = null

    //    override val layoutId: Int
//        get() = R.layout.fragment_ongoing_orders
//
//
//    override val viewModel: OngoingOrdersViewModel
//        get() {
//            mViewModel = ViewModelProviders.of(this, factory).get(OngoingOrdersViewModel::class.java)
//            return mViewModel!!
//        }
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = activity?.let { ViewModelProviders.of(it, factory).get(OrdersViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ongoing_orders, container, false)
    }

    override fun onResume() {
        super.onResume()
        iniUI()
    }

    private fun iniUI() {
        mContext = context
    }

}