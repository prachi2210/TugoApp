package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_manage_address.*
import javax.inject.Inject

class FragmentManageAddress : BaseFragment<ManageAddressViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: ManageAddressViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_manage_address

    override val viewModel: ManageAddressViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(ManageAddressViewModel::class.java)
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
        llAddAddress.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentManageAddress_to_fragmentAddAddress)
        })
    }
}