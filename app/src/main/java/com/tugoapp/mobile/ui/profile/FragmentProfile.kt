package com.tugoapp.mobile.ui.profile

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.RootActivity
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

class FragmentProfile : BaseFragment<ProfileViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: ProfileViewModel? = null
    lateinit var mContext: Context

    override val layoutId: Int
        get() = R.layout.fragment_profile

    override val viewModel: ProfileViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
            return mViewModel!!
        }
    override val screenTitle: String
        get() = getString(R.string.title_account_settings)

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = this!!.requireContext()
        (activity as RootActivity).supportActionBar?.setHomeButtonEnabled(false)
        (activity as RootActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        llPersonalInfo.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentPersonalInformation) })

        llChangePswd.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentChangePswd) })

        llDeliveryAddress.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentManageAddress) })

        llPaymentMethods.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentPaymentMethods) })

        llLogout.setOnClickListener(View.OnClickListener {
            var builder = CommonUtils.showDialog(mContext, R.string.title_warning, R.string.txt_confirm_logout)
            builder.setOnCancelListener(DialogInterface.OnCancelListener { dialog -> dialog.dismiss() })
            builder.setPositiveButton(R.string.btn_yes){dialogInterface, which ->
               FirebaseAuth.getInstance().signOut()
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProfile_to_fragmentWelcome)
            }
            builder.setNegativeButton(R.string.btn_no){dialogInterface, which ->
                dialogInterface.dismiss()
            }
            builder.create().show()
        })
    }
}