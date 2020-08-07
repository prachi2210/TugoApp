package com.tugoapp.mobile.ui.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_forgot_pswd.*
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class FragmentForgotPassword : BaseFragment<BaseViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: BaseViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_forgot_pswd

    override val viewModel: BaseViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(BaseViewModel::class.java)
            return mViewModel!!
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context
        initControls()
        initTextChange()
    }

    private fun initControls() {
        mContext = context
        btnReset.setOnClickListener(View.OnClickListener {
            doResendLink()
        })
    }

    private fun initTextChange() {
        edtEmailForgotPswd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var data = s.toString()
                if (!data.isNullOrBlank() && com.tugoapp.mobile.utils.CommonUtils.isEmailValid(data)) {
                    imgForgotEmailRight.visibility = View.VISIBLE
                } else {
                    imgForgotEmailRight.visibility = View.GONE
                }
            }
        })
    }

    private fun doResendLink() {
        var data = edtEmailForgotPswd.text.toString()
        if (data.isNullOrBlank() || !CommonUtils.isEmailValid(data)) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_valid_email))
            return
        }

        showLoading(getString(R.string.txt_please_wait))
        FirebaseAuth.getInstance().sendPasswordResetEmail(data).addOnCompleteListener { task ->
            hideLoading()
            if (task.isSuccessful) {
                CommonUtils.showSnakeBar(rootView,getString(R.string.txt_reset_link_sent))
            } else {
                CommonUtils.showSnakeBar(rootView,getString(R.string.txt_reset_link_sent_fail))
            }
        }
    }
}