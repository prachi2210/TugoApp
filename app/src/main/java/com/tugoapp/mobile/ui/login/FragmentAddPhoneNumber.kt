package com.tugoapp.mobile.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_add_phone_number.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FragmentAddPhoneNumber : BaseFragment<AddPhoneNumberViewModel?>() {
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: AddPhoneNumberViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_add_phone_number

    override val viewModel: AddPhoneNumberViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(AddPhoneNumberViewModel::class.java)
            return mViewModel!!
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context
        initControls()
        initCallbacks()
    }

    private fun initCallbacks() {
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
               // signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e is FirebaseTooManyRequestsException) {
                    CommonUtils.showToast(mContext,"SMS limit excceed")
                }

                // Show a message and update the UI
                // ...
            }

            override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
               // storedVerificationId = verificationId
                //resendToken = token
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentAddPhoneNumber_to_fragmentVerifyOTP)
            }
        }
    }

    private fun initControls() {
        btnAddPhoneSignUp.setOnClickListener(View.OnClickListener {
            doValidateAndAuthenticateNumber()
        })
    }

    private fun doValidateAndAuthenticateNumber() {

//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "7016500705",
//                60,
//                TimeUnit.SECONDS,
//                this,
//                mCallbacks)
    }
}
