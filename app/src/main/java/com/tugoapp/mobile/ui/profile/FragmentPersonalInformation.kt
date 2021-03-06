package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.SaveUserDetailRequestModel
import com.tugoapp.mobile.data.remote.model.response.OrderModel
import com.tugoapp.mobile.data.remote.model.response.UserDetailModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import com.tugoapp.mobile.utils.SharedPrefsUtils
import kotlinx.android.synthetic.main.fragment_personal_info.*
import java.io.IOException
import java.util.*
import javax.inject.Inject

class FragmentPersonalInformation : BaseFragment<PersonalInformationViewModel?>() {
    private var mPersonalInfo: UserDetailModel? = null

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: PersonalInformationViewModel? = null
    var mContext: Context? = null
    var mIsFromLoginFlow : Boolean = false

    override val layoutId: Int
        get() = R.layout.fragment_personal_info

    override val viewModel: PersonalInformationViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(PersonalInformationViewModel::class.java)
            return mViewModel!!
        }

    override val screenTitle: String
        get() = getString(R.string.txt_personal_info)

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context

        mPersonalInfo = arguments?.getParcelable<UserDetailModel>(AppConstant.USER_DETAIL_DATA)
        if(arguments?.containsKey(AppConstant.KEY_IS_PROFILE_FROM_LOGIN)!!) {
            mIsFromLoginFlow = arguments?.getBoolean(AppConstant.KEY_IS_PROFILE_FROM_LOGIN)!!
        }

        if (mPersonalInfo == null) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }

        edtEmail.setText(mPersonalInfo?.userEmail)
        edtPhone.setText(mPersonalInfo?.userPhone)
        edtName.setText(mPersonalInfo?.userName)

        initObserver()

        btnProfileUpdate.setOnClickListener(View.OnClickListener {
            var newPhone = edtPhone.text.toString()
            var name = edtName.text.toString()
            if(mPersonalInfo?.userPhone != null && mPersonalInfo?.userPhone?.equals(newPhone)!!) {
                if(mPersonalInfo?.userName != null && mPersonalInfo?.userName?.equals(edtName.text.toString())!! && !mIsFromLoginFlow) {
                    CommonUtils.showSnakeBar(rootView,"No information is changed!")
                } else {
                    if(!edtName.text.toString().isNullOrBlank()) {
                        doUpdateServerForUserDetail()
                    } else {
                        CommonUtils.showSnakeBar(rootView,"Please fill name")
                    }
                }
            } else {
                SharedPrefsUtils.setStringPreference(mContext,AppConstant.FULL_NAME,name)
                var bundle = bundleOf(AppConstant.IS_FROM_EDIT_PROFILE to true,AppConstant.FIREBASE_EMAIL_ADDRESS to edtEmail.text.toString(), AppConstant.FIREBASE_PHONE_NUMBER to edtPhone.text.toString())
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentPersonalInformation_to_fragmentAddPhoneNumber,bundle)
            }
        })
    }

    private fun initObserver() {

        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { CommonUtils.showSnakeBar(rootView!!,it)})

        mViewModel?.mIsUserDetailSubmitted?.observe(viewLifecycleOwner, Observer {
            if(it == 1) {
                if(mIsFromLoginFlow) {
                    SharedPrefsUtils.setBooleanPreference(mContext, AppConstant.IS_LOGGED_IN, true)
                    SharedPrefsUtils.setStringPreference(mContext, AppConstant.FULL_NAME, mPersonalInfo?.userName)
                    SharedPrefsUtils.setStringPreference(mContext, AppConstant.LOGGED_IN_EMAIL, mPersonalInfo?.userEmail)
                    SharedPrefsUtils.setStringPreference(mContext, AppConstant.LOGGED_IN_PHONE, mPersonalInfo?.userPhone)

                    if (SharedPrefsUtils.didUserSeenWalkthrough(mContext!!, FirebaseAuth.getInstance().currentUser?.uid)) {
                        Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentPersonalInformation_to_fragmentHome)
                    } else {
                        SharedPrefsUtils.setWalkthroughForUser(mContext!!,  FirebaseAuth.getInstance().currentUser?.uid)
                        Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentPersonalInformation_to_fragmentWalkthrough)
                    }
                } else {
                    Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentPersonalInformation_to_fragmentProfile)
                }
            } else {
                CommonUtils.showSnakeBar(rootView!!,getString(R.string.txt_err_fail_user_detail))
            }
        })
    }

    private fun doUpdateServerForUserDetail() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener {
            task ->
            if (task.isSuccessful) {
                Thread(Runnable {
                    try {
                        mViewModel?.doSaveUserDetailOnServer(task.result?.token, SaveUserDetailRequestModel(edtEmail.text.toString(), edtPhone.text.toString(),
                                edtName.text.toString(),
                                FirebaseAuth.getInstance().currentUser?.uid,
                                mContext?.let { CommonUtils.getDeviceId(it) }, mContext?.let { SharedPrefsUtils.getPushToken(it,AppConstant.PREF_KEY_PUSH_TOKEN) },
                                "android", TimeZone.getDefault()?.displayName))
                    } catch (e: IOException) {
                    }
                }).start()
            } else {
                //CommonUtils.showSnakeBar(rootView,getString(R.string.txt_fail_save_user_server))
            }
        }
    }
}