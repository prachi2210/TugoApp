package com.tugoapp.mobile.ui.orders

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_history_orders.*
import javax.inject.Inject


class FragmentHistoryOrders : BaseFragment<OrdersViewModel?>() {

    private lateinit var mOnOrderSelected: OnListItemClickListener

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: OrdersViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_history_orders

    override val viewModel: OrdersViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(OrdersViewModel::class.java)
            return mViewModel!!
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        try {
//            mOnOrderSelected = parentFragment as OnListItemClickListener
//        } catch (e: ClassCastException) {
//            throw ClassCastException(parentFragment.toString() + " must implement OnPlayerSelectionSetListener")
//        }
    }

    override fun onResume() {
        super.onResume()
        iniUI()
    }


    private fun iniUI() {
        mContext = context
        historyOrderList.layoutManager = LinearLayoutManager(mContext)
        val users = ArrayList<String>()
        users.add("t1")
        users.add("t1")
        users.add("t1")
        users.add("t1")
        users.add("t1")
        users.add("t1")
        users.add("t1")
        val adapter = mContext?.let {
            OrderHistoryListAdapter(it, users, object : OnListItemClickListener {
                override fun onListItemClick(position: Int) {
                    /*val fm: FragmentManager? = childFragmentManager
                    val fragm: FragmentOrders = fm?.findFragmentById(R.id.main_navigation) as FragmentOrders
                    fragm?.sendNavigation()*/

                   // FragmentOrders.click()
                   // Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentHistoryOrders_to_fragmentOrderDetail)
                }
            })
        }
        historyOrderList.adapter = adapter
    }
}