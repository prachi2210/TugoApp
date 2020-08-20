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
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.fragment_history_orders.*
import kotlinx.android.synthetic.main.fragment_ongoing_orders.*
import java.util.ArrayList
import javax.inject.Inject


class FragmentHistoryOrders : Fragment() {
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
        return  inflater.inflate(R.layout.fragment_history_orders, container, false)
    }

    override fun onResume() {
        super.onResume()
        iniUI()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun iniUI() {
        mContext = context

        if(mViewModel?.mHistoryOrderData?.value == null)
             mViewModel?.doGetHistoryOrders()
        else {
            doSetHistoryData(mViewModel?.mHistoryOrderData?.value!!)
        }

        initObserver()
        initController()
    }

    private fun initController() {
        btnExploreFoodHistory.setOnClickListener(View.OnClickListener {
            mViewModel?.doExploreFood()
        })
    }

    private fun initObserver() {

        mViewModel?.mHistoryOrderData?.observe(viewLifecycleOwner, Observer {
           doSetHistoryData(it)
        })
    }

    private fun doSetHistoryData(it: ArrayList<OrderModel>?) {
        historyOrderList.layoutManager = LinearLayoutManager(mContext)
        val data = ArrayList<OrderModel>()
        if(it != null && it.size > 0) {
            data.addAll(it)
            llOrdersEmpty.visibility = View.GONE
            historyOrderList.visibility = View.VISIBLE
        } else {
            llOrdersEmpty.visibility = View.VISIBLE
            historyOrderList.visibility = View.GONE
        }
        val adapter = mContext?.let {
            OrderHistoryListAdapter(it, true , data, object : OnListItemClickListener {
                override fun onListItemClick(position: Int) {
                    mViewModel?.setSelectedHistoryOrder(data[position])
                }
            })
        }
        historyOrderList.adapter = adapter
    }
}