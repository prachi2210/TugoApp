package com.tugoapp.mobile.ui.orders

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_orders.*
import javax.inject.Inject

class FragmentOrderDetail : BaseFragment<OrderDetailViewModel?>() {
    private lateinit var mTabsAdapter: TabsAdapter

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: OrderDetailViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_order_detail

    override val viewModel: OrderDetailViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(OrderDetailViewModel::class.java)
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
    }
}