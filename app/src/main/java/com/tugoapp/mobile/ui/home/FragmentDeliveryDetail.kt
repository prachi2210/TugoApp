package com.tugoapp.mobile.ui.home

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.MealPlanModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.home.adapters.OnPayButtonClick
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_delivery_detail.*
import kotlinx.android.synthetic.main.fragment_order_summary.*
import kotlinx.android.synthetic.main.fragment_order_summary.view.*
import kotlinx.android.synthetic.main.fragment_select_payment_method.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class FragmentDeliveryDetail : BaseFragment<HomeViewModel?>(), OnPayButtonClick {
    private var mSelectedMealPlan: MealPlanModel? = null
    private var mCalender: Calendar = Calendar.getInstance()

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_delivery_detail

    override val viewModel: HomeViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
            return mViewModel!!
        }

    override val screenTitle: String
        get() = getString(R.string.title_delivery)

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context

        mSelectedMealPlan = arguments?.getParcelable(AppConstant.SELECTED_MEAL_PLAN)

        if (mSelectedMealPlan == null) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }

        txtAvailableDeliveryTime.text = mSelectedMealPlan?.startTime + " - " + mSelectedMealPlan?.endTime
        txtDuration.text = String.format(getString(R.string.txt_duration_days),Integer.parseInt(mSelectedMealPlan?.noOfWeeks)*7)
        mSelectedMealPlan?.address = "230, Baker Road, Chicago, IL, 369852, USA"
        mCalender.timeInMillis = System.currentTimeMillis()
        updateLabel()
        llDeliveryStartDate.setOnClickListener(View.OnClickListener {
           var dialog = DatePickerDialog(mContext, onDateSelectedEvent, mCalender[Calendar.YEAR], mCalender[Calendar.MONTH], mCalender[Calendar.DAY_OF_MONTH])
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dialog.show()
        })

        btnOrderSummary.setOnClickListener(View.OnClickListener {
            val view: View = layoutInflater.inflate(R.layout.fragment_order_summary, null)
            mSelectedMealPlan?.instructions = txtDeliveryInstructions?.text.toString()
            doSetOrderSummary(view)

            val dialog = mContext?.let { it1 -> BottomSheetDialog(it1) }
            dialog?.setContentView(view)
            dialog?.show()

            view?.txtEdit.setOnClickListener(View.OnClickListener {
                dialog?.hide()
                doPopBackStack()
            })

            dialog?.btnChoosePaymentMethod?.setOnClickListener(View.OnClickListener {

                dialog?.hide()
                val view1: View = layoutInflater.inflate(R.layout.fragment_select_payment_method, null)

                val dialog1 = mContext?.let { it1 -> BottomSheetDialog(it1) }
                dialog1?.setContentView(view1)
                dialog1?.show()

                view1.btnPay.setOnClickListener(View.OnClickListener {
                    dialog1?.hide()
                    doNavigate();
                })

            })

        })

    }

    private fun doSetOrderSummary(view: View) {
        view.txtPlanName?.text = mSelectedMealPlan?.title
        view.txtPlanAmount?.text = mSelectedMealPlan?.startingFrom
        view.txtPlanDetail?.text = mSelectedMealPlan?.description
        view.txtPlanSub?.text = mSelectedMealPlan?.noOfMeals + " meals per day for " + Integer.parseInt(mSelectedMealPlan?.noOfWeeks) * 7 + " days"
        view.txtLocation?.text = mSelectedMealPlan?.address
        view?.txtDeliveryTime.text = mSelectedMealPlan?.startTime + " - " + mSelectedMealPlan?.endTime
        view?.txtInstructions.text = mSelectedMealPlan?.instructions
        view?.txtPlanTotalAmount.text = mSelectedMealPlan?.price
    }

    private fun doPopBackStack() {
        Navigation.findNavController(rootView!!).popBackStack()
    }

    var onDateSelectedEvent = OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
        mCalender = Calendar.getInstance()
        mCalender[Calendar.YEAR] = year
        mCalender[Calendar.MONTH] = monthOfYear
        mCalender[Calendar.DAY_OF_MONTH] = dayOfMonth
        updateLabel()
    }

    private fun updateLabel() {
        val apiFormat = "yyyy-MM-dd"
        val localFormat = "dd/MM/yyyy"

        val sdfApiFormat = SimpleDateFormat(apiFormat)
        val sdfLocalFormat = SimpleDateFormat(localFormat)

        txtStartdate.text = sdfLocalFormat.format(mCalender.time)
        mSelectedMealPlan?.startFrom = sdfApiFormat.format(mCalender.time)
        var calender : Calendar = mCalender.clone() as Calendar
        calender.add(Calendar.DATE, (Integer.parseInt(mSelectedMealPlan?.noOfWeeks) * 7))
        txtEnddate.text = String.format(getString(R.string.txt_end_date_is),sdfLocalFormat.format(calender.time))
        mSelectedMealPlan?.endOn = sdfApiFormat.format(calender.time)
    }

    private fun doNavigate() {
        var bundle = bundleOf(AppConstant.SELECTED_MEAL_PLAN to mSelectedMealPlan)
        Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentDeliveryDetail_to_fragmentThankYou, bundle)
    }

    override fun onPayClick(position: Int) {

    }
}

