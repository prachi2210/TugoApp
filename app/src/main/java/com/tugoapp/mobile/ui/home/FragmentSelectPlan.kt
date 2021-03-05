package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.MealOptionsModel
import com.tugoapp.mobile.data.remote.model.response.MealPlanModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.home.adapters.CategoryListAdapter
import com.tugoapp.mobile.ui.home.adapters.MealOptionsListAdapter
import com.tugoapp.mobile.ui.home.adapters.MealOptionsNumbersAdapter
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.bottom_sheet_select_snacks.view.*
import kotlinx.android.synthetic.main.fragment_delivery_detail.*
import kotlinx.android.synthetic.main.fragment_order_summary.*
import kotlinx.android.synthetic.main.fragment_order_summary.view.*
import kotlinx.android.synthetic.main.fragment_select_plan.*
import kotlinx.android.synthetic.main.fragment_select_plan.txtPlanName
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import javax.inject.Inject


class FragmentSelectPlan : BaseFragment<HomeViewModel?>() {
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

            var maxSnack = mSelectedMealPlan?.maxSnack!!.toInt()
            if(maxSnack > 0) {
                doShowSnackPopup()
            } else {
               navigateToNextScreen()
            }
        })

        imgBackSelectPlan.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).popBackStack()
        })
    }

    private fun navigateToNextScreen() {
        var bundle = bundleOf(AppConstant.SELECTED_PLAN_OBJECT to mSelectedPlanObject,
                AppConstant.SELECTED_MEAL_PLAN to mSelectedMealPlan, AppConstant.SELECTED_MEAL_PLAN_TRIAL to false)
        Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSelectPlan_to_fragmentDeliveryDetail,bundle)
    }

    private fun doSetPlanData() {
        val list = mutableListOf<CarouselItem>()

        if (mSelectedPlanObject?.sampleMenu == null || mSelectedPlanObject?.sampleMenu!!.size <= 0) {
            list.add(
                    CarouselItem(
                            imageUrl = mSelectedPlanObject?.featuredImage,
                            caption = mSelectedPlanObject?.title
                    )
            )
        } else {
            for (sample in mSelectedPlanObject?.sampleMenu!!) {
                list.add(
                        CarouselItem(
                                imageUrl = sample.imagePath,
                                caption = sample.title
                        )
                )
            }
        }
        imageCarousel.addData(list)

        txtPlanName.text = mSelectedPlanObject?.title
        deliveryDaysSelectPlan.text = mSelectedPlanObject?.deliveryDays
        txtPlanDescSelectPlan.text = mSelectedPlanObject?.description

        if (mSelectedPlanObject != null && mSelectedPlanObject?.mealOptions != null && mSelectedPlanObject?.mealOptions?.size!! > 0) {

            rvMealOptionsNumbers.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            val dataOfMealNumbers = ArrayList<String>()
            mSelectedPlanObject?.mealOptions?.keys?.let { dataOfMealNumbers.addAll(it) }

            val adapterOfMealNumbers = mContext?.let {
                MealOptionsNumbersAdapter(it, dataOfMealNumbers, object : OnListItemClickListener {
                    override fun onListItemClick(position: Int) {
                        doRefreshPlanList(mSelectedPlanObject?.mealOptions!![dataOfMealNumbers[position]])
                    }
                },0)
            }
            rvMealOptionsNumbers.adapter = adapterOfMealNumbers
        }
    }

    private fun doRefreshPlanList(planData: ArrayList<MealOptionsModel>?) {
        rvMealOptions.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        val data = ArrayList<MealOptionsModel>()
        planData?.let { data.addAll(it) }

        val adapter = mContext?.let {
            MealOptionsListAdapter(it, data, object : OnListItemClickListener {
                override fun onListItemClick(position: Int) {
                    mSelectedMealPlan = planData?.get(position)
                    val maxSnack = mSelectedMealPlan?.maxSnack!!.toInt()
                    if(maxSnack > 0) {
                        btnSelectPlan.text = getString(R.string.txt_select_snack)
                    } else {
                        btnSelectPlan.text = getString(R.string.txt_continue)
                    }
                }
            })
        }
        rvMealOptions.adapter = adapter
    }

    private fun doShowSnackPopup() {
        val view: View = layoutInflater.inflate(R.layout.bottom_sheet_select_snacks, null)
        val dialog = mContext.let { it1 -> BottomSheetDialog(it1) }
        dialog.setContentView(view)
        dialog.show()
        val maxSnack = mSelectedMealPlan?.maxSnack!!.toInt()
        var numOfSnack = 1
        view.txtSnackTitle.text = mSelectedMealPlan?.pricePerSnack + "AED"
        view.txtPlanDetailSnack.text = "for " + mSelectedMealPlan?.noOfDays + " days plan"
        view.txtSnackTotalAmount.text = mSelectedMealPlan?.noOfDays?.toInt()?.let { (mSelectedMealPlan?.pricePerSnack?.toInt()?.times(numOfSnack))?.times(it).toString() } + " AED"
        view.txtPlus.setOnClickListener {
            if(numOfSnack < maxSnack) {
                numOfSnack++
                view.noOfSnack.text = numOfSnack.toString()
                view.txtSnackTotalAmount.text = mSelectedMealPlan?.noOfDays?.toInt()?.let { (mSelectedMealPlan?.pricePerSnack?.toInt()?.times(numOfSnack))?.times(it).toString() } + " AED"
            }
        }

        view.txtMinus.setOnClickListener {
            if(numOfSnack > 1) {
                numOfSnack--
                view.noOfSnack.text = numOfSnack.toString()
                view.txtSnackTotalAmount.text = mSelectedMealPlan?.noOfDays?.toInt()?.let { (mSelectedMealPlan?.pricePerSnack?.toInt()?.times(numOfSnack))?.times(it).toString() } + " AED"
            }
        }

        view.btnSnackContinue.setOnClickListener(View.OnClickListener {
            mSelectedMealPlan?.snackQty = numOfSnack.toString()
            dialog.dismiss()
            navigateToNextScreen()
        })
    }
}

