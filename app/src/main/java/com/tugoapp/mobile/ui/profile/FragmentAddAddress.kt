package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.crashlytics.internal.common.CommonUtils
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.AddAddressRequestModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_add_address.*
import javax.inject.Inject

class FragmentAddAddress : BaseFragment<AddAddressViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: AddAddressViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_add_address

    override val viewModel: AddAddressViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(AddAddressViewModel::class.java)
            return mViewModel!!
        }

    override val screenTitle: String
        get() = getString(R.string.txt_add_a_new_address)

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context

        initObserver()

        initControllers()
    }

    private fun initControllers() {
        btnAddAddress.setOnClickListener(View.OnClickListener {
            doAddAddress()
        })
    }

    private fun doAddAddress() {
        var address = edtLocation.text.toString()
        if(address.isNullOrBlank()) {
            com.tugoapp.mobile.utils.CommonUtils.showToast(mContext,getString(R.string.txt_fill_address))
        }

        mViewModel?.doAddAddressOnServer(AddAddressRequestModel(address,cbIsDefault.isChecked))
    }

    private fun initObserver() {

        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { com.tugoapp.mobile.utils.CommonUtils.showSnakeBar(rootView!!,it)})

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
                Navigation.findNavController(rootView!!).popBackStack()
                com.tugoapp.mobile.utils.CommonUtils.showSnakeBar(rootView,it.second)
            } else {
                com.tugoapp.mobile.utils.CommonUtils.showSnakeBar(rootView,it.second)
            }
        })
    }
}