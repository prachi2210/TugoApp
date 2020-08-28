package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.AddressModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.OnManageAddressListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.profile.adapter.AddressListAdapter
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_manage_address.*
import javax.inject.Inject

class FragmentManageAddress : BaseFragment<ManageAddressViewModel?>()  {

    private var mIsFromDeliveryScreen: Boolean? = false

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

    override val screenTitle: String
        get() = getString(R.string.title_delivery_address)


    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context

        mIsFromDeliveryScreen = arguments?.getBoolean(AppConstant.IS_FROM_DELIVERY_SCREEN)
        if(mIsFromDeliveryScreen != null && mIsFromDeliveryScreen as Boolean) {
            selectAddressHeader.visibility = View.VISIBLE
        } else {
            selectAddressHeader.visibility = View.GONE
        }

        initControls()

        initObservers()

        mViewModel?.doLoadAllAddress()
    }

    private fun initObservers() {
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

        mViewModel?.mAddressListData?.observe(viewLifecycleOwner, Observer {
            rvSavedAddress.layoutManager = LinearLayoutManager(mContext)
            val data = ArrayList<AddressModel>()
            if(it != null && it.size > 0) {
                data.addAll(it)
                llAddressList.visibility = View.VISIBLE
                txtEmptyViewAddress.visibility = View.GONE
            } else {
                llAddressList.visibility = View.GONE
                txtEmptyViewAddress.visibility = View.VISIBLE
            }
            val adapter = mContext?.let {
                AddressListAdapter(it, data, object : OnManageAddressListItemClickListener {
                    override fun onListItemClick(position: Int) {
                        if(mIsFromDeliveryScreen != null && mIsFromDeliveryScreen as Boolean) {
                            Navigation.findNavController(rootView!!).previousBackStackEntry?.savedStateHandle?.set("deliveryAddress", data[position])
                            Navigation.findNavController(rootView!!).popBackStack()
                        }
                    }

                    override fun onItemEditClick(position: Int) {
                        var bundle = bundleOf(AppConstant.ADDRESS_TO_EDIT to data[position])
                        Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentManageAddress_to_fragmentAddAddress,bundle)
                    }
                })
            }
            rvSavedAddress.adapter = adapter
        })
    }

    private fun initControls() {
        llAddAddress.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentManageAddress_to_fragmentAddAddress)
        })
    }
}