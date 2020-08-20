package com.tugoapp.mobile.ui.orders

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.RootActivity
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_orders.*
import javax.inject.Inject
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.tugoapp.mobile.ui.home.FragmentHome
import com.tugoapp.mobile.utils.AppConstant
import kotlinx.android.synthetic.main.fragment_orders.view.*


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

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context

        class ViewPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            public override fun getItem(position: Int): Fragment {
                return return when (position) {
                    0 -> FragmentOnGoingOrders()
                    1 -> FragmentHistoryOrders()
                    else -> FragmentOnGoingOrders()
                }
            }

            public override fun getCount(): Int {
                return 2
            }

            public override fun getPageTitle(position: Int): CharSequence? {
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
        activity?.let {
            mViewModel?.mSelectedHistoryOrder?.observe(it, Observer {
                var isHistoryTab : Boolean = orderViewPager.tabLayout.selectedTabPosition == 1
                var bundle = bundleOf(AppConstant.SELECTED_ORDER_FOR_DETAIL to it, AppConstant.SELECTED_ORDER_IS_HISTORY to isHistoryTab)
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentOrders_to_fragmentOrderDetail,bundle)
            })
        }

        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { CommonUtils.showSnakeBar(rootView!!,it)})

        mViewModel?.mShowProgress?.observe(viewLifecycleOwner, Observer {
            if(it.first) {
                if(it.second.isNullOrBlank()) {
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
