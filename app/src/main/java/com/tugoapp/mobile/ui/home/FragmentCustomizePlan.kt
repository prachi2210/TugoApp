package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.GetFilterProviderRequestModel
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

    private var mIsTrialMeal : Boolean = false
    private var mMinRange :String ? = null
    private var mMaxRange :String ? = null
    private var mLocation :String ? = null
    private var mNoOfmeals :String ? = null

    override val layoutId: Int
        get() = R.layout.fragment_customize_plan

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
    }

    private fun iniUI() {
        mContext = context

        rangeSeekBar.setOnRangeSeekBarChangeListener(object : OnRangeSeekBarChangeListener {
            override fun onProgressChanged(seekBar: RangeSeekBar, progressStart: Int, progressEnd: Int, fromUser: Boolean) {
                mMaxRange = progressEnd.toString()
                mMinRange = progressStart.toString()
                txtMin.text = String.format(getString(R.string.txt_min), mMinRange)
                txtMax.text = String.format(getString(R.string.txt_max), mMaxRange)
            }

            override fun onStartTrackingTouch(seekBar: RangeSeekBar) {

            }
            override fun onStopTrackingTouch(seekBar: RangeSeekBar) {

            }
        })

        mViewModel?.doGetCustomFilterParameters()

        initControls()
        initObserver()
    }

    private fun initObserver() {
        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { CommonUtils.showSnakeBar(rootView!!, it) })

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

        mViewModel?.mCustomFilterData?.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                if (it.numOfMeals != null && it.numOfMeals?.size!! > 0) {
                    rvMinimalMealsList.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

                    val dataMinMeal = ArrayList<String>()
                    dataMinMeal.addAll(it.numOfMeals!!)

                    val dataMinMealAdapter = mContext?.let {
                        CustomizeListAdapter(it, dataMinMeal, object : OnListItemClickListener {
                            override fun onListItemClick(position: Int) {
                                mNoOfmeals = dataMinMeal[position]
                            }
                        })
                    }
                    rvMinimalMealsList.adapter = dataMinMealAdapter
                }

                if (it.allLocations != null && it.allLocations?.size!! > 0) {
                    rvDeliversTo.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                    val data = ArrayList<String>()
                    data.addAll(it.allLocations!!)
                    val adapter = mContext?.let {
                        CustomizeListAdapter(it, data, object : OnListItemClickListener {
                            override fun onListItemClick(position: Int) {
                                mLocation = data[position]
                            }
                        })
                    }
                    rvDeliversTo.adapter = adapter
                }
            }
        })
    }

    private fun initControls() {
        imgClose.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).popBackStack() })

        btnUpdate.setOnClickListener(View.OnClickListener {
            mViewModel?.doGetCustomFilterProviders(GetFilterProviderRequestModel(mMinRange,mMaxRange, mNoOfmeals, mLocation , mIsTrialMeal))
            Navigation.findNavController(rootView!!).popBackStack()
        })

        filterAvailable.setOnClickListener(View.OnClickListener {
            if(mIsTrialMeal) {
                mIsTrialMeal = false
                filterAvailable.setTextColor(resources.getColor(R.color.color999999))
            } else {
                mIsTrialMeal = true
                filterAvailable.setTextColor(resources.getColor(R.color.colorPrimary))
            }
        })
    }
}

