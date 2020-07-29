package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class FragmentHome : BaseFragment<HomeViewModel?>(), androidx.appcompat.widget.SearchView.OnQueryTextListener{
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
        searchHome.setOnQueryTextListener(this)
        rvBrowseByDiet.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
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

        llLetsFindMealPlan.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentHome_to_fragmentBrowseAllProviders)
        })
    }

    private fun doBrowseAllProviders() {
        llMainView.visibility = View.GONE

        rvBrowseAllProviders.layoutManager = LinearLayoutManager(mContext)
        val data = ArrayList<Int>()
        data.add(R.drawable.ic_search_image)
        data.add(R.drawable.ic_search_image)
        data.add(R.drawable.ic_search_image)
        data.add(R.drawable.ic_search_image)
        data.add(R.drawable.ic_search_image)
        data.add(R.drawable.ic_search_image)


        val adapter = mContext?.let {
            SearchHomeListAdapter(it, data, object : OnListItemClickListener {
                override fun onListItemClick(position: Int) {
                    CommonUtils.showToast(mContext, "Clicked:" + position)
                }
            })
        }
        rvBrowseAllProviders.adapter = adapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false;
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText.isNullOrBlank())  {
            llMainView.visibility = View.VISIBLE
            rvBrowseAllProviders.visibility = View.GONE
        } else {
            doBrowseAllProviders()
            llMainView.visibility = View.GONE
            rvBrowseAllProviders.visibility = View.VISIBLE
        }
        return false;
    }
}

