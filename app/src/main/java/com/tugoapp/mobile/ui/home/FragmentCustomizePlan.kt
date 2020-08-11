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
import com.tugoapp.mobile.ui.home.adapters.CustomizeListAdapter
import com.tugoapp.mobile.utils.CommonUtils
import it.sephiroth.android.library.rangeseekbar.RangeSeekBar
import it.sephiroth.android.library.rangeseekbar.RangeSeekBar.OnRangeSeekBarChangeListener
import kotlinx.android.synthetic.main.fragment_customize_plan.*
import javax.inject.Inject


class FragmentCustomizePlan : BaseFragment<HomeViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_customize_plan

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
    }

    private fun iniUI() {
        mContext = context

        rangeSeekBar.setOnRangeSeekBarChangeListener(object : OnRangeSeekBarChangeListener {
            override fun onProgressChanged(seekBar: RangeSeekBar, progressStart: Int, progressEnd: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: RangeSeekBar) {}
            override fun onStopTrackingTouch(seekBar: RangeSeekBar) {}
        })

        rvDeliversTo.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)

        val data = ArrayList<String>()
        data.add("All Cities")
        data.add("Dubai")
        data.add("Sarjah")

        val adapter = mContext?.let {
            CustomizeListAdapter(it, data, object : OnListItemClickListener {
                override fun onListItemClick(position: Int) {
                    CommonUtils.showToast(mContext, "Clicked:" + position)
                }
            })
        }
        rvDeliversTo.adapter = adapter

        rvMinimalMealsList.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)

        val dataMinMeal = ArrayList<String>()
        dataMinMeal.add("1")
        dataMinMeal.add("2")
        dataMinMeal.add("3")
        dataMinMeal.add("4")
        dataMinMeal.add("5")
        dataMinMeal.add("6")

        val dataMinMealAdapter = mContext?.let {
            CustomizeListAdapter(it, dataMinMeal, object : OnListItemClickListener {
                override fun onListItemClick(position: Int) {
                    CommonUtils.showToast(mContext, "Clicked:" + position)
                }
            })
        }
        rvMinimalMealsList.adapter = dataMinMealAdapter

        imgClose.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).popBackStack() })

        btnUpdate.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).popBackStack() })
    }
}

