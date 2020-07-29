package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.home.adapters.CategoryListAdapter
import com.tugoapp.mobile.ui.home.adapters.SearchHomeListAdapter
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_browse_all_providers.*
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
    }

    private fun iniUI() {
        mContext = context

        rvCategoryList.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)

        val data = ArrayList<String>()
        data.add("All")
        data.add("Vegetarian")
        data.add("Keto")
        data.add("Vegan")
        data.add("Plant Based")
        data.add("Test1")
        data.add("Test2")
        data.add("Test3")
        data.add("Test4")
        val adapter = mContext?.let {
            CategoryListAdapter(it, data, object : OnListItemClickListener {
                override fun onListItemClick(position: Int) {
                    CommonUtils.showToast(mContext, "Clicked:" + position)
                }
            })
        }
        rvCategoryList.adapter = adapter

        rvMealProviders.layoutManager =  LinearLayoutManager(mContext)
        val mealData = ArrayList<Int>()
        mealData.add(R.drawable.ic_search_image)
        mealData.add(R.drawable.ic_search_image)
        mealData.add(R.drawable.ic_search_image)
        mealData.add(R.drawable.ic_search_image)
        mealData.add(R.drawable.ic_search_image)
        mealData.add(R.drawable.ic_search_image)


        val mealDataAdapter = mContext?.let {
            SearchHomeListAdapter(it, mealData, object : OnListItemClickListener {
                override fun onListItemClick(position: Int) {
                    Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentBrowseAllProviders_to_fragmentProviderDetails)
                }
            })
        }
        rvMealProviders.adapter = mealDataAdapter

        imgCustomize.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentBrowseAllProviders_to_fragmentCustomizePlan)
        })
    }
}