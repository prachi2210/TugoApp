package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.AddAddressRequestModel
import com.tugoapp.mobile.data.remote.model.request.DeleteAddressRequestModel
import com.tugoapp.mobile.data.remote.model.request.UpdateAddressRequestModel
import com.tugoapp.mobile.data.remote.model.response.AddressModel
import com.tugoapp.mobile.ui.RootActivity
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_add_address.*
import javax.inject.Inject

class FragmentAddAddress : BaseFragment<AddAddressViewModel?>() {
    private var mAddressModel: AddressModel? = null

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

        mAddressModel = arguments?.getParcelable<AddressModel>(AppConstant.ADDRESS_TO_EDIT)

        if(mAddressModel != null) {
            edtLocation.setText(mAddressModel?.address)
            llEditBtnLayout.visibility = View.VISIBLE
            btnAddAddress.visibility = View.GONE
            txtHeaderAddress.text = getString(R.string.txt_edit_address_header)
            (activity as RootActivity).supportActionBar?.setTitle(getString(R.string.title_edit_address))
        } else {
            llEditBtnLayout.visibility = View.GONE
            btnAddAddress.visibility = View.VISIBLE
            txtHeaderAddress.text = getString(R.string.txt_add_address)
            (activity as RootActivity).supportActionBar?.setTitle(getString(R.string.txt_add_a_new_address))
        }

        initObserver()

        initControllers()
    }

    private fun initControllers() {
        btnAddAddress.setOnClickListener(View.OnClickListener {
            doAddAddress()
        })

        btnEditAddress.setOnClickListener(View.OnClickListener {
            doUpdateAddress()
        })

        btnDeleteAddress.setOnClickListener(View.OnClickListener {
            doDeleteAddress()
        })
    }

    private fun doDeleteAddress() {

        var builder = mContext?.let { CommonUtils.showDialog(it,R.string.txt_warning,R.string.txt_delete_address_confirmation) }
        builder?.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            mViewModel?.doDeleteAddress(DeleteAddressRequestModel(mAddressModel?.addressId))
        })
        builder?.setNegativeButton("No", DialogInterface.OnClickListener{
            dialog, which ->
            dialog.dismiss()
        })
        builder?.show()

    }

    private fun doAddAddress() {
        var address = edtLocation.text.toString()
        if(address.isNullOrBlank()) {
            com.tugoapp.mobile.utils.CommonUtils.showToast(mContext,getString(R.string.txt_fill_address))
        }
        mViewModel?.doAddAddressOnServer(AddAddressRequestModel(address,cbIsDefault.isChecked))
    }

    private fun doUpdateAddress() {
        var address = edtLocation.text.toString()
        if(address.isNullOrBlank()) {
            com.tugoapp.mobile.utils.CommonUtils.showToast(mContext,getString(R.string.txt_fill_address))
        }
        mViewModel?.doUpdateAddressOnServer(UpdateAddressRequestModel(mAddressModel?.addressId,address,cbIsDefault.isChecked))
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