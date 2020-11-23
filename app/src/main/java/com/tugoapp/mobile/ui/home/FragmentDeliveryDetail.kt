package com.tugoapp.mobile.ui.home

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.ApiConstants
import com.tugoapp.mobile.data.remote.model.request.PlaceOrderObject
import com.tugoapp.mobile.data.remote.model.request.PlaceOrderRequestModel
import com.tugoapp.mobile.data.remote.model.response.AddressModel
import com.tugoapp.mobile.data.remote.model.response.MealOptionsModel
import com.tugoapp.mobile.data.remote.model.response.MealPlanModel
import com.tugoapp.mobile.data.remote.model.response.PaymentConfigModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import com.tugoapp.mobile.utils.SharedPrefsUtils
import kotlinx.android.synthetic.main.fragment_delivery_detail.*
import kotlinx.android.synthetic.main.fragment_order_summary.*
import kotlinx.android.synthetic.main.fragment_order_summary.view.*
import mumbai.dev.sdkdubai.*
import mumbai.dev.sdkdubai.CustomModel.OnCustomStateListener
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class FragmentDeliveryDetail : BaseFragment<HomeViewModel?>(), OnCustomStateListener{
    private var isNavigationRequired: Boolean = false
    private var mIsTrialMeal: Boolean = false
    private var mPlanObject: MealPlanModel? = null
    private var mSelectedMealPlan: MealOptionsModel? = null

    private var mPlaceOrderRequestModel: PlaceOrderObject? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context
        CustomModel.getInstance().setListener(this)
        mPlanObject = arguments?.getParcelable(AppConstant.SELECTED_PLAN_OBJECT)
        mSelectedMealPlan = arguments?.getParcelable(AppConstant.SELECTED_MEAL_PLAN)
        mIsTrialMeal = arguments?.getBoolean(AppConstant.SELECTED_MEAL_PLAN_TRIAL)!!

        if (mIsTrialMeal && (mPlanObject == null)) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        } else if (!mIsTrialMeal && (mPlanObject == null || mSelectedMealPlan == null)) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }

        mPlaceOrderRequestModel = PlaceOrderObject
        mPlaceOrderRequestModel?.planObj = mPlanObject as MealPlanModel
        mPlaceOrderRequestModel?.isTrialPlan = mIsTrialMeal
        mPlaceOrderRequestModel?.planId = mPlanObject?.planId
        mPlaceOrderRequestModel?.address = mPlanObject?.defaultUserAddress
        mPlaceOrderRequestModel?.deliveryTime = mPlanObject?.deliveryTime
        mPlaceOrderRequestModel?.deliveryLocation = mPlanObject?.locations

        if (!mIsTrialMeal) {
            mPlaceOrderRequestModel?.mealId = mSelectedMealPlan?.mealId.toString()
            mPlaceOrderRequestModel?.noOfMeals = mSelectedMealPlan?.noOfMeals
            mPlaceOrderRequestModel?.noOfWeeks = mSelectedMealPlan?.weeks
            mPlaceOrderRequestModel?.price = mSelectedMealPlan?.price
        } else {
            mPlaceOrderRequestModel?.mealId = null
            mPlaceOrderRequestModel?.noOfMeals = mPlanObject?.trialPlanMeals
            mPlaceOrderRequestModel?.noOfWeeks = mPlanObject?.trialPlanWeeks
            mPlaceOrderRequestModel?.price = mPlanObject?.trailPlanPricing
        }

        doInitDeliveryDetails()
        initController()
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

        mViewModel?.mIsPaymentConfigInfoUpdated?.observe(viewLifecycleOwner, Observer {
            if (it) {
                doPlaceOrderAndPayForOrder()
            } else {
                CommonUtils.showSnakeBar(rootView!!, "Failed to load payment gateway config information")
            }
        })

        Navigation.findNavController(rootView!!).currentBackStackEntry?.savedStateHandle?.getLiveData<AddressModel>("deliveryAddress")?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mPlaceOrderRequestModel?.planObj?.addressId = it.addressId
                mPlaceOrderRequestModel?.planObj?.defaultUserAddress = it.address
                mPlaceOrderRequestModel?.address = it.address
                txtAddress.text = it.address
                btnEditAddress.text = getString(R.string.txt_change_address)
                txtAddress.visibility = View.VISIBLE
            }
            Navigation.findNavController(rootView!!).previousBackStackEntry?.savedStateHandle?.remove<AddressModel>("deliveryAddress")
        })

        mViewModel?.mPlaceOrderResponse?.observe(viewLifecycleOwner, Observer {
            if (it.first == 1) {
                doOpenPaymentGateway(it.second?.orderId)
            } else {
                CommonUtils.showSnakeBar(rootView, it.second?.message)
            }
        })
    }

    private fun doOpenPaymentGateway(orderId: Int?) {
        var paymentConfigs = SharedPrefsUtils.getStringPreference(mContext, AppConstant.PAYMENT_CONFIG_INFO)
        if (!paymentConfigs.isNullOrEmpty()) {
            var model = Gson().fromJson(paymentConfigs, PaymentConfigModel::class.java)
            if (model != null) {

                val merchantDetail = MerchantDetails()
                merchantDetail.access_code = model.access_code?.trim()
                merchantDetail.merchant_id = model.merchant_id?.trim()
                merchantDetail.currency = "AED".trim()
                merchantDetail.amount = mPlaceOrderRequestModel?.price?.trim()
                merchantDetail.redirect_url = ApiConstants.BASE_URL.trim() +model.redirectUrl?.trim()
                merchantDetail.cancel_url = ApiConstants.BASE_URL.trim() + model.cancelUrl?.trim()
                merchantDetail.rsa_url = ApiConstants.BASE_URL.trim() + model.getRSA?.trim()
                merchantDetail.order_id = orderId.toString().trim()
                merchantDetail.customer_id = "Test".trim()
                merchantDetail.promo_code = "".trim()
                merchantDetail.add1 = "test1"
                merchantDetail.add2 = "test2"
                merchantDetail.add3 = "test3"
                merchantDetail.add4 = "test4"
                merchantDetail.add5 = "test5"
                merchantDetail.isShow_addr = false
                merchantDetail.isCCAvenue_promo = false

                val billingAddress = BillingAddress()
                billingAddress.name = "Tugo".trim()
                billingAddress.address = mPlaceOrderRequestModel?.address?.trim()
                billingAddress.country = mPlaceOrderRequestModel?.address?.trim()
                billingAddress.state = mPlaceOrderRequestModel?.address?.trim()
                billingAddress.city = mPlaceOrderRequestModel?.address?.trim()
                billingAddress.telephone = "999999999".trim()
                billingAddress.email = "paras.gangwal@avenues.info".trim()


                val shippingAddress = ShippingAddress()
                shippingAddress.name = "Tugo".trim()
                shippingAddress.address = mPlaceOrderRequestModel?.address?.trim()
                shippingAddress.country = mPlaceOrderRequestModel?.address?.trim()
                shippingAddress.state = mPlaceOrderRequestModel?.address?.trim()
                shippingAddress.city = mPlaceOrderRequestModel?.address?.trim()
                shippingAddress.telephone = "999999999".trim()


                val sdkIntent = Intent(context, PaymentOptions::class.java)
                sdkIntent.putExtra("merchant", merchantDetail)
                sdkIntent.putExtra("billing", billingAddress)
                sdkIntent.putExtra("shipping", shippingAddress)
                startActivity(sdkIntent)
            } else {
                CommonUtils.showSnakeBar(rootView!!, "Failed to convert payment config information to model")
            }
        } else {
            CommonUtils.showSnakeBar(rootView!!, "Payment gateway config information not found")
        }
    }

    private fun initController() {
        btnOrderSummary.setOnClickListener(View.OnClickListener {
            if (!doValidateDeliveryScreenData()) {
                return@OnClickListener
            }
            val view: View = layoutInflater.inflate(R.layout.fragment_order_summary, null)
            mPlaceOrderRequestModel?.instructions = txtDeliveryInstructions?.text.toString()
            doSetOrderSummary(view)
            val dialog = mContext?.let { it1 -> BottomSheetDialog(it1) }
            dialog?.setContentView(view)
            dialog?.show()
            view.txtEdit.setOnClickListener(View.OnClickListener {
                dialog?.dismiss()
            })

            dialog?.btnPay?.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
                doPlaceOrderAndPayForOrder()

//                val view1: View = layoutInflater.inflate(R.layout.fragment_select_payment_method, null)
//                val dialog1 = mContext?.let { it1 -> BottomSheetDialog(it1) }
//                dialog1?.setContentView(view1)
//                dialog1?.show()
//                view1.btnPay.setOnClickListener(View.OnClickListener {
//
//                })
            })
        })
    }

    private fun doValidateDeliveryScreenData(): Boolean {
        if (txtAddress.text.toString().isNullOrBlank()) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.err_fill_address))
            return false
        }
        if (txtStartdate.text.toString().isNullOrBlank()) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.err_fill_date))
            return false
        }
        return true
    }

    private fun doInitDeliveryDetails() {
        deliveryDays.text = mPlanObject?.deliveryDays
        if (mIsTrialMeal) {
            txtDuration.text = String.format(getString(R.string.txt_duration_days), Integer.parseInt(mPlanObject?.trialPlanDays))
        } else {
            txtDuration.text = String.format(getString(R.string.txt_duration_days), Integer.parseInt(mSelectedMealPlan?.noOfDays))
        }

        mCalender.timeInMillis = System.currentTimeMillis()
        updateLabel()
        llDeliveryStartDate.setOnClickListener(View.OnClickListener {
            var dialog = DatePickerDialog(mContext, onDateSelectedEvent, mCalender[Calendar.YEAR], mCalender[Calendar.MONTH], mCalender[Calendar.DAY_OF_MONTH])
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dialog.show()
        })

        if (!mPlanObject?.defaultUserAddress.isNullOrBlank()) {
            txtAddress.text = mPlanObject?.defaultUserAddress
            btnEditAddress.text = getString(R.string.txt_change_address)
            txtAddress.visibility = View.VISIBLE
        } else {
            txtAddress.visibility = View.GONE
            btnEditAddress.text = getString(R.string.txt_add_address_with_bracket)
        }

        btnEditAddress.setOnClickListener(View.OnClickListener {
            if (mPlanObject?.defaultUserAddress.isNullOrBlank()) {
                doShowAddressDialog(true, "", "")
            } else {
                doShowAddressDialog(false, mPlanObject?.defaultUserAddress, mPlanObject?.addressId)
            }
        })
    }

    private fun doShowAddressDialog(isAddAddress: Boolean, address: String?, addressId: String?) {

        var bundle = bundleOf(AppConstant.IS_FROM_DELIVERY_SCREEN to true)
        Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentDeliveryDetail_to_fragmentManageAddress, bundle)
    }

    private fun doSetOrderSummary(view: View) {
        view.txtPlanName?.text = mPlanObject?.title
        view.txtPlanDetail?.text = mPlanObject?.description

        if (mIsTrialMeal) {
            view.txtPlanSub?.text = mPlanObject?.trialPlanDescription
        } else {
            view.txtPlanSub?.text = mSelectedMealPlan?.noOfMeals + " meals X " + mSelectedMealPlan?.noOfDays + " days for " + mSelectedMealPlan?.weeks + " weeks"
        }
        view.txtLocation?.text = mPlanObject?.defaultUserAddress
        view.txtDeliveryTime.text = mPlanObject?.deliveryTime
        view.txtInstructions.text = mPlaceOrderRequestModel?.instructions
        if (mIsTrialMeal) {
            view.txtPlanTotalAmount.text = mPlanObject?.trailPlanPricing
        } else {
            view.txtPlanTotalAmount.text = mSelectedMealPlan?.price
        }
    }

    var onDateSelectedEvent = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        mCalender = Calendar.getInstance()
        mCalender[Calendar.YEAR] = year
        mCalender[Calendar.MONTH] = monthOfYear
        mCalender[Calendar.DAY_OF_MONTH] = dayOfMonth
        updateLabel()
    }

    private fun updateLabel() {
        val localFormat = "dd/MM/yyyy"
        val sdfLocalFormat = SimpleDateFormat(localFormat)

        txtStartdate.text = sdfLocalFormat.format(mCalender.time)
        mPlaceOrderRequestModel?.startFrom = sdfLocalFormat.format(mCalender.time)
        var calender: Calendar = mCalender.clone() as Calendar
        if (mIsTrialMeal) {
            calender.add(Calendar.DATE, (Integer.parseInt(mPlanObject?.trialPlanWeeks) * 7))
        } else {
            calender.add(Calendar.DATE, (Integer.parseInt(mSelectedMealPlan?.weeks) * 7))
        }
        txtEnddate.text = String.format(getString(R.string.txt_end_date_is), sdfLocalFormat.format(calender.time))
        mPlaceOrderRequestModel?.endOn = sdfLocalFormat.format(calender.time)
    }

    private fun doPlaceOrderAndPayForOrder() {
        var paymentConfigs = SharedPrefsUtils.getStringPreference(mContext, AppConstant.PAYMENT_CONFIG_INFO)
        if (!paymentConfigs.isNullOrEmpty()) {
            var model = Gson().fromJson(paymentConfigs, PaymentConfigModel::class.java)
            if (model != null) {
                mViewModel?.doPlaceOrder(PlaceOrderRequestModel(mPlaceOrderRequestModel?.isTrialPlan, mPlaceOrderRequestModel?.noOfMeals, mPlaceOrderRequestModel?.noOfWeeks,
                        mPlaceOrderRequestModel?.instructions, mPlaceOrderRequestModel?.planId, mPlaceOrderRequestModel?.mealId, mPlaceOrderRequestModel?.deliveryTime,
                        mPlaceOrderRequestModel?.deliveryLocation, mPlaceOrderRequestModel?.address, mPlaceOrderRequestModel?.startFrom, mPlaceOrderRequestModel?.endOn,
                        mPlaceOrderRequestModel?.price, mPlaceOrderRequestModel?.planObj))
            } else {
                CommonUtils.showSnakeBar(rootView!!, "Failed to convert payment config information to model")
            }
        } else {
            mViewModel?.doGetPaymentConfigs()
        }
    }

    override fun stateChanged() {
        val modelState = CustomModel.getInstance().state
        isNavigationRequired = true
    }

    override fun onResume() {
        super.onResume()
        if(isNavigationRequired) {
            isNavigationRequired = false

            var bundle = bundleOf(AppConstant.SELECTED_MEAL_PLAN to mSelectedMealPlan,AppConstant.SELECTED_PLAN_OBJECT to mPlanObject,
                    AppConstant.START_DATE_FOR_THANKYOU to mPlaceOrderRequestModel?.startFrom,AppConstant.SELECTED_MEAL_PLAN_TRIAL to mIsTrialMeal)


            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentDeliveryDetail_to_fragmentThankYou,bundle)
        }
    }
}

