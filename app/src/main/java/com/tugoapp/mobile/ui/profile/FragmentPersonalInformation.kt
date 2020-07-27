package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import javax.inject.Inject

class FragmentPersonalInformation : BaseFragment<PersonalInformationViewModel?>() {
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

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context
    }
}