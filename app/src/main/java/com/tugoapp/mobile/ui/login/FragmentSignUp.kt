package com.tugoapp.mobile.ui.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import com.tugoapp.mobile.utils.SharedPrefsUtils
import kotlinx.android.synthetic.main.fragment_add_phone_number.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import kotlinx.android.synthetic.main.fragment_welcome.*
import javax.inject.Inject

class FragmentSignUp : BaseFragment<SignUpViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: SignUpViewModel? = null
    var mContext: Context? = null
    private lateinit var auth: FirebaseAuth

    override val layoutId: Int
        get() = R.layout.fragment_signup

    override val viewModel: SignUpViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(SignUpViewModel::class.java)
            return mViewModel!!
        }

    override val screenTitle: String
        get() = ""


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

        initTextChangeListener()
    }

    private fun initTextChangeListener() {
        edtName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var name = s.toString()
                if(!name.isNullOrBlank() && name.length >= 6) {
                    imgSignupFullNameRight.visibility = View.VISIBLE
                } else {
                    imgSignupFullNameRight.visibility = View.GONE
                }
            }
        })

        edtSignupEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var data = s.toString()
                if(!data.isNullOrBlank() && com.tugoapp.mobile.utils.CommonUtils.isEmailValid(data)) {
                    imgSignupEmailRight.visibility = View.VISIBLE
                } else {
                    imgSignupEmailRight.visibility = View.GONE
                }
            }
        })

        edtSignupPswd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var data = s.toString()
                if(!data.isNullOrBlank() && data.length >=6 ) {
                    imgSignupPswdRight.visibility = View.VISIBLE
                } else {
                    imgSignupPswdRight.visibility = View.GONE
                }
            }
        })

        edtConfirmPswd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var data = s.toString()
                if(!data.isNullOrBlank() &&  data.length >=6 && edtSignupPswd.text.toString() == data) {
                    imgSignupConfirmPswdRight.visibility = View.VISIBLE
                } else {
                    imgSignupConfirmPswdRight.visibility = View.GONE
                }
            }
        })
    }

    private fun initControls() {
        btnSignUp.setOnClickListener(View.OnClickListener {
            doValidateAndSignUp()
        })
    }

    private fun doValidateAndSignUp() {
        var fullName = edtName.text.toString()
        var pswd = edtSignupPswd.text.toString()
        var confirmPswd = edtConfirmPswd.text.toString()
        var email = edtSignupEmail.text.toString()

        if(fullName.isNullOrBlank() || pswd.isNullOrBlank() || confirmPswd.isNullOrBlank() || email.isNullOrBlank()) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.err_fill_detail))
            return
        }

        showLoading(getString(R.string.txt_please_wait))
        auth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                //SharedPrefsUtils.setStringPreference(mContext,email,pswd)
                SharedPrefsUtils.setStringPreference(mContext,AppConstant.FULL_NAME,fullName)
                var bundle = bundleOf(AppConstant.IS_FROM_EDIT_PROFILE to false,AppConstant.FIREBASE_EMAIL_ADDRESS to email)
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSignUp_to_fragmentAddPhoneNumber,bundle)
            } else {
                com.tugoapp.mobile.utils.CommonUtils.showSnakeBar(rootView,"Authentication failed"+task.exception?.localizedMessage)
            }
            hideLoading()
        }

    }
}