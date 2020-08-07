package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
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
import com.tugoapp.mobile.ui.home.adapters.CategoryListAdapter
import com.tugoapp.mobile.ui.home.adapters.SearchHomeListAdapter
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_browse_all_providers.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class FragmentBrowseAllProviders : BaseFragment<HomeViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_browse_all_providers

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
        initObservers()
    }

    private fun iniUI() {
        mContext = context
        mViewModel?.doLoadCategory();

        mViewModel?.doLoadProviders(GetProvidersRequestModel(null,null,null));

        imgCustomize.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentBrowseAllProviders_to_fragmentCustomizePlan)
        })
    }

    private fun initObservers() {

        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { CommonUtils.showSnakeBar(rootView!!,it)})

        mViewModel?.mCategoryData?.observe(viewLifecycleOwner, Observer { it ->
            if (it != null && it.size > 0) {
                rvCategoryList.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                val categoryData = ArrayList<CategoryDetailModel>()
                categoryData.add(CategoryDetailModel("0","All",null,true))
                categoryData.addAll(it)

                val adapter = mContext?.let {
                    CategoryListAdapter(it, categoryData, object : OnListItemClickListener {
                        override fun onListItemClick(position: Int) {
                            if(position == 0) {
                                mViewModel?.doLoadProviders(GetProvidersRequestModel(null,null,null))
                            } else {
                                mViewModel?.doLoadProviders(GetProvidersRequestModel(null,null,categoryData.get(position).categoryId));
                            }
                        }
                    })
                }
                rvCategoryList.adapter = adapter
            } else {
                CommonUtils.showSnakeBar(rootView!!, getString(R.string.txt_err_no_category_data_found))
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
                            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentBrowseAllProviders_to_fragmentProviderDetails)
                        }
                    })
                }
                rvMealProviders.adapter = adapter
            } else {
                CommonUtils.showSnakeBar(rootView!!, getString(R.string.txt_err_no_category_data_found))
            }
        })

    }
}