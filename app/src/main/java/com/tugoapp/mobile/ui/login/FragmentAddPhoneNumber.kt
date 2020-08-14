package com.tugoapp.mobile.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.SaveUserDetailRequestModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_add_phone_number.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FragmentAddPhoneNumber : BaseFragment<AddPhoneNumberViewModel?>() {
    private lateinit var mEmailAddress: String
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: AddPhoneNumberViewModel? = null
    var mContext: Context? = null
    private var mResendToken : PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var mVerificationId : String
    private lateinit var mPhoneNumber : String
    private var mIsResend : Boolean = false

    override val layoutId: Int
        get() = R.layout.fragment_add_phone_number

    override val viewModel: AddPhoneNumberViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(AddPhoneNumberViewModel::class.java)
            return mViewModel!!
        }

    override val screenTitle: String
        get() = getString(R.string.title_create_an_acnt)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context
        mEmailAddress = arguments?.getString(AppConstant.FIREBASE_EMAIL_ADDRESS).toString()
        if(mEmailAddress.isNullOrBlank()) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.txt_err_no_pref_value))
            return
        }
        initControls()
        initCallbacks()
    }

    private fun initControls() {
        btnAddPhoneSignUp.setOnClickListener(View.OnClickListener {
            doValidateAndAuthenticateNumber()
        })

        btnOtpContinue.setOnClickListener(View.OnClickListener {
            doVerifyOTP()
        })

        txtOtpResend.setOnClickListener(View.OnClickListener {
            doResendToken()
        })

        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { CommonUtils.showSnakeBar(rootView!!,it)})

        mViewModel?.mIsUserDetailSubmitted?.observe(viewLifecycleOwner, Observer {
            if(it == 1) {
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentAddPhoneNumber_to_fragmentWalkthrough)
            } else {
                CommonUtils.showSnakeBar(rootView!!,getString(R.string.txt_err_fail_user_detail))
            }
        })
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
                doUpdateServerForUserDetail()
            } else {
                CommonUtils.showSnakeBar(rootView,task.exception?.localizedMessage)
            }
            hideLoading()
        }
    }

    private fun doUpdateServerForUserDetail() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener {
            task ->
            if (task.isSuccessful) {
                mViewModel?.doSaveUserDetailOnServer(task.result?.token,SaveUserDetailRequestModel(mEmailAddress,mPhoneNumber,"test",FirebaseAuth.getInstance().currentUser?.uid))
            } else {
        }
        }
    }

    private fun doResendToken() {
        mIsResend = true
        showLoading(getString(R.string.txt_resending_otp))
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

    private fun initCallbacks() {
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                 otp.setText( credential.smsCode)
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
//                var bundle = bundleOf(AppConstant.FIREBASE_VERIFICATION_ID to verificationId,
//                        AppConstant.FIREBASE_RESEND_TOKEN to token, AppConstant.FIREBASE_PHONE_NUMBER to edtPhone.text.toString(),
//                        AppConstant.FIREBASE_EMAIL_ADDRESS to mEmailAddress)
                mVerificationId = verificationId
                mResendToken = token
                mPhoneNumber = edtPhone.text.toString()
                hideLoading()
                if(!mIsResend) {
                    doShowOTPView(true)
                } else {
                    mIsResend = false
                }
            }
        }
    }

    private fun doShowOTPView(isShow: Boolean) {
        if(isShow) {
            llAddPhone.visibility = View.GONE
            llVerifyOTP.visibility = View.VISIBLE
            txtVerifyPhoneSubtitle.text = String.format(getString(R.string.txt_verify_otp_header_message),mPhoneNumber)
        } else {
            llAddPhone.visibility = View.VISIBLE
            llVerifyOTP.visibility = View.GONE
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        showLoading(getString(R.string.txt_otp_autodetected))
        doLinkAccount(credential)
    }

    private fun doValidateAndAuthenticateNumber() {

        var phone = edtPhone.text.toString()
        if (phone.isNullOrBlank()) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_phone_number))
            return
        }

        showLoading(getString(R.string.txt_sending_code))
        activity?.let {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,
                    60,
                    TimeUnit.SECONDS,
                    it,
                    mCallbacks)
        }
    }
}
