package com.tugoapp.mobile.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_verify_otp.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FragmentVerifyOTP : BaseFragment<VerifyOTPViewModel?>() {
    private lateinit var mEmailAddress: String
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: VerifyOTPViewModel? = null
    var mContext: Context? = null
    private var mResendToken : PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var mVerificationId : String
    private lateinit var mPhoneNumber : String

    override val layoutId: Int
        get() = R.layout.fragment_verify_otp

    override val viewModel: VerifyOTPViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(VerifyOTPViewModel::class.java)
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
        mResendToken = arguments?.getParcelable<PhoneAuthProvider.ForceResendingToken>(AppConstant.FIREBASE_RESEND_TOKEN)
        mVerificationId = arguments?.getString(AppConstant.FIREBASE_VERIFICATION_ID).toString()
        mPhoneNumber = arguments?.getString(AppConstant.FIREBASE_PHONE_NUMBER).toString()
        mEmailAddress = arguments?.getString(AppConstant.FIREBASE_EMAIL_ADDRESS).toString()

        if(mResendToken == null || mVerificationId.isNullOrBlank() || mPhoneNumber.isNullOrBlank() || mEmailAddress.isNullOrBlank()) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.txt_err_no_pref_value))
            return
        }
        txtVerifyPhoneSubtitle.setText(String.format(getString(R.string.txt_verify_otp_header_message),mPhoneNumber))
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
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException) {
                    CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_phone_number))
                } else if (e is FirebaseTooManyRequestsException) {
                    CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_firebase_sms_excceed))
                } else {
                    CommonUtils.showSnakeBar(rootView, e.localizedMessage)
                }
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
               CommonUtils.showSnakeBar(rootView,getString(R.string.txt_msg_code_resend))
            }
        }
    }

    private fun initControls() {
        btnOtpContinue.setOnClickListener(View.OnClickListener {
            doVerifyOTP()
        })

        txtOtpResend.setOnClickListener(View.OnClickListener {
            doResendToken()
        })
    }

    private fun doResendToken() {
        activity?.let {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    mPhoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    it,
                    mCallbacks,
                    mResendToken)
        }
    }

    private fun doVerifyOTP() {
        var otpData = otp.text.toString()
        if(otpData.isNullOrBlank() || otpData.length < 6) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.txt_err_valid_otp))
            return
        }

        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, otpData)
        doLinkAccount(credential)
    }

    private fun doLinkAccount(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().currentUser?.linkWithCredential(credential)?.addOnCompleteListener{
            task ->
            if(task.isSuccessful) {
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentVerifyOTP_to_fragmentWalkthrough)
            } else {
                CommonUtils.showSnakeBar(rootView,task.exception?.localizedMessage)
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentVerifyOTP_to_fragmentWalkthrough)
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    CommonUtils.showToast(mContext,getString(R.string.txt_err_valid_otp))
                }
            }
        }
    }
}