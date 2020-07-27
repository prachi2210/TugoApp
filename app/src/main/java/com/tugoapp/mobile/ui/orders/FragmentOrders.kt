package com.tugoapp.mobile.ui.orders

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_orders.*
import javax.inject.Inject

class FragmentOrders : BaseFragment<OrdersViewModel?>() , FragmentHistoryOrders.OnOrderItemSelectedListener {
    private lateinit var mTabsAdapter: TabsAdapter


    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: OrdersViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_orders

    override val viewModel: OrdersViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(OrdersViewModel::class.java)
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
        mTabsAdapter = TabsAdapter(childFragmentManager)
        mTabsAdapter.addFragment(FragmentOnGoingOrders(), "Ongoing")
        mTabsAdapter.addFragment(FragmentHistoryOrders(), "History")
        orderViewPager.adapter = mTabsAdapter
        tabLayout.setupWithViewPager(orderViewPager)
    }

    override fun onOrderSelectionSet(data: String) {
        CommonUtils.showToast(mContext,"coming soon.")
       // Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentOrders_to_fragmentOrderDetail)
    }
}