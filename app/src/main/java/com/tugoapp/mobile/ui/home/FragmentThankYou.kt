package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.MealOptionsModel
import com.tugoapp.mobile.data.remote.model.response.MealPlanModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_order_summary.view.*
import kotlinx.android.synthetic.main.fragment_provider_details.*
import kotlinx.android.synthetic.main.fragment_thank_you.*
import javax.inject.Inject

class FragmentThankYou : BaseFragment<HomeViewModel?>() {
    private var mIsTrialMeal: Boolean = false
    private var mSelectedMealPlan: MealOptionsModel? = null
    private var mPlanObject: MealPlanModel? = null

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    lateinit var mContext: Context

    override val layoutId: Int
        get() = R.layout.fragment_thank_you

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
        mContext = this!!.requireContext()

        initController()

        mSelectedMealPlan = arguments?.getParcelable(AppConstant.SELECTED_MEAL_PLAN)
        mPlanObject = arguments?.getParcelable(AppConstant.SELECTED_PLAN_OBJECT)
        txtDate.text  = arguments?.getString(AppConstant.START_DATE_FOR_THANKYOU)
        mIsTrialMeal = arguments?.getBoolean(AppConstant.SELECTED_MEAL_PLAN_TRIAL)!!

        if(mIsTrialMeal && (mPlanObject == null)) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        } else if (!mIsTrialMeal && (mPlanObject == null ||  mSelectedMealPlan == null)) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }

        txtPlanName.text = mPlanObject?.title
        if(mIsTrialMeal) {
            txtAmount.text = mPlanObject?.trailPlanPricing  +" AED"
            txtPlanDetail.text = mPlanObject?.trialPlanDays + " days "
            txtTotalPaid.text = mPlanObject?.trailPlanPricing +" AED"

        } else {
            txtAmount.text = mSelectedMealPlan?.price  +" AED"
            txtPlanDetail.text = mSelectedMealPlan?.noOfMeals + " meals X " + mSelectedMealPlan?.noOfDays + " days plan"
            txtTotalPaid.text = mSelectedMealPlan?.price +" AED"
        }

        Glide.with(mContext)
                .load(mPlanObject?.featuredImage)
                .circleCrop()
                .into(imgPlan)
    }

    private fun initController() {
        btnBackToHome.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentThankyou_to_fragmentHome)
        })

        llMessageUs.setOnClickListener(View.OnClickListener {
            CommonUtils.doSendMessageToWhatsApp(mContext,rootView)
        })

        txtViewPlan.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentThankyou_to_fragmentOrders)
        })
    }
}
