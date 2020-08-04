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

class FragmentChangePswd : BaseFragment<ChangePswdViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: ChangePswdViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_change_pswd

    override val viewModel: ChangePswdViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(ChangePswdViewModel::class.java)
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