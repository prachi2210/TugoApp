package com.tugoapp.mobile.ui.orders

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_orders.*
import javax.inject.Inject


class FragmentOrders : BaseFragment<OrdersViewModel?>() {
    private lateinit var mTabsAdapter: TabsAdapter

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: OrdersViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_orders

    override val screenTitle: String
        get() = getString(R.string.title_orders)

    override val viewModel: OrdersViewModel
        get() {
            mViewModel = activity?.let { ViewModelProviders.of(it, factory).get(OrdersViewModel::class.java) }
            return mViewModel!!
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context

        class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return return when (position) {
                    0 -> FragmentOnGoingOrders()
                    1 -> FragmentHistoryOrders()
                    else -> FragmentOnGoingOrders()
                }
            }

            override fun getCount(): Int {
                return 2
            }

            override fun getPageTitle(position: Int): CharSequence? {
                var title: String? = null
                if (position == 0) {
                    title = getString(R.string.txt_ongoing)
                } else if (position == 1) {
                    title = getString(R.string.txt_history)
                }
                return title
            }
        }
        orderViewPager.adapter = ViewPagerAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(orderViewPager)
        initObservers()
        tabLayout.getTabAt(tabLayout.selectedTabPosition)?.select()
    }

    private fun initObservers() {
        mViewModel?.mSelectedHistoryOrder?.observe(viewLifecycleOwner, Observer {
            var bundle = bundleOf(AppConstant.SELECTED_ORDER_FOR_DETAIL to it.second, AppConstant.SELECTED_ORDER_IS_HISTORY to it.first)
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentOrders_to_fragmentOrderDetail, bundle)
        })

        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { CommonUtils.showSnakeBar(rootView!!, it) })

        mViewModel?.mShowProgress?.observe(viewLifecycleOwner, Observer {
            if (it.first) {
                if (it.second.isNullOrBlank()) {
                    showLoading()
                } else {
                    showLoading(it.second)
                }
            } else {
                hideLoading()
            }
        })

        mViewModel?.mExploreFood?.observe(viewLifecycleOwner, Observer {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentOrders_to_fragmentHome)
        })
    }
}
