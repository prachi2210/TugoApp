package com.tugoapp.mobile.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.SaveUserDetailRequestModel
import com.tugoapp.mobile.data.remote.model.response.UserDetailModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import com.tugoapp.mobile.utils.NetworkUtils
import com.tugoapp.mobile.utils.SharedPrefsUtils
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import javax.inject.Inject

class FragmentLogin : BaseFragment<AddPhoneNumberViewModel?>() {
    private var mFacebookCallbackManager: CallbackManager? = null
    private var googleSignInClient: GoogleSignInClient? = null

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: AddPhoneNumberViewModel? = null
    var mContext: Context? = null
    private lateinit var auth: FirebaseAuth

    private var mUserEmailToStore : String? = null
    private var mUserPhoneToStore : String? = null
    private var mUserDisplayNameToStore : String? = null


    override val layoutId: Int
        get() = R.layout.fragment_login

    override val viewModel: AddPhoneNumberViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(AddPhoneNumberViewModel::class.java)
            return mViewModel!!
        }

    override val screenTitle: String
        get() = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
        googleSignInClient = GoogleSignIn.getClient(mContext!!, gso)

        mFacebookCallbackManager = CallbackManager.Factory.create()
        btnDummyFbLoginPage.fragment = this
        btnDummyFbLoginPage.setPermissions(listOf("email"))
        btnDummyFbLoginPage.registerCallback(mFacebookCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                CommonUtils.showSnakeBar(rootView, "Facebook login cancelled ")
            }

            override fun onError(error: FacebookException) {
                CommonUtils.showSnakeBar(rootView, "Facebook login failed " + error.localizedMessage)
            }
        })

        initControls()
        initObserver()
        initTextChange()
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        if (credential != null) {
            auth.signInWithCredential(credential).addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) {
                    var email = ""
                    var request = GraphRequest.newMeRequest(accessToken) { jsonData, response ->
                        if (response?.error != null) {
                            CommonUtils.showSnakeBar(rootView, "Failed to retrieve email id from facebook")
                        } else {
                            email = jsonData?.optString("email").toString()
                            //SharedPrefsUtils.setStringPreference(mContext, AppConstant.LOGGED_IN_EMAIL, email)
                        }

                        if (auth.currentUser?.phoneNumber.isNullOrBlank()) {
                            SharedPrefsUtils.setStringPreference(mContext, AppConstant.FULL_NAME, auth.currentUser?.displayName)
                            SharedPrefsUtils.setBooleanPreference(mContext, AppConstant.IS_LOGGED_IN, true)
                            SharedPrefsUtils.setStringPreference(mContext, AppConstant.LOGGED_IN_EMAIL, email)
                            var bundle = bundleOf(AppConstant.IS_FROM_EDIT_PROFILE to false, AppConstant.FIREBASE_EMAIL_ADDRESS to email)
                            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentLogin_to_fragmentAddPhoneNumber, bundle)
                        } else {
                            mUserEmailToStore = email
                            mUserPhoneToStore = auth.currentUser?.phoneNumber
                            mUserDisplayNameToStore = auth.currentUser?.displayName
                            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    try {
//                                        mViewModel?.doSaveUserDetailOnServer(task.result?.token, SaveUserDetailRequestModel(mUserEmailToStore, mUserPhoneToStore,
//                                                mUserDisplayNameToStore,
//                                                FirebaseAuth.getInstance().currentUser?.uid,
//                                                mContext?.let { CommonUtils.getDeviceId(it) }, SharedPrefsUtils.getStringPreference(mContext,AppConstant.PREF_KEY_PUSH_TOKEN),
//                                                "android", TimeZone.getDefault()?.displayName))

                                        mViewModel?.checkIfUserExist(task.result?.token)
                                    } catch (e: IOException) {
                                    }
                                } else {
                                    //CommonUtils.showSnakeBar(rootView,getString(R.string.txt_fail_save_user_server))
                                }
                            }
                        }
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,email")
                    request.parameters = parameters
                    request.executeAsync()
                } else {
                    CommonUtils.showSnakeBar(rootView, "Facebook authentication failed.")
                }
            })
        }
    }

    private fun initTextChange() {
        edtEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var data = s.toString()
                if (!data.isNullOrBlank() && com.tugoapp.mobile.utils.CommonUtils.isEmailValid(data)) {
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
                if (!data.isNullOrBlank() && data.length >= 6) {
                    imgSigninPswdRight.visibility = View.VISIBLE
                } else {
                    imgSigninPswdRight.visibility = View.GONE
                }
            }
        })

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

        mViewModel?.mIsUserDetailExistOnServer?.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                SharedPrefsUtils.setBooleanPreference(mContext, AppConstant.IS_LOGGED_IN, true)
                SharedPrefsUtils.setStringPreference(mContext, AppConstant.FULL_NAME, mUserDisplayNameToStore)
                SharedPrefsUtils.setStringPreference(mContext, AppConstant.LOGGED_IN_EMAIL, mUserEmailToStore)
                SharedPrefsUtils.setStringPreference(mContext, AppConstant.LOGGED_IN_PHONE, mUserPhoneToStore)
                if (SharedPrefsUtils.didUserSeenWalkthrough(mContext!!, auth.currentUser?.uid)) {
                    Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentLogin_to_fragmentHome)
                } else {
                    SharedPrefsUtils.setWalkthroughForUser(mContext!!, auth.currentUser?.uid)
                    Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentLogin_to_fragmentWalkthrough)
                }
            } else {
               // CommonUtils.showSnakeBar(rootView!!,getString(R.string.txt_err_fail_user_detail))
                var model = UserDetailModel(mUserEmailToStore,mUserDisplayNameToStore,mUserPhoneToStore,true,true,true)
                var bundle  = bundleOf(AppConstant.USER_DETAIL_DATA to model,AppConstant.KEY_IS_PROFILE_FROM_LOGIN to true)
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentLogin_to_fragmentPersonalInformation,bundle)
            }
        })
    }

    private fun initControls() {
        btnLoginSignInFb.setOnClickListener(View.OnClickListener {
            if(NetworkUtils.isNetworkConnected(requireContext())) {
                btnDummyFbLoginPage.performClick()
            } else {
                CommonUtils.showSnakeBar(rootView,getString(R.string.txt_no_internet))
            }
        })

        txtLoginSignUp.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentLogin_to_fragmentSignUp)
        })

        txtForgotPswd.setOnClickListener(View.OnClickListener {
            if(NetworkUtils.isNetworkConnected(requireContext())) {
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentLogin_to_fragmentForgotPswd)
            } else {
                CommonUtils.showSnakeBar(rootView,getString(R.string.txt_no_internet))
            }
        })

        btnLoginSignInGPlus.setOnClickListener(View.OnClickListener {
            if(NetworkUtils.isNetworkConnected(requireContext())) {
                val signInIntent = googleSignInClient?.signInIntent
                startActivityForResult(signInIntent, AppConstant.RC_SIGN_IN)
                showLoading(getString(R.string.txt_please_wait))
            } else {
                CommonUtils.showSnakeBar(rootView,getString(R.string.txt_no_internet))
            }
        })

        btnLoginSignIn.setOnClickListener(View.OnClickListener {
            if(NetworkUtils.isNetworkConnected(requireContext())) {
                doValidateAndLogin()
            } else {
                CommonUtils.showSnakeBar(rootView,getString(R.string.txt_no_internet))
            }
        })
    }

    private fun doValidateAndLogin() {

        var email = edtEmail.text.toString()
        var pswd = edtPswd.text.toString()

        if (email.isNullOrBlank() || pswd.isNullOrBlank()) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.err_fill_detail))
            return
        }

        auth.signInWithEmailAndPassword(email, pswd).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                val phoneNumber = FirebaseAuth.getInstance().currentUser?.phoneNumber
                if (!email.isNullOrBlank() && phoneNumber.isNullOrBlank()) {
                    SharedPrefsUtils.setBooleanPreference(mContext, AppConstant.IS_LOGGED_IN, true)
                    SharedPrefsUtils.setStringPreference(mContext, AppConstant.FULL_NAME, auth.currentUser?.displayName)
                    SharedPrefsUtils.setStringPreference(mContext, AppConstant.LOGGED_IN_EMAIL, email)
                    val bundle = bundleOf(AppConstant.IS_FROM_EDIT_PROFILE to false, AppConstant.FIREBASE_EMAIL_ADDRESS to email)
                    Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentLogin_to_fragmentAddPhoneNumber, bundle)
                } else {
                    mUserEmailToStore = email
                    mUserPhoneToStore = phoneNumber
                    mUserDisplayNameToStore = auth.currentUser?.displayName
                    FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                                try {
//                                    mViewModel?.doSaveUserDetailOnServer(task.result?.token, SaveUserDetailRequestModel(mUserEmailToStore, mUserPhoneToStore,
//                                            mUserDisplayNameToStore,
//                                            FirebaseAuth.getInstance().currentUser?.uid,
//                                            mContext?.let { CommonUtils.getDeviceId(it) }, SharedPrefsUtils.getStringPreference(mContext,AppConstant.PREF_KEY_PUSH_TOKEN),
//                                            "android", TimeZone.getDefault()?.displayName))
                                    mViewModel?.checkIfUserExist(task.result?.token)
                                } catch (e: IOException) {
                                }
                        } else {
                            //CommonUtils.showSnakeBar(rootView,getString(R.string.txt_fail_save_user_server))
                        }
                    }
                }
            } else {
                CommonUtils.showSnakeBar(rootView, task.exception?.localizedMessage)
            }
        }
    }

    // G+ login
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!, account.email)
            } catch (e: ApiException) {
                CommonUtils.showSnakeBar(rootView!!, e.localizedMessage)
                hideLoading()
            }
        } else {
            mFacebookCallbackManager?.onActivityResult(requestCode, resultCode, data)
            hideLoading()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(idToken: String, email: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hideLoading()
                        if (email != null) {
                            auth.currentUser?.updateEmail(email)
                            if (auth.currentUser?.phoneNumber.isNullOrBlank()) {
                                SharedPrefsUtils.setBooleanPreference(mContext, AppConstant.IS_LOGGED_IN, true)
                                SharedPrefsUtils.setStringPreference(mContext, AppConstant.LOGGED_IN_EMAIL, email)
                                SharedPrefsUtils.setStringPreference(mContext, AppConstant.FULL_NAME, auth.currentUser?.displayName)
                                var bundle = bundleOf(AppConstant.IS_FROM_EDIT_PROFILE to false, AppConstant.FIREBASE_EMAIL_ADDRESS to email)
                                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentLogin_to_fragmentAddPhoneNumber, bundle)
                            } else {
                                mUserEmailToStore = email
                                mUserPhoneToStore = auth.currentUser?.phoneNumber
                                mUserDisplayNameToStore = auth.currentUser?.displayName
                                FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        try {
//                                            mViewModel?.doSaveUserDetailOnServer(task.result?.token, SaveUserDetailRequestModel(mUserEmailToStore, mUserPhoneToStore,
//                                                    mUserDisplayNameToStore,
//                                                    FirebaseAuth.getInstance().currentUser?.uid,
//                                                    mContext?.let { CommonUtils.getDeviceId(it) }, SharedPrefsUtils.getStringPreference(mContext,AppConstant.PREF_KEY_PUSH_TOKEN),
//                                                    "android", TimeZone.getDefault()?.displayName))
                                            mViewModel?.checkIfUserExist(task.result?.token)
                                        } catch (e: IOException) {
                                        }
                                    } else {
                                        //CommonUtils.showSnakeBar(rootView,getString(R.string.txt_fail_save_user_server))
                                    }
                                }
                            }
                        } else {
                            CommonUtils.showSnakeBar(rootView!!, "Google login succeed, but email address not found with account")
                        }
                    } else {
                        hideLoading()
                        CommonUtils.showSnakeBar(rootView!!, task.exception?.localizedMessage)
                    }
                }
    }
}