package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.RootActivity
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_change_pswd.*
import javax.inject.Inject

class FragmentChangePswd : BaseFragment<ChangePswdViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: ChangePswdViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_change_pswd

    override val viewModel: ChangePswdViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(ChangePswdViewModel::class.java)
            return mViewModel!!
        }

    override val screenTitle: String
        get() = getString(R.string.txt_change_pswd)

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }


    private fun iniUI() {
        mContext = context

        btnChangePswd.setOnClickListener(View.OnClickListener {
            doChangePswd()
        })
    }

    private fun doChangePswd() {
        var pswd = edtPswd.text.toString()
        var newPswd = edtNewPswd.text.toString()
        var confirmPswd = edtConfirmPswd.text.toString()
        if(pswd.isNullOrBlank() || pswd.length < 6) {
                CommonUtils.showSnakeBar(rootView,getString(R.string.err_enter_pswd))
            return
        }

        if(newPswd.isNullOrBlank() || newPswd.length < 6) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.err_enter_new_pswd))
            return
        }

        if(confirmPswd.isNullOrBlank() || confirmPswd.length < 6 || !newPswd.equals(confirmPswd)) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.err_enter_confirm_pswd))
            return
        }

        var user = FirebaseAuth.getInstance().currentUser

        if(user?.providerId.equals("firebase",false)) {

            var credential = user?.email?.let { EmailAuthProvider.getCredential(it, pswd) };
            if (credential != null) {
                showLoading(getString(R.string.txt_changing_pswd))
                user?.reauthenticate(credential)?.addOnCompleteListener(OnCompleteListener<Void>() { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPswd).addOnCompleteListener(OnCompleteListener<Void>() { taskUpdate ->
                            hideLoading()
                            if (taskUpdate.isSuccessful) {
                                Navigation.findNavController(rootView!!).popBackStack()
                                CommonUtils.showSnakeBar(rootView, getString(R.string.txt_change_pswd_success))
                            } else {
                                CommonUtils.showSnakeBar(rootView, getString(R.string.txt_change_pswd_fail))
                            }
                        });
                    } else {
                        hideLoading()
                        CommonUtils.showSnakeBar(rootView, getString(R.string.err_wrong_pswd))
                    }
                })
            };
        } else {
            CommonUtils.showSnakeBar(rootView,getString(R.string.txt_err_cant_change_gplus_pswd))
        }
    }
}