package com.tugoapp.mobile.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.internal.common.CommonUtils
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
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
            com.tugoapp.mobile.utils.CommonUtils.showToast(mContext,getString(R.string.err_fill_detail))
            return
        }

        auth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                val firebaseUser = auth.currentUser!!
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentSignUp_to_fragmentAddPhoneNumber)
            } else {
                com.tugoapp.mobile.utils.CommonUtils.showToast(mContext,"Authentication failed"+task.exception?.localizedMessage)
            }
        }

    }
}