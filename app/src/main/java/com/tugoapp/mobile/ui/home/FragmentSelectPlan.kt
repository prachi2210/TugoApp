package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.MealOptionsModel
import com.tugoapp.mobile.data.remote.model.response.MealPlanModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.home.adapters.MealOptionsListAdapter
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_select_plan.*
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import javax.inject.Inject


class FragmentSelectPlan : BaseFragment<HomeViewModel?>() {
    private var mSelectedDay: Int = 0
    private var mSelectedWeek: Int = 0

    private var mSelectedPlanObject: MealPlanModel? = null
    private var mSelectedMealPlan: MealOptionsModel? = null


    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    lateinit var mContext: Context

    override val layoutId: Int
        get() = R.layout.fragment_select_plan

    override val viewModel: HomeViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
            return mViewModel!!
        }

    override val screenTitle: String
        get() = getString(R.string.txt_select_plan)

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = this!!.requireContext()

        mSelectedPlanObject = arguments?.getParcelable(AppConstant.SELECTED_MEAL_PLAN)

        if (mSelectedPlanObject == null) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }

        doSetPlanData()
        initControls()
    }

    private fun initControls() {
        btnSelectPlan.setOnClickListener(View.OnClickListener {
            if(mSelectedMealPlan == null || mSelectedMealPlan?.mealId.isNullOrBlank()) {
                CommonUtils.showSnakeBar(rootView,getString(R.string.err_select_plan))
                return@OnClickListener
            }
            var bundle = bundleOf(AppConstant.SELECTED_PLAN_OBJECT to mSelectedPlanObject,
                    AppConstant.SELECTED_MEAL_PLAN to mSelectedMealPlan, AppConstant.SELECTED_MEAL_PLAN_TRIAL to false)
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSelectPlan_to_fragmentDeliveryDetail,bundle)
        })

        btnTryNow.setOnClickListener(View.OnClickListener {
//            var bundle = bundleOf(AppConstant.SELECTED_PLAN_OBJECT to mSelectedPlanObject, AppConstant.SELECTED_MEAL_PLAN_TRIAL to true)
//            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSelectPlan_to_fragmentDeliveryDetail,bundle)
        })
    }

    private fun doSetPlanData() {
        val list = mutableListOf<CarouselItem>()

        for(sample in mSelectedPlanObject?.sampleMenu!!) {
            list.add(
                    CarouselItem(
                            imageUrl = sample.imagePath,
                            caption = sample.title
                    )
            )
        }
        imageCarousel.addData(list)

        txtPlanName.text = mSelectedPlanObject?.title
        deliveryDaysSelectPlan.text = mSelectedPlanObject?.deliveryDays
        txtPlanDescSelectPlan.text = mSelectedPlanObject?.description
        if(mSelectedPlanObject?.isTrialPlanAvailable!!) {
            llTrialMeal.visibility = View.VISIBLE
            txtTrialPlanData.text = mSelectedPlanObject?.trailPlanMainDescription
        } else {
            llTrialMeal.visibility = View.GONE
        }

        if (mSelectedPlanObject?.mealOptions != null && mSelectedPlanObject?.mealOptions?.size!! > 0) {
            rvMealOptions.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            val data = ArrayList<MealOptionsModel>()
            data.addAll(mSelectedPlanObject?.mealOptions!!)

            val adapter = mContext?.let {
                MealOptionsListAdapter(it, data, object : OnListItemClickListener {
                    override fun onListItemClick(position: Int) {
                        mSelectedMealPlan = mSelectedPlanObject?.mealOptions!![position]
                    }
                })
            }
            rvMealOptions.adapter = adapter
        }
    }
}

