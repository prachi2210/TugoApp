package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_provider_details.*
import javax.inject.Inject


class FragmentProviderDetails : BaseFragment<HomeViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    lateinit var mContext: Context

    override val layoutId: Int
        get() = R.layout.fragment_provider_details

    override val viewModel: HomeViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
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
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_providerdetail,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_fav) {

        } else if(item.itemId == R.id.menu_info) {
            showProviderDetailDialog()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showProviderDetailDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_providr_info, null)
        val providerDialog = AlertDialog.Builder(mContext)
                .setView(dialogView)
                .show()
        val btnClose = dialogView.findViewById<ImageView>(R.id.imgClose)
        btnClose.setOnClickListener(View.OnClickListener { providerDialog.dismiss() })
    }

    private fun iniUI() {
        mContext = this!!.requireContext()

        imgSampleMenu.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProviderDetails_to_fragmentSampleMenu) })
        btnCustomize.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProviderDetails_to_fragmentSelectPlan) })
    }
}
