package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.MealPlanModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_select_plan.*
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import javax.inject.Inject


class FragmentSelectPlan : BaseFragment<HomeViewModel?>() {
    private var mSelectedDay: Int = 0
    private var mSelectedWeek: Int = 0

    private var mSelectedMealPlan: MealPlanModel? = null

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

        mSelectedMealPlan = arguments?.getParcelable(AppConstant.SELECTED_MEAL_PLAN)

        if (mSelectedMealPlan == null) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }

        doSetPlanData()
        initControls()
    }

    private fun initControls() {
        btnSelectPlan.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSelectPlan_to_fragmentDeliveryDetail)
        })
    }

    private fun doSetPlanData() {
        val list = mutableListOf<CarouselItem>()

        for(sample in mSelectedMealPlan?.sampleMenu!!) {
            list.add(
                    CarouselItem(
                            imageUrl = sample.imagePath,
                            caption = sample.title
                    )
            )
        }
        imageCarousel.addData(list)

        txtPlanName.text = mSelectedMealPlan?.title
        txtPlanAmount.text = mSelectedMealPlan?.startingFrom + " AED "
        if(mSelectedMealPlan?.isTrialPlanAvailable!!) {
            llTrialMeal.visibility = View.VISIBLE
        } else {
            llTrialMeal.visibility = View.GONE
        }

        val adapterMeal = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, mSelectedMealPlan?.mealOptions)
        adapterMeal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSelectDay.adapter = adapterMeal

        val adapterWeek = ArrayAdapter(mContext, android.R.layout.simple_spinner_item, mSelectedMealPlan?.weekOptions)
        adapterWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSelectWeek.adapter = adapterWeek

        btnTryNow.setOnClickListener(View.OnClickListener {

        })

        spinnerSelectDay.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                mSelectedDay = Integer.parseInt(mSelectedMealPlan?.mealOptions?.get(position))
                doCalculateAndShowData()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }

        spinnerSelectWeek.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                mSelectedWeek = Integer.parseInt(mSelectedMealPlan?.weekOptions?.get(position))
                doCalculateAndShowData()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {

            }
        }
    }

    private fun doCalculateAndShowData() {
        txtYourPlanDetail.text = mSelectedDay.toString() + " meals * " + (mSelectedWeek*7).toString() + " days"

        when(mSelectedDay) {
            1 ->
                txtYourPlanPrice.text =(mSelectedDay * mSelectedWeek * 7 * Integer.parseInt(mSelectedMealPlan?.priceForOneMeal)).toString()
            2 ->
                txtYourPlanPrice.text =(mSelectedDay * mSelectedWeek * 7 * Integer.parseInt(mSelectedMealPlan?.priceForTwoMeals)).toString()
            3 ->
                txtYourPlanPrice.text =(mSelectedDay * mSelectedWeek * 7 * Integer.parseInt(mSelectedMealPlan?.priceForThreeMeals)).toString()
        }
    }
}

