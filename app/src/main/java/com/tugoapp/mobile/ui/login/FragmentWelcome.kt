package com.tugoapp.mobile.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_welcome.*
import javax.inject.Inject

class FragmentWelcome : BaseFragment<WelcomeViewModel?>() {
    private var googleSignInClient: GoogleSignInClient? = null

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: WelcomeViewModel? = null
    var mContext: Context? = null
    private lateinit var auth: FirebaseAuth

    override val layoutId: Int
        get() = R.layout.fragment_welcome

    override val viewModel: WelcomeViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(WelcomeViewModel::class.java)
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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
        googleSignInClient = GoogleSignIn.getClient(mContext!!, gso)
        initControls()
    }

    private fun initControls() {
        btnWelcomeSignIn.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentWelcome_to_fragmentLogin)
        })

        txtWelcomeSignUp.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentWelcome_to_fragmentSignUp)
        })

        btnWelcomeSignInGPlus.setOnClickListener(View.OnClickListener {
            val signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, AppConstant.RC_SIGN_IN)
            showLoading(getString(R.string.txt_please_wait))
        })
    }

    // G+ login
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!,account.email)
            } catch (e: ApiException) {
                CommonUtils.showSnakeBar(rootView!!,e?.localizedMessage)
                hideLoading()
            }
        } else {
            hideLoading()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, email: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        hideLoading()
                        if (email != null) {
                            auth.currentUser?.updateEmail(email)
                        }
                        if(auth.currentUser?.phoneNumber.isNullOrBlank()) {
                            var bundle = bundleOf(AppConstant.FIREBASE_EMAIL_ADDRESS to email)
                            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentWelcome_to_fragmentAddPhoneNumber,bundle)
                        } else {
                            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentWelcome_to_fragmentWalkthrough)
                        }
                    } else {
                        hideLoading()
                        CommonUtils.showSnakeBar(rootView!!,task.exception?.localizedMessage)
                    }
                }
    }
}