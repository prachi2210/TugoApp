package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.GetProvidersRequestModel
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.data.remote.model.response.ProviderModel
import com.tugoapp.mobile.ui.RootActivity
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.home.adapters.CategoryListAdapter
import com.tugoapp.mobile.ui.home.adapters.SearchHomeListAdapter
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_browse_all_providers.*
import kotlinx.android.synthetic.main.toolbar1.view.*
import javax.inject.Inject

class FragmentBrowseAllProviders : BaseFragment<HomeViewModel?>() {
    private var mCategoryList: java.util.ArrayList<CategoryDetailModel>? = null
    private var mSelectedCategory: Int = 0

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_browse_all_providers

    override val viewModel: HomeViewModel
        get() {
            mViewModel = activity?.let { ViewModelProviders.of(it, factory).get(HomeViewModel::class.java) }
            return mViewModel!!
        }
    override val screenTitle: String
        get() = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
        initObservers()
    }

    private fun iniUI() {
        mContext = context
        (activity as RootActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as RootActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbarBrowseAllProvider?.imgBack?.visibility = View.VISIBLE

        toolbarBrowseAllProvider?.imgBack?.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).popBackStack()
        })

        mSelectedCategory = arguments?.getInt(AppConstant.SELECTED_CATEGORY_FOR_PROVIDERS)!!
        mCategoryList = arguments?.getParcelableArrayList(AppConstant.ALL_CATEGORY_FOR_PROVIDERS)

        if (!(rvCategoryList.adapter != null && rvCategoryList.adapter?.itemCount != null && rvCategoryList.adapter?.itemCount!! > 0)) {
            if (mCategoryList.isNullOrEmpty()) {
                mViewModel?.doLoadCategory()
            } else {
                doSetCategoryData(mCategoryList)
            }
        }

        imgCustomize.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentBrowseAllProviders_to_fragmentCustomizePlan)
        })
    }

    private fun initObservers() {

        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { CommonUtils.showSnakeBar(rootView!!, it) })

        mViewModel?.mCategoryData?.observe(viewLifecycleOwner, Observer { it ->
            doSetCategoryData(it)
        })

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

        mViewModel?.mProvidersData?.observe(viewLifecycleOwner, Observer { it ->
            if (it != null && it.size > 0) {
                rvMealProviders.layoutManager = LinearLayoutManager(mContext)
                val data = ArrayList<ProviderModel>()
                data.addAll(it)
                val adapter = mContext?.let {
                    SearchHomeListAdapter(it, data, object : OnListItemClickListener {
                        override fun onListItemClick(position: Int) {
                            var bundle = bundleOf(AppConstant.SELECTED_PROVIDER_FOR_PROVIDER_DETAIL to data[position].businessId)
                            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentBrowseAllProviders_to_fragmentProviderDetails, bundle)
                        }
                    })
                }
                rvMealProviders.adapter = adapter
            } else {
                CommonUtils.showSnakeBar(rootView!!, getString(R.string.txt_err_no_providers_found))
            }
        })

    }

    private fun doSetCategoryData(data: java.util.ArrayList<CategoryDetailModel>?) {
        if (data != null && data.size > 0) {
            rvCategoryList.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            val categoryData = ArrayList<CategoryDetailModel>()
            categoryData.add(CategoryDetailModel("0", "All", null, null,true))
            categoryData.addAll(data)
            val adapter = mContext?.let {
                CategoryListAdapter(it, categoryData, object : OnListItemClickListener {
                    override fun onListItemClick(position: Int) {
                        if (position == 0) {
                            mViewModel?.doLoadProviders(GetProvidersRequestModel(null, null, null, null))
                        } else {
                            mViewModel?.doLoadProviders(GetProvidersRequestModel(null, null, categoryData.get(position).categoryId, null))
                        }
                    }
                }, mSelectedCategory + 1 )
            }
            rvCategoryList.adapter = adapter
        } else {
            CommonUtils.showSnakeBar(rootView!!, getString(R.string.txt_err_no_category_data_found))
        }
    }
}