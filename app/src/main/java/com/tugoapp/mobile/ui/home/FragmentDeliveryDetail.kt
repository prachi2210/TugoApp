package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.home.adapters.OnPayButtonClick
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_delivery_detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_order_summary.*
import kotlinx.android.synthetic.main.fragment_select_payment_method.view.*
import javax.inject.Inject

class FragmentDeliveryDetail : BaseFragment<HomeViewModel?>(), OnPayButtonClick {
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

        btnOrderSummary.setOnClickListener(View.OnClickListener {
            //Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentDeliveryDetail_to_fragmentOrderSummary)

//            val bottomSheetFragment = FragmentOrderSummary()
//            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)

            val view: View = layoutInflater.inflate(R.layout.fragment_order_summary, null)

            val dialog = mContext?.let { it1 -> BottomSheetDialog(it1) }
            dialog?.setContentView(view)
            dialog?.show()

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

    private fun doNavigate() {
        Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentDeliveryDetail_to_fragmentThankYou)
    }

    override fun onPayClick(position: Int) {

    }
}

