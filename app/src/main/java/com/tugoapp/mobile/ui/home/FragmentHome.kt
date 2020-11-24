package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.GetProvidersRequestModel
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.data.remote.model.response.ProviderModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.home.adapters.BrowseByDietListAdapter
import com.tugoapp.mobile.ui.home.adapters.SearchHomeListAdapter
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import com.tugoapp.mobile.utils.SharedPrefsUtils
import kotlinx.android.synthetic.main.fragment_browse_all_providers.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar1.view.*
import javax.inject.Inject

class FragmentHome : BaseFragment<HomeViewModel?>(), androidx.appcompat.widget.SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {
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
    override val screenTitle: String
        get() = ""

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()

        initObservers()
    }

    private fun initObservers() {

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

            mViewModel?.mCategoryData?.observe(viewLifecycleOwner, Observer { it ->
                if (it != null && it.size > 0) {
                    rvBrowseByDiet.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                    val categoryData = ArrayList<CategoryDetailModel>()
                    categoryData.addAll(it)

                    val adapter = mContext?.let {
                        BrowseByDietListAdapter(it, categoryData, object : OnListItemClickListener {
                            override fun onListItemClick(position: Int) {
                                var bundle = bundleOf(AppConstant.SELECTED_CATEGORY_FOR_PROVIDERS to position,
                                        AppConstant.ALL_CATEGORY_FOR_PROVIDERS to categoryData)
                                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentHome_to_fragmentBrowseAllProviders, bundle)
                            }
                        })
                    }
                    rvBrowseByDiet.adapter = adapter
                } else {
                    CommonUtils.showSnakeBar(rootView!!, getString(R.string.txt_err_no_category_data_found))
                }
            })

            mViewModel?.mSearchedProvidersData?.observe(viewLifecycleOwner, Observer { it ->
                // if (it != null && it.size > 0) {
                rvBrowseAllProviders.layoutManager = LinearLayoutManager(mContext)
                val data = ArrayList<ProviderModel>()
                if (it != null && it.size > 0) {
                    data.addAll(it)
                    llEmptyViewProvider.visibility = View.GONE
                    rvBrowseAllProviders.visibility = View.VISIBLE
                } else {
                    llEmptyViewProvider.visibility = View.VISIBLE
                    rvBrowseAllProviders.visibility = View.GONE
                }
                val adapter = mContext?.let {
                    SearchHomeListAdapter(it, data, object : OnListItemClickListener {
                        override fun onListItemClick(position: Int) {
                            var bundle = bundleOf(AppConstant.SELECTED_PROVIDER_FOR_PROVIDER_DETAIL to data[position].businessId)
                            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentHome_to_fragmentProviderDetails, bundle)
                        }
                    })
                }
                rvBrowseAllProviders.adapter = adapter
                //  } else {
                // CommonUtils.showSnakeBar(rootView!!, getString(R.string.txt_err_no_category_data_found))
                //  }
            })
    }

    private fun iniUI() {
        mContext = context
        searchHome.setOnQueryTextListener(this)

        if(mViewModel?.mCategoryData?.value == null)
        mViewModel?.doLoadCategory();

        searchHome.clearFocus()

        toolbarBrowseAllProvider?.imgBack?.visibility = View.INVISIBLE

        llLetsFindMealPlan.setOnClickListener(View.OnClickListener {
            var bundle = bundleOf(AppConstant.SELECTED_CATEGORY_FOR_PROVIDERS to -1,
                    AppConstant.ALL_CATEGORY_FOR_PROVIDERS to null)
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentHome_to_fragmentBrowseAllProviders,bundle)
        })

        val adapter = ArrayAdapter.createFromResource(
                mContext,
                R.array.locations,
                android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.item_location)
        locationSpinner.adapter = adapter
        locationSpinner.onItemSelectedListener = this

        var currentSelectedLocation = SharedPrefsUtils.getStringPreference(mContext,AppConstant.PREF_KEY_SELECTED_LOCATION)
        if(!currentSelectedLocation.isNullOrEmpty()) {
            locationSpinner.setSelection(adapter.getPosition(currentSelectedLocation))
        } else {
            locationSpinner.setSelection(0)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false;
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText.isNullOrBlank())  {
            llMainView.visibility = View.VISIBLE
            rvBrowseAllProviders.visibility = View.GONE
            llEmptyViewProvider.visibility = View.GONE
        } else {
            llMainView.visibility = View.GONE
            rvBrowseAllProviders.visibility = View.VISIBLE
            llEmptyViewProvider.visibility = View.GONE
            mViewModel?.doSearchTerm(GetProvidersRequestModel(null,null,null,newText))
        }
        return false;
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        SharedPrefsUtils.setStringPreference(mContext,AppConstant.PREF_KEY_SELECTED_LOCATION,locationSpinner.selectedItem.toString())
    }
}

