package com.tugoapp.mobile.ui.orders

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_orders.*
import javax.inject.Inject


class FragmentOrders : BaseFragment<OrdersViewModel?>(), OnListItemClickListener  {
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
        var controller = activity?.let { Navigation.findNavController(it,R.id.child_container) };

//        mTabsAdapter  = TabsAdapter(childFragmentManager)
//        mTabsAdapter.addFragment(FragmentOnGoingOrders(), "Ongoing")
//        mTabsAdapter.addFragment(FragmentHistoryOrders(), "History")
//        orderViewPager.adapter = mTabsAdapter
        tabLayout.setupWithViewPager(orderViewPager)

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> controller?.navigate(R.id.action_fragmentHistoryOrders_to_fragmentOnGoingOrders, null)
                    1 -> controller?.navigate(R.id.action_fragmentOnGoingOrders_to_fragmentHistoryOrders, null)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onListItemClick(position: Int) {
        Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentOrders_to_fragmentOrderDetail)
    }
}
