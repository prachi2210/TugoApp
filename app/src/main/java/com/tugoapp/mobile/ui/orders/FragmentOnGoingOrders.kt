package com.tugoapp.mobile.ui.orders

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.OrderModel
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.fragment_ongoing_orders.*
import javax.inject.Inject

class FragmentOnGoingOrders : Fragment() {

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: OrdersViewModel? = null
    var mContext: Context? = null

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

        if(mViewModel?.mOngoingOrderData?.value == null)
            mViewModel?.doGetOngoingOrders()
        else {
            doSetOngoingData(mViewModel?.mOngoingOrderData?.value!!)
        }

        initObserver()

        initController()
    }

    private fun initController() {
        btnExploreFood.setOnClickListener(View.OnClickListener {
            mViewModel?.doExploreFood()
        })
    }

    private fun initObserver() {
        mViewModel?.mOngoingOrderData?.observe(viewLifecycleOwner, Observer {
            doSetOngoingData(it)
        })
    }

    private fun doSetOngoingData(it: ArrayList<OrderModel>?) {
        ongoingOrderList.layoutManager = LinearLayoutManager(mContext)
        val data = ArrayList<OrderModel>()
        if(it != null && it.size > 0) {
            data.addAll(it)
            llOngoingOrdersEmpty.visibility = View.GONE
            ongoingOrderList.visibility = View.VISIBLE
        } else {
            llOngoingOrdersEmpty.visibility = View.VISIBLE
            ongoingOrderList.visibility = View.GONE
        }
        val adapter = mContext?.let {
            OrderHistoryListAdapter(it, false, data, object : OnListItemClickListener {
                override fun onListItemClick(position: Int) {
                    mViewModel?.setSelectedHistoryOrder(data[position],false)
                }
            })
        }
        ongoingOrderList.adapter = adapter
    }

}