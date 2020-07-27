package com.tugoapp.mobile.ui.orders

import android.R.attr.fragment
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_history_orders.*
import javax.inject.Inject


class FragmentHistoryOrders : BaseFragment<HistoryOrdersViewModel?>() {
    public interface OnOrderItemSelectedListener {
        fun onOrderSelectionSet(data: String)
    }

    private lateinit var mOnPlayerSelectionSetListener: OnOrderItemSelectedListener

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HistoryOrdersViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_history_orders

    override val viewModel: HistoryOrdersViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(HistoryOrdersViewModel::class.java)
            return mViewModel!!
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            mOnPlayerSelectionSetListener = parentFragment as OnOrderItemSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$fragment must implement OnPlayerSelectionSetListener")
        }
    }

    override fun onResume() {
        super.onResume()
        iniUI()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            OrderHistoryListAdapter(it, users, object : OnCellClickListener {
                override fun onCellClickListener(position: Int) {
                    mOnPlayerSelectionSetListener.onOrderSelectionSet(users.get(position))
                }
            })
        }
        historyOrderList.adapter = adapter
    }
}