package com.tugoapp.mobile.ui.home

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.orders.OrderHistoryListAdapter
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class FragmentHome : BaseFragment<HomeViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_home

    override val viewModel: HomeViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
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

        rvBrowseByDiet.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
        val data = ArrayList<Int>()
        data.add(R.drawable.ic_bbf_1)
        data.add(R.drawable.ic_bbf_2)
        data.add(R.drawable.ic_bbf_1)
        data.add(R.drawable.ic_bbf_2)
        data.add(R.drawable.ic_bbf_1)
        data.add(R.drawable.ic_bbf_2)


        val adapter = mContext?.let {
            BrowseByDietListAdapter(it, data, object : OnListItemClickListener {
                override fun onListItemClick(position: Int) {
                    CommonUtils.showToast(mContext, "Clicked:" + position)
                }
            })
        }
        rvBrowseByDiet.adapter = adapter
    }
}