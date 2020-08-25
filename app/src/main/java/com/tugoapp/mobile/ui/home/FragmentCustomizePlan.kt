package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.GetFilterProviderRequestModel
import com.tugoapp.mobile.data.remote.model.response.CustomizeListModel
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
    private var mLocation :ArrayList<String> ? = ArrayList()
    private var mNoOfmeals :ArrayList<String> ? = ArrayList()

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

        rangeSeekBar.setOnRangeSeekbarChangeListener(OnRangeSeekbarChangeListener { minValue, maxValue ->
            mMaxRange = maxValue.toString()
            mMinRange = minValue.toString()
            txtMin.text = String.format(getString(R.string.txt_min), mMinRange)
            txtMax.text = String.format(getString(R.string.txt_max), mMaxRange)
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
                it.minimumPrice?.toFloat()?.let { it1 -> rangeSeekBar.setMinValue(it1) }
                it.maximumPrice?.toFloat()?.let { it1 -> rangeSeekBar.setMaxValue(it1) }
                txtMin.text = String.format(getString(R.string.txt_min), it.minimumPrice)
                txtMax.text = String.format(getString(R.string.txt_max), it.maximumPrice)

                if (it.numOfMeals != null && it.numOfMeals?.size!! > 0) {
                    rvMinimalMealsList.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

                    val dataMinMeal = ArrayList<CustomizeListModel>()
                    for(data in it.numOfMeals!!) {
                        dataMinMeal.add(CustomizeListModel(data,false))
                    }

                    val dataMinMealAdapter = mContext?.let {
                        CustomizeListAdapter(it, dataMinMeal, object : OnListItemClickListener {
                            override fun onListItemClick(position: Int) {
                                if(dataMinMeal[position].isSelected) {
                                    dataMinMeal[position].value?.let { it1 -> mNoOfmeals?.add(it1) }
                                } else {
                                    mNoOfmeals?.remove(dataMinMeal[position].value)
                                }
                            }
                        })
                    }
                    rvMinimalMealsList.adapter = dataMinMealAdapter
                }

                if (it.allLocations != null && it.allLocations?.size!! > 0) {
                    rvDeliversTo.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                    val dataModels = ArrayList<CustomizeListModel>()
                    for(data in it.allLocations!!) {
                        dataModels.add(CustomizeListModel(data,false))
                    }

                    val adapter = mContext?.let {
                        CustomizeListAdapter(it, dataModels, object : OnListItemClickListener {
                            override fun onListItemClick(position: Int) {
                                if(dataModels[position].isSelected) {
                                    dataModels[position].value?.let { it1 -> mLocation?.add(it1) }
                                } else {
                                    mLocation?.remove(dataModels[position].value)
                                }
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

