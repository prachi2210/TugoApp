package com.tugoapp.mobile.ui.orderdetail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.OrderModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_browse_all_providers.*
import kotlinx.android.synthetic.main.fragment_order_detail.*
import kotlinx.android.synthetic.main.fragment_orders.*
import javax.inject.Inject

class FragmentOrderDetails : BaseFragment<OrderDetailsViewModel?>()  {
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

    override fun onResume() {
        super.onResume()
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
    }

    private fun initControllers() {
        llMessageUs.setOnClickListener(View.OnClickListener {
            mContext?.let { it1 -> CommonUtils.doSendMessageToWhatsApp(it1,rootView) }
        })
    }

    private fun doSetHistoryData() {
        if(mIsHistoryDetail) {
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
                .centerCrop()
                .into(imgOrder)

            txtTitleOrderDetail.text = mOrderDetailData?.companyName
            txtStatus.text = if(mIsHistoryDetail) getString(R.string.txt_expired) else getString(R.string.txt_ongoing)
            txtDateOrderDetail.text = mOrderDetailData?.orderPlacedAt
            txtOrderId.text = String.format(getString(R.string.txt_order_id),mOrderDetailData?.orderId)
            txtAddressOrderDetail.text = mOrderDetailData?.address
            txtOrderSummaryTitle.text = mOrderDetailData?.planName
            txtOrderSummary.text = "In progress in discussion"
            txtOrderDeliveryTime.text = String.format(getString(R.string.txt_delivery_time_order_summary),mOrderDetailData?.deliveryTime)
            txtOrderInstructions.text = String.format(getString(R.string.txt_order_summary_instructions),mOrderDetailData?.instructions)
            txtTotalPaid.text = mOrderDetailData?.price + "AED"
            txtPaymentType.text = mOrderDetailData?.paymentType
        }
    }

}