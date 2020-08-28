package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.UserDetailModel
import com.tugoapp.mobile.ui.RootActivity
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class FragmentProfile : BaseFragment<ProfileViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: ProfileViewModel? = null
    lateinit var mContext: Context
    private var mProfileData: UserDetailModel? = null

    override val layoutId: Int
        get() = R.layout.fragment_profile

    override val viewModel: ProfileViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
            return mViewModel!!
        }
    override val screenTitle: String
        get() = getString(R.string.title_account_settings)

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = this!!.requireContext()
        (activity as RootActivity).supportActionBar?.setHomeButtonEnabled(false)
        (activity as RootActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        if(mProfileData == null)
        mViewModel?.doLodUserDetail();

        initController()

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

        mViewModel?.mUserDetailModel?.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                mProfileData = it

                doSetSwitchCallbacks(false)
                switchPromoNoti.isChecked = mProfileData?.sendPromotionalNotification!!
                switchPushNoti.isChecked = mProfileData?.sendPushNotification!!
                switchSmsNoti.isChecked = mProfileData?.sendSMSNotification!!
                doSetSwitchCallbacks(true)
            }
        })

        mViewModel?.mIsUserLogout?.observe(viewLifecycleOwner, Observer {
            if(it == 1) {
                FirebaseAuth.getInstance().signOut()
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentWelcome)
            } else {
                CommonUtils.showSnakeBar(rootView,getString(R.string.txt_logout_fail))
            }
        })
    }

    private fun initController() {
        llPersonalInfo.setOnClickListener(View.OnClickListener {
            var bundle  = bundleOf(AppConstant.USER_DETAIL_DATA to mProfileData)
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentPersonalInformation,bundle)
        })

        llChangePswd.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentChangePswd)
        })

        llDeliveryAddress.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentManageAddress)
        })

        llPaymentMethods.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentPaymentMethods)
        })

        llLogout.setOnClickListener(View.OnClickListener {
            var builder = CommonUtils.showDialog(mContext, R.string.title_warning, R.string.txt_confirm_logout)
            builder.setOnCancelListener(DialogInterface.OnCancelListener { dialog -> dialog.dismiss() })
            builder.setPositiveButton(R.string.btn_yes){dialogInterface, which ->
                mViewModel?.doUserLogout()
            }
            builder.setNegativeButton(R.string.btn_no){dialogInterface, which ->
                dialogInterface.dismiss()
            }
            builder.create().show()
        })

        doSetSwitchCallbacks(true)
    }

    private fun doSetSwitchCallbacks(isAdd: Boolean) {
        if(isAdd) {
            switchPushNoti.setOnCheckedChangeListener { buttonView, isChecked ->
                mProfileData?.sendPushNotification = isChecked
                doSetUserData(mProfileData)
            }
            switchSmsNoti.setOnCheckedChangeListener { buttonView, isChecked ->
                mProfileData?.sendSMSNotification = isChecked
                doSetUserData(mProfileData)
            }
            switchPromoNoti.setOnCheckedChangeListener { buttonView, isChecked ->
                mProfileData?.sendPromotionalNotification = isChecked
                doSetUserData(mProfileData)
            }
        } else {
            switchPushNoti.setOnCheckedChangeListener(null)
            switchSmsNoti.setOnCheckedChangeListener(null)
            switchPromoNoti.setOnCheckedChangeListener(null)
        }
    }

    private fun doSetUserData(data: UserDetailModel?) {
        mViewModel?.doSetUserProfile(data)
    }
}