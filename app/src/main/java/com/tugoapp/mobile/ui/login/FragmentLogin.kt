package com.tugoapp.mobile.ui.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*
import javax.inject.Inject

class FragmentLogin : BaseFragment<LoginViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: LoginViewModel? = null
    var mContext: Context? = null
    private lateinit var auth: FirebaseAuth

    override val layoutId: Int
        get() = R.layout.fragment_login

    override val viewModel: LoginViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
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
        auth = FirebaseAuth.getInstance()
        initControls()
        initTextChange()
    }

    private fun initTextChange() {
        edtEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var data = s.toString()
                if(!data.isNullOrBlank() && com.tugoapp.mobile.utils.CommonUtils.isEmailValid(data)) {
                    imgSigninEmailRight.visibility = View.VISIBLE
                } else {
                    imgSigninEmailRight.visibility = View.GONE
                }
            }
        })

        edtPswd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var data = s.toString()
                if(!data.isNullOrBlank() && data.length >=6 ) {
                    imgSigninPswdRight.visibility = View.VISIBLE
                } else {
                    imgSigninPswdRight.visibility = View.GONE
                }
            }
        })

    }

    private fun initControls() {
        txtLoginSignUp.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentLogin_to_fragmentSignUp)
        })

        btnLoginSignIn.setOnClickListener(View.OnClickListener {doValidateAndLogin()  })
    }

    private fun doValidateAndLogin() {

        var email = edtEmail.text.toString()
        var pswd = edtPswd.text.toString()

        if(email.isNullOrBlank() || pswd.isNullOrBlank()) {
            CommonUtils.showToast(mContext,"Please fill details")
            return
        }

        auth.signInWithEmailAndPassword(email,pswd).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentLogin_to_fragmentWalkthrough)
            } else {
                CommonUtils.showToast(mContext,"Login Failed" +task.exception?.localizedMessage)
            }
        }
    }
}