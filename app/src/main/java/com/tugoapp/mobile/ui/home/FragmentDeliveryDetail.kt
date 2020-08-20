package com.tugoapp.mobile.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.AddAddressRequestModel
import com.tugoapp.mobile.data.remote.model.request.UpdateAddressRequestModel
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
import androidx.lifecycle.Observer
import com.tugoapp.mobile.data.remote.model.request.PlaceOrderObject
import com.tugoapp.mobile.data.remote.model.request.PlaceOrderRequestModel
import com.tugoapp.mobile.data.remote.model.response.MealOptionsModel


class FragmentDeliveryDetail : BaseFragment<HomeViewModel?>(), OnPayButtonClick {
    private var mIsTrialMeal: Boolean = false
    private var mPlanObject: MealPlanModel? = null
    private var mSelectedMealPlan: MealOptionsModel? = null

    private var mPlaceOrderRequestModel : PlaceOrderObject? = null
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

        mPlanObject = arguments?.getParcelable(AppConstant.SELECTED_PLAN_OBJECT)
        mSelectedMealPlan = arguments?.getParcelable(AppConstant.SELECTED_MEAL_PLAN)
        mIsTrialMeal = arguments?.getBoolean(AppConstant.SELECTED_MEAL_PLAN_TRIAL)!!

        if (mPlanObject == null || (!mIsTrialMeal && mSelectedMealPlan == null)) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }

        mPlaceOrderRequestModel = PlaceOrderObject
        mPlaceOrderRequestModel?.planObj = mPlanObject as MealPlanModel
        mPlaceOrderRequestModel?.mealId = mSelectedMealPlan?.mealId.toString()
        mPlaceOrderRequestModel?.isTrialPlan = mIsTrialMeal
        mPlaceOrderRequestModel?.noOfMeals = mSelectedMealPlan?.noOfMeals
        mPlaceOrderRequestModel?.noOfWeeks = mSelectedMealPlan?.weeks
        mPlaceOrderRequestModel?.deliveryTime =  mPlanObject?.startTime + " - " + mPlanObject?.endTime
        mPlaceOrderRequestModel?.deliveryLocation = mPlanObject?.locations
        mPlaceOrderRequestModel?.planId = mPlanObject?.planId
        mPlaceOrderRequestModel?.address = mPlanObject?.defaultUserAddress

        doInitDeliveryDetails()

        initController()

        initObserver()

    }

    private fun initObserver() {
        mViewModel?.mToastMessage?.observe(viewLifecycleOwner,  Observer { CommonUtils.showSnakeBar(rootView!!,it)})

        mViewModel?.mShowProgress?.observe(viewLifecycleOwner, Observer {
            if(it.first) {
                if(it.second.isNullOrBlank()) {
                    showLoading()
                } else {
                    showLoading(it.second)
                }
            } else {
                hideLoading()
            }
        })

        mViewModel?.mAddAddressData?.observe(viewLifecycleOwner, Observer {
            if(it.first == 1) {
                CommonUtils.showSnakeBar(rootView,"Address added successfully")
                mPlanObject?.addressId = it.second.first
                mPlanObject?.defaultUserAddress = it.second.second
                txtAddress.text = it.second.second
                btnEditAddress.visibility = View.VISIBLE
                btnAddAddressDeliveryScreen.visibility = View.GONE
                txtAddress.visibility = View.VISIBLE
            } else {
                CommonUtils.showSnakeBar(rootView,it.second.first)
            }
        })

        mViewModel?.mUpdateAddressData?.observe(viewLifecycleOwner, Observer {
            if(it.first == 1) {
                CommonUtils.showSnakeBar(rootView,"Address updated successfully")
                mPlanObject?.defaultUserAddress = it.second
                txtAddress.text = it.second
                btnEditAddress.visibility = View.VISIBLE
                btnAddAddressDeliveryScreen.visibility = View.GONE
                txtAddress.visibility = View.VISIBLE
            } else {
                CommonUtils.showSnakeBar(rootView,it.second)
            }
        })

        mViewModel?.mPlaceOrderResponse?.observe(viewLifecycleOwner, Observer {
            if(it.first == 1) {
                CommonUtils.showSnakeBar(rootView,it.second)
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentDeliveryDetail_to_fragmentThankYou)
            } else {
                CommonUtils.showSnakeBar(rootView,it.second)
            }
        })
    }

    private fun initController() {
        btnOrderSummary.setOnClickListener(View.OnClickListener {

            if(!doValidateDeliveryScreenData()) {
                return@OnClickListener;
            }

            val view: View = layoutInflater.inflate(R.layout.fragment_order_summary, null)
            mPlaceOrderRequestModel?.instructions = txtDeliveryInstructions?.text.toString()
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

    private fun doValidateDeliveryScreenData(): Boolean {
        if(txtAddress.text.toString().isNullOrBlank()) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.err_fill_address))
            return false
        }
        if(txtStartdate.text.toString().isNullOrBlank()) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.err_fill_date))
            return false
        }
     return true
    }

    private fun doInitDeliveryDetails() {
        txtAvailableDeliveryTime.text = mPlanObject?.startTime + " - " + mPlanObject?.endTime
        deliveryDays.text = mPlanObject?.deliveryDays
        txtDuration.text = String.format(getString(R.string.txt_duration_days),Integer.parseInt(mSelectedMealPlan?.noOfDays))

        mCalender.timeInMillis = System.currentTimeMillis()
        updateLabel()
        llDeliveryStartDate.setOnClickListener(View.OnClickListener {
            var dialog = DatePickerDialog(mContext, onDateSelectedEvent, mCalender[Calendar.YEAR], mCalender[Calendar.MONTH], mCalender[Calendar.DAY_OF_MONTH])
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dialog.show()
        })

        if(!mPlanObject?.defaultUserAddress.isNullOrBlank()) {
            txtAddress.text = mPlanObject?.defaultUserAddress
            btnEditAddress.visibility = View.VISIBLE
            btnAddAddressDeliveryScreen.visibility = View.GONE
            txtAddress.visibility = View.VISIBLE
        } else {
            btnAddAddressDeliveryScreen.visibility = View.VISIBLE
            txtAddress.visibility = View.GONE
            btnEditAddress.visibility = View.GONE
        }

        btnAddAddressDeliveryScreen.setOnClickListener(View.OnClickListener {
           doShowAddressDialog(true, "", "")
        })

        btnEditAddress.setOnClickListener(View.OnClickListener {
            doShowAddressDialog(false, mPlanObject?.defaultUserAddress, mPlanObject?.addressId)
        })
    }

    private fun doShowAddressDialog(isAddAddress: Boolean, address: String?, addressId: String?) {

        val li = LayoutInflater.from(context)
        val promptsView: View = li.inflate(R.layout.dialog_address, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(promptsView)
        alertDialogBuilder.setCancelable(false)
        var addressEditText = promptsView.findViewById<EditText>(R.id.dialogAddress)
        var btnCancel = promptsView.findViewById<AppCompatButton>(R.id.btnCancelAddressDialog)
        var btnAdd = promptsView.findViewById<AppCompatButton>(R.id.btnAddAddressDialog)
        if(isAddAddress) {
            btnAdd.text = getString(R.string.txt_add)
        } else {
            btnAdd.text = getString(R.string.txt_edit)
            addressEditText.setText(mPlanObject?.defaultUserAddress)
        }
        var dialog = alertDialogBuilder.create()

        btnCancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        btnAdd.setOnClickListener(View.OnClickListener {
            if(isAddAddress) {
                mViewModel?.doAddAddress(AddAddressRequestModel(addressEditText.text.toString(),true))
                dialog.dismiss()
            } else {
                mViewModel?.doUpdateAddressOnServer(UpdateAddressRequestModel(mPlanObject?.addressId,addressEditText.text.toString(),true))
                dialog.dismiss()
            }
        })

        dialog.show()
    }

    private fun doSetOrderSummary(view: View) {
        view.txtPlanName?.text = mPlanObject?.title
        view.txtPlanAmount?.text = mPlanObject?.startingFrom
        view.txtPlanDetail?.text = mPlanObject?.description
        view.txtPlanSub?.text = mSelectedMealPlan?.noOfMeals + " meals per day for " + Integer.parseInt(mSelectedMealPlan?.noOfDays) + " days"
        view.txtLocation?.text = mPlanObject?.defaultUserAddress
        view?.txtDeliveryTime.text = mPlanObject?.startTime + " - " + mPlanObject?.endTime
        view?.txtInstructions.text = mPlaceOrderRequestModel?.instructions
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
        mPlaceOrderRequestModel?.startFrom = sdfApiFormat.format(mCalender.time)
        var calender : Calendar = mCalender.clone() as Calendar
        calender.add(Calendar.DATE, (Integer.parseInt(mSelectedMealPlan?.noOfDays)))
        txtEnddate.text = String.format(getString(R.string.txt_end_date_is),sdfLocalFormat.format(calender.time))
        mPlaceOrderRequestModel?.endOn = sdfApiFormat.format(calender.time)
    }

    private fun doNavigate() {
        mViewModel?.doPlaceOrder(PlaceOrderRequestModel(mPlaceOrderRequestModel?.isTrialPlan,mPlaceOrderRequestModel?.noOfMeals,mPlaceOrderRequestModel?.noOfWeeks,
        mPlaceOrderRequestModel?.instructions,mPlaceOrderRequestModel?.planId,mPlaceOrderRequestModel?.mealId,mPlaceOrderRequestModel?.deliveryTime,
        mPlaceOrderRequestModel?.deliveryLocation, mPlaceOrderRequestModel?.address, mPlaceOrderRequestModel?.startFrom, mPlaceOrderRequestModel?.endOn,
        mSelectedMealPlan?.price, mPlaceOrderRequestModel?.planObj))
    }

    override fun onPayClick(position: Int) {

    }
}

