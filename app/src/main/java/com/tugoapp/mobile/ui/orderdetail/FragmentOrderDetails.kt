package com.tugoapp.mobile.ui.orderdetail

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.AddAddressRequestModel
import com.tugoapp.mobile.data.remote.model.request.ResumeOrderRequestModel
import com.tugoapp.mobile.data.remote.model.request.UpdateAddressRequestModel
import com.tugoapp.mobile.data.remote.model.response.OrderModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_browse_all_providers.*
import kotlinx.android.synthetic.main.fragment_order_detail.*
import kotlinx.android.synthetic.main.fragment_orders.*
import javax.inject.Inject

class FragmentOrderDetails : BaseFragment<OrderDetailsViewModel?>() {
    private var mIsHistoryDetail: Boolean = false
    private var mOrderDetailData: OrderModel? = null

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: OrderDetailsViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_order_detail

    override val screenTitle: String
        get() = getString(R.string.title_order_detail)

    override val viewModel: OrderDetailsViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(OrderDetailsViewModel::class.java)
            return mViewModel!!
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context

        mOrderDetailData = arguments?.getParcelable<OrderModel>(AppConstant.SELECTED_ORDER_FOR_DETAIL)
        mIsHistoryDetail = arguments?.getBoolean(AppConstant.SELECTED_ORDER_IS_HISTORY)!!

        if (mOrderDetailData == null) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }

        initControllers()
        doSetHistoryData()

        initObserver()
    }

    private fun initObserver() {

        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { CommonUtils.showSnakeBar(rootView!!,it)})

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

        mViewModel?.mIsPausePlanDone?.observe(viewLifecycleOwner, Observer {
            if(it.first == 1) {
                CommonUtils.showSnakeBar(rootView,it.second)
            } else {
                CommonUtils.showSnakeBar(rootView,it.second)
            }
        })
        mViewModel?.mIsCancelPlanDone?.observe(viewLifecycleOwner, Observer {
            if(it.first == 1) {
                CommonUtils.showSnakeBar(rootView,it.second)
            } else {
                CommonUtils.showSnakeBar(rootView,it.second)
            }
        })
        mViewModel?.mIsResumePlanDone?.observe(viewLifecycleOwner, Observer {
            if(it.first == 1) {
                CommonUtils.showSnakeBar(rootView,it.second)
            } else {
                CommonUtils.showSnakeBar(rootView,it.second)
            }
        })
    }

    private fun initControllers() {
        llMessageUs.setOnClickListener(View.OnClickListener {
            mContext?.let { it1 -> CommonUtils.doSendMessageToWhatsApp(it1, rootView) }
        })

        btnPause.setOnClickListener(View.OnClickListener {
            doShowPauseDialog(true)
        })

        btnCancel.setOnClickListener(View.OnClickListener {
            doShowPauseDialog(false)
        })
    }

    private fun doShowPauseDialog(isPauseDialog: Boolean) {
        val li = LayoutInflater.from(context)
        val promptsView: View = li.inflate(R.layout.dialog_pause_plan, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(promptsView)
        alertDialogBuilder.setCancelable(false)
        var txtHeader = promptsView.findViewById<TextView>(R.id.txtHeaderOrderPause)
        var btnNevermind = promptsView.findViewById<TextView>(R.id.btnNeverMind)
        var btnPauseOrResume = promptsView.findViewById<TextView>(R.id.btnPausePlan)
        var btnClose = promptsView.findViewById<ImageView>(R.id.imgclosePauseDialog)
        var imgPauseOrResume = promptsView.findViewById<ImageView>(R.id.imagePausePlan)

        if (isPauseDialog) {
            txtHeader.text = getString(R.string.txt_header_pause_order)
            imgPauseOrResume.setImageResource(R.drawable.ic_pause)
            btnPauseOrResume.text = getString(R.string.txt_pause_plan)
        } else {
            txtHeader.text = getString(R.string.txt_header_cancel_order)
            imgPauseOrResume.setImageResource(R.drawable.ic_cancel)
            btnPauseOrResume.text = getString(R.string.txt_cancel_plan)
        }
        var dialog = alertDialogBuilder.create()

        btnNevermind.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        btnClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        btnPauseOrResume.setOnClickListener(View.OnClickListener {
            if (isPauseDialog) {
                mViewModel?.doPausePlan(ResumeOrderRequestModel(mOrderDetailData?.orderId,""))
                dialog.dismiss()
            } else {
                mViewModel?.doCancelPlan(ResumeOrderRequestModel(mOrderDetailData?.orderId,""))
                dialog.dismiss()
            }
        })

        dialog.show()
    }

    private fun doSetHistoryData() {
        if (mIsHistoryDetail) {
            llReorderOrWrite.visibility = View.VISIBLE
            llPauseCancel.visibility = View.GONE
            llMessageUs.visibility = View.GONE
        } else {
            llReorderOrWrite.visibility = View.GONE
            llPauseCancel.visibility = View.VISIBLE
            llMessageUs.visibility = View.VISIBLE
        }

        mContext?.let {
            Glide.with(it)
                    .load(mOrderDetailData?.companyLogo)
                    .circleCrop()
                    .into(imgOrder)

            txtTitleOrderDetail.text = mOrderDetailData?.companyName
            if (mIsHistoryDetail) txtStatus.text = getString(R.string.txt_expired)
            else {
                if (mOrderDetailData?.isPaused!!) txtStatus.text = getString(R.string.txt_paused)
                else txtStatus.text = getString(R.string.txt_ongoing)
            }
            txtDateOrderDetail.text = mOrderDetailData?.orderPlacedAt
            txtOrderId.text = String.format(getString(R.string.txt_order_id), mOrderDetailData?.orderId)
            txtAddressOrderDetail.text = mOrderDetailData?.address
            txtOrderSummaryTitle.text = mOrderDetailData?.planName
            txtOrderSummary.text = mOrderDetailData?.noOfMeals + " meals per day for " + mOrderDetailData?.noOfDays + " days"
            txtOrderDeliveryTime.text = String.format(getString(R.string.txt_delivery_time_order_summary), mOrderDetailData?.deliveryTime)
            txtOrderInstructions.text = String.format(getString(R.string.txt_order_summary_instructions), mOrderDetailData?.instructions)
            txtTotalPaid.text = mOrderDetailData?.price + "AED"
            txtPaymentType.text = mOrderDetailData?.paymentType
        }
    }

}