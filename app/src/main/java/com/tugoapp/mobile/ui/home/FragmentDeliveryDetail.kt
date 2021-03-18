package com.tugoapp.mobile.ui.home

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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
import com.tugoapp.mobile.data.remote.model.response.*
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import com.tugoapp.mobile.utils.SharedPrefsUtils
import kotlinx.android.synthetic.main.dialog_for_startdate.view.*
import kotlinx.android.synthetic.main.fragment_delivery_detail.*
import kotlinx.android.synthetic.main.fragment_order_summary.*
import kotlinx.android.synthetic.main.fragment_order_summary.view.*
import mumbai.dev.sdkdubai.*
import mumbai.dev.sdkdubai.CustomModel.OnCustomStateListener
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class FragmentDeliveryDetail : BaseFragment<HomeViewModel?>(), OnCustomStateListener {
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
            mPlaceOrderRequestModel?.snackQty = mSelectedMealPlan?.snackQty
            mPlaceOrderRequestModel?.noOfMeals = mSelectedMealPlan?.noOfMeals
            mPlaceOrderRequestModel?.noOfWeeks = mSelectedMealPlan?.weeks
            if(mSelectedMealPlan?.snackQty?.toInt()!! > 0) {
                mPlaceOrderRequestModel?.price = mSelectedMealPlan?.priceWithSnack
                mPlaceOrderRequestModel?.amount = mSelectedMealPlan?.amountWithSnack
            } else {
                mPlaceOrderRequestModel?.price = mSelectedMealPlan?.price
                mPlaceOrderRequestModel?.amount = mSelectedMealPlan?.amount
            }
        } else {
            mPlaceOrderRequestModel?.mealId = null
            mPlaceOrderRequestModel?.snackQty = mSelectedMealPlan?.snackQty
            mPlaceOrderRequestModel?.noOfMeals = mPlanObject?.trialPlanMeals
            mPlaceOrderRequestModel?.noOfWeeks = mPlanObject?.trialPlanWeeks
            mPlaceOrderRequestModel?.price = mPlanObject?.trailPlanPricing
            mPlaceOrderRequestModel?.amount = mPlanObject?.trialPlanAmount
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
                if (it.second == null || it.second?.orderId.isNullOrEmpty()) {
                    CommonUtils.showSnakeBar(rootView, "OrderId should not be null from server")
                } else {
                    doOpenPaymentGateway(it.second!!)
                }
            } else {
                CommonUtils.showSnakeBar(rootView, it.second?.message)
            }
        })
    }

    private fun doOpenPaymentGateway(placeOrderModel: PlaceOrderResponseModel) {
        var paymentConfigs = SharedPrefsUtils.getStringPreference(mContext, AppConstant.PAYMENT_CONFIG_INFO)
        if (!paymentConfigs.isNullOrEmpty()) {
            var model = Gson().fromJson(paymentConfigs, PaymentConfigModel::class.java)
            if (model != null) {

                val merchantDetail = MerchantDetails()
                merchantDetail.access_code = model.access_code?.trim()
                merchantDetail.merchant_id = model.merchant_id?.trim()
                merchantDetail.currency = "AED".trim()
                merchantDetail.amount = mPlaceOrderRequestModel?.amount?.trim()
                merchantDetail.redirect_url = ApiConstants.BASE_URL.trim() + model.redirectUrl?.trim()
                merchantDetail.cancel_url = ApiConstants.BASE_URL.trim() + model.cancelUrl?.trim()
                merchantDetail.rsa_url = ApiConstants.BASE_URL.trim() + model.getRSA?.trim()
                merchantDetail.order_id = placeOrderModel.orderId.toString().trim()

                merchantDetail.add1 = "test1"
                merchantDetail.add2 = "test2"
                merchantDetail.add3 = "test3"
                merchantDetail.add4 = "test4"
                merchantDetail.add5 = "test5"
                merchantDetail.isShow_addr = false
//                merchantDetail.promo_code = ""
 //               merchantDetail.isCCAvenue_promo = true
                val promocode = txtDeliveryPromocode.text.toString()
                if(promocode.isNullOrBlank()) {
                    merchantDetail.isCCAvenue_promo = false
                    merchantDetail.promo_code = ""
                } else {
                    merchantDetail.isCCAvenue_promo = true
                    merchantDetail.promo_code = promocode
                }

                val billingAddress = BillingAddress()
                val shippingAddress = ShippingAddress()

                if(!placeOrderModel.name?.trim().isNullOrBlank()) {
                    billingAddress.name = placeOrderModel.name?.trim()
                    shippingAddress.name = placeOrderModel.name?.trim()
                } else {
//                    billingAddress.name = "Tugo".trim()
//                    shippingAddress.name = "Tugo".trim()
                    CommonUtils.showSnakeBar(rootView, "Name detail not found for billling and shipping address.")
                    return
                }

                if(!mPlaceOrderRequestModel?.address?.trim().isNullOrBlank()) {
                    billingAddress.address = mPlaceOrderRequestModel?.address?.trim()
                    billingAddress.state = mPlaceOrderRequestModel?.address?.trim()
                    billingAddress.city = mPlaceOrderRequestModel?.address?.trim()

                    shippingAddress.address = mPlaceOrderRequestModel?.address?.trim()
                    shippingAddress.state = mPlaceOrderRequestModel?.address?.trim()
                    shippingAddress.city = mPlaceOrderRequestModel?.address?.trim()
                } else {
//                    billingAddress.address = "Tugo".trim()
//                    billingAddress.state = "Tugo".trim()
//                    billingAddress.city = "Tugo".trim()
//
//                    shippingAddress.address = "Tugo".trim()
//                    shippingAddress.state = "Tugo".trim()
//                    shippingAddress.city = "Tugo".trim()
                    CommonUtils.showSnakeBar(rootView, "Address detail not found for billling and shipping address.")
                    return
                }

                if(!placeOrderModel.number?.trim().isNullOrBlank()) {
                    billingAddress.telephone = placeOrderModel.number?.trim()
                    shippingAddress.telephone = placeOrderModel.number?.trim()
                } else {
//                    billingAddress.telephone = "999999999".trim()
//                    shippingAddress.telephone = "999999999".trim()
                    CommonUtils.showSnakeBar(rootView, "Phone detail not found for billling and shipping address.")
                    return
                }

                if(!placeOrderModel.email?.trim().isNullOrBlank()) {
                    billingAddress.email = placeOrderModel.email?.trim()
                    merchantDetail.customer_id = placeOrderModel.email?.trim()
                } else {
                    //billingAddress.email = "paras.gangwal@avenues.info".trim()
                    CommonUtils.showSnakeBar(rootView, "Email detail not found for billling address.")
                    return
                }

                billingAddress.country = "UAE".trim()
                shippingAddress.country = "UAE".trim()

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

            var promocode = txtDeliveryPromocode.text.toString()
            if(promocode.isNullOrBlank()) {
                view.llPromoCode.visibility = View.GONE
            } else {
                view.txtPromocode.text = promocode
                view.llPromoCode.visibility = View.VISIBLE
            }

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
        txtAvailableDeliveryTime.text = mPlanObject?.deliveryTime
        deliveryDays.text = mPlanObject?.deliveryDays
        if (mIsTrialMeal) {
            txtDuration.text = String.format(getString(R.string.txt_duration_days), mPlanObject?.trialPlanDays?.let { Integer.parseInt(it) })
        } else {
            txtDuration.text = String.format(getString(R.string.txt_duration_days), mSelectedMealPlan?.noOfDays?.let { Integer.parseInt(it) })
        }

        mCalender.timeInMillis = System.currentTimeMillis()
       // updateLabel()
        llDeliveryStartDate.setOnClickListener(View.OnClickListener {
            var canShowDateInfoDialog = SharedPrefsUtils.getBooleanPreference(mContext, AppConstant.PREF_KEY_SHOW_DATE_DIALOG, true)
            if (canShowDateInfoDialog) {
                val li = LayoutInflater.from(context)
                var dateDialogView = li.inflate(R.layout.dialog_for_startdate, null)
                val alertDialogBuilder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
                alertDialogBuilder.setView(dateDialogView)
                alertDialogBuilder.setCancelable(false)

                var dateDialog = alertDialogBuilder.create()

                dateDialogView.btnGotIt.setOnClickListener {
                    if (dateDialogView.rbDontShowAgain.isSelected) {
                        SharedPrefsUtils.setBooleanPreference(mContext, AppConstant.PREF_KEY_SHOW_DATE_DIALOG, false)
                    } else {
                        SharedPrefsUtils.setBooleanPreference(mContext, AppConstant.PREF_KEY_SHOW_DATE_DIALOG, true)
                    }
                    dateDialog.dismiss()
                    showDateDialog()
                }

                dateDialogView.rbDontShowAgain.setOnClickListener {
                    if (dateDialogView.rbDontShowAgain.isSelected) {
                        dateDialogView.rbDontShowAgain.isChecked = false
                        dateDialogView.rbDontShowAgain.isSelected = false
                    } else {
                        dateDialogView.rbDontShowAgain.isChecked = true
                        dateDialogView.rbDontShowAgain.isSelected = true
                    }
                }
                dateDialog.show()
            } else {
                showDateDialog()
            }
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

    private fun showDateDialog() {
        var dialog = mContext?.let { it1 -> DatePickerDialog(it1, onDateSelectedEvent, mCalender[Calendar.YEAR], mCalender[Calendar.MONTH], mCalender[Calendar.DAY_OF_MONTH]) }
        dialog?.datePicker?.minDate = System.currentTimeMillis() - 1000
        dialog?.show()
    }

    private fun doShowAddressDialog(isAddAddress: Boolean, address: String?, addressId: String?) {

        var bundle = bundleOf(AppConstant.IS_FROM_DELIVERY_SCREEN to true)
        Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentDeliveryDetail_to_fragmentManageAddress, bundle)
    }

    private fun doSetOrderSummary(view: View) {
        view.txtPlanName?.text = mPlanObject?.title
        view.txtPlanDetail?.text = mPlanObject?.description
        view.deliveryDaysSummary?.text = mPlanObject?.deliveryDays

        if (mIsTrialMeal) {
            view.txtPlanSub?.text = mPlanObject?.trialPlanDescription
        } else {
            var noOfSnacks = mSelectedMealPlan?.snackQty?.toInt()!!
            var noOfMeals =  mSelectedMealPlan?.noOfMeals?.toInt()!!
            var text = ""
            if(noOfSnacks > 0) {

                text = if(noOfMeals == 1) {
                    text +  mSelectedMealPlan?.noOfMeals + " Meal for " + mSelectedMealPlan?.noOfDays + " days \n"
                } else {
                    text +  mSelectedMealPlan?.noOfMeals + " Meals for " + mSelectedMealPlan?.noOfDays + " days \n"
                }

                text = if(noOfSnacks == 1) {
                    text + mSelectedMealPlan?.snackQty + " Snack for "+ mSelectedMealPlan?.noOfDays + " days "
                } else {
                    text + mSelectedMealPlan?.snackQty + " Snacks for "+ mSelectedMealPlan?.noOfDays + " days "
                }

                view.txtPlanSub?.text = text
            } else {
                view.txtPlanSub?.text = if(noOfMeals == 1) {
                    mSelectedMealPlan?.noOfMeals + " Meal for " + mSelectedMealPlan?.noOfDays + " days "
                } else {
                    mSelectedMealPlan?.noOfMeals + " Meals for " + mSelectedMealPlan?.noOfDays + " days "
                }
            }
        }
        view.txtLocation?.text = mPlanObject?.defaultUserAddress
        view.txtDeliveryTime.text = mPlanObject?.deliveryTime
        view.txtInstructions.text = mPlaceOrderRequestModel?.instructions
        if (mIsTrialMeal) {
            view.txtPlanTotalAmount.text = mPlanObject?.trailPlanPricing
        } else {
            view.txtPlanTotalAmount.text = mPlaceOrderRequestModel?.price
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
            calender.add(Calendar.DATE, (mPlanObject?.trialPlanWeeks?.let { Integer.parseInt(it) }?.times(7)!!))
        } else {
            calender.add(Calendar.DATE, (mSelectedMealPlan?.weeks?.let { Integer.parseInt(it) }?.times(7)!!))
        }
        txtEnddate.text = String.format(getString(R.string.txt_end_date_is), sdfLocalFormat.format(calender.time))
        mPlaceOrderRequestModel?.endOn = sdfLocalFormat.format(calender.time)
    }

    private fun doPlaceOrderAndPayForOrder() {
        var paymentConfigs = SharedPrefsUtils.getStringPreference(mContext, AppConstant.PAYMENT_CONFIG_INFO)
        if (!paymentConfigs.isNullOrEmpty()) {
            var model = Gson().fromJson(paymentConfigs, PaymentConfigModel::class.java)
            if (model != null) {
                mViewModel?.doPlaceOrder(PlaceOrderRequestModel(mPlaceOrderRequestModel?.isTrialPlan, mPlaceOrderRequestModel?.noOfMeals,mPlaceOrderRequestModel?.snackQty, mPlaceOrderRequestModel?.noOfWeeks,
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
        try {
            if (!modelState.isNullOrEmpty()) {
                val model = Gson().fromJson(modelState, PaymentGatewayResponseModel::class.java)
                if (model != null) {
                    if (model.success) {
                        isNavigationRequired = true
                    } else {
                        CommonUtils.showSnakeBar(rootView!!, model.status_message)
                    }
                } else {
                    CommonUtils.showSnakeBar(rootView!!, "Payment status not found. Can not procceed without it")
                }
            } else {
                CommonUtils.showSnakeBar(rootView!!, "Payment status not found. Can not procceed without it")
            }
        } catch (e: Exception) {

        }
    }

    override fun onResume() {
        super.onResume()
        if (isNavigationRequired) {
            isNavigationRequired = false
            var bundle = bundleOf(AppConstant.SELECTED_MEAL_PLAN to mSelectedMealPlan, AppConstant.SELECTED_PLAN_OBJECT to mPlanObject,
                    AppConstant.START_DATE_FOR_THANKYOU to mPlaceOrderRequestModel?.startFrom, AppConstant.SELECTED_MEAL_PLAN_TRIAL to mIsTrialMeal)
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentDeliveryDetail_to_fragmentThankYou, bundle)
        }
    }
}

