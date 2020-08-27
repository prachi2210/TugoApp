package com.tugoapp.mobile.ui.orderdetail

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.AddReviewRequestModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_write_a_comment.*
import javax.inject.Inject

class FragmentWriteAComment : BaseFragment<OrderDetailsViewModel?>() {
    private var mOrderId: String? = null

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: OrderDetailsViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_write_a_comment

    override val screenTitle: String
        get() = getString(R.string.title_write_a_comment)

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

        mOrderId = arguments?.getString(AppConstant.ORDER_DETAIL_ORDER_ID)

        if (mOrderId == null) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }

        initControllers()
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

        mViewModel?.mIsReviewAdded?.observe(viewLifecycleOwner, Observer {
           if(it == 1) {
               doShowSuccessDialog()
           } else {
               CommonUtils.showSnakeBar(rootView,getString(R.string.txt_write_comment_fail))
           }
        }
        )
    }

    private fun doShowSuccessDialog() {
        val li = LayoutInflater.from(context)
        val promptsView: View = li.inflate(R.layout.dialog_thankyou, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(promptsView)
        alertDialogBuilder.setCancelable(false)
        var btnBack = promptsView.findViewById<AppCompatButton>(R.id.btnBackToHomeComment)

        var dialog = alertDialogBuilder.create()

        btnBack.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
           Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentWriteAComment_to_fragmentHome)
        })

        dialog.show()
    }

    private fun initControllers() {

        btnSubmit.setOnClickListener(View.OnClickListener {
            doSubmitReview()
        })

    }

    private fun doSubmitReview() {
        var review = txtComment.text.toString()
        if(review.isNullOrBlank()) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.err_fill_comment))
            return
        }

        mViewModel?.doSubmitReview(AddReviewRequestModel(mOrderId,txtRating.rating.toString(),review))
    }
}
