package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.fragment_select_payment_method.*
import javax.inject.Inject


class FragmentSelectPaymentMethod : BottomSheetDialogFragment() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    var mContext: Context? = null

//    override val layoutId: Int
//        get() = R.layout.fragment_order_summary
//
//    override val viewModel: HomeViewModel
//        get() {
//            mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
//            return mViewModel!!
//        }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_payment_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context
//        btnPay.setOnClickListener(View.OnClickListener {
//            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSelectPaymentMethod_to_fragmentThankYou)
//        })
    }
}

