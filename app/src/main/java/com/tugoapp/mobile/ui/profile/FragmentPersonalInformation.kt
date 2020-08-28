package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.OrderModel
import com.tugoapp.mobile.data.remote.model.response.UserDetailModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_personal_info.*
import javax.inject.Inject

class FragmentPersonalInformation : BaseFragment<PersonalInformationViewModel?>() {
    private var mPersonalInfo: UserDetailModel? = null

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: PersonalInformationViewModel? = null
    var mContext: Context? = null

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

        if (mPersonalInfo == null) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }

        edtEmail.setText(mPersonalInfo?.userEmail)
        edtPhone.setText(mPersonalInfo?.userPhone)
        edtName.setText(mPersonalInfo?.userName)
    }
}