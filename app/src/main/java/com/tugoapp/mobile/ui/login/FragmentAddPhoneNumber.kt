package com.tugoapp.mobile.ui.login

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.SaveUserDetailRequestModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.login.adapter.CountryListAdapter
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import com.tugoapp.mobile.utils.SharedPrefsUtils
import kotlinx.android.synthetic.main.fragment_add_phone_number.*
import java.io.IOException
import java.util.*
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
    private var mPhoneNumber : String? = null
    private var mIsResend : Boolean = false
    private var mIsFromEditProfile = false

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
        mPhoneNumber = arguments?.getString(AppConstant.FIREBASE_PHONE_NUMBER).toString()
        mIsFromEditProfile = arguments?.getBoolean(AppConstant.IS_FROM_EDIT_PROFILE)!!

        if(mEmailAddress.isNullOrBlank()) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.txt_err_no_pref_value))
            return
        }

        if(mIsFromEditProfile) {
            spinnerCountry.visibility = View.GONE
            edtPhone.setText(mPhoneNumber)
        } else {
            spinnerCountry.visibility = View.VISIBLE
            mViewModel?.doLoadCountry()
        }

        initControls()
        initObserver()
        initCallbacks()
    }

    private fun initObserver() {
        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { CommonUtils.showSnakeBar(rootView!!,it)})

        mViewModel?.mShowProgress?.observe(viewLifecycleOwner, Observer {
            if(it.first) {
                if(it.second.isNullOrBlank()) {
                    showLoading()
                } else {
                    showLoading(it.second)
                }
            } else {
                hideLoading()
            }
        })

        mViewModel?.mCountryData?.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                val adapter = mContext?.let { it1 ->
                    CountryListAdapter(it1, it)
                }
                spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        edtPhone.setText(it[position].dial_code)
                    }

                }
                spinnerCountry.adapter = adapter
            }
        })
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
                SharedPrefsUtils.setStringPreference(mContext,AppConstant.LOGGED_IN_PHONE,mPhoneNumber)
                if(mIsFromEditProfile) {
                    Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentAddPhoneNumber_to_fragmentProfile)
                    CommonUtils.showSnakeBar(rootView,"User details update successfully")
                } else {
                    SharedPrefsUtils.setWalkthroughForUser(mContext!!, FirebaseAuth.getInstance().currentUser?.uid)
                    Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentAddPhoneNumber_to_fragmentWalkthrough)
                }
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
        if(mIsFromEditProfile) {
            FirebaseAuth.getInstance().currentUser?.unlink("phone")?.addOnCompleteListener(OnCompleteListener {
                FirebaseAuth.getInstance().currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        doUpdateServerForUserDetail()
                    } else {
                        CommonUtils.showSnakeBar(rootView, task.exception?.localizedMessage)
                    }
                    hideLoading()
                }
            })
        } else {
            FirebaseAuth.getInstance().currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    doUpdateServerForUserDetail()
                } else {
                    CommonUtils.showSnakeBar(rootView, task.exception?.localizedMessage)
                }
                hideLoading()
            }
        }
    }

    private fun doUpdateServerForUserDetail() {
        FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener {
            task ->
            if (task.isSuccessful) {
                Thread(Runnable {
                    try {
                        mViewModel?.doSaveUserDetailOnServer(task.result?.token, SaveUserDetailRequestModel(mEmailAddress, mPhoneNumber,
                                SharedPrefsUtils.getStringPreference(mContext, AppConstant.FULL_NAME, "NO-NAME"),
                                FirebaseAuth.getInstance().currentUser?.uid,
                                mContext?.let { CommonUtils.getDeviceId(it) }, mContext?.let { SharedPrefsUtils.getPushToken(it,AppConstant.PREF_KEY_PUSH_TOKEN) },
                                "android", TimeZone.getDefault()?.displayName))
                    } catch (e: IOException) {
                    }
                }).start()
            } else {
                //CommonUtils.showSnakeBar(rootView,getString(R.string.txt_fail_save_user_server))
            }
        }
    }

    private fun doResendToken() {
        if(mPhoneNumber != null) {
            mIsResend = true
            showLoading(getString(R.string.txt_resending_otp))
            activity?.let {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        mPhoneNumber!!,
                        60,
                        TimeUnit.SECONDS,
                        it,
                        mCallbacks,
                        mResendToken)
            }
        } else {
            CommonUtils.showSnakeBar(rootView,getString(R.string.txt_err_no_phonenumber))
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
                hideLoading()
                 otp.setText( credential.smsCode)
                 signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                hideLoading()
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
