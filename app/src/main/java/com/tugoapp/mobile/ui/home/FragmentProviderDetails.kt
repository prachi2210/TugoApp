package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_home.*
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
            CommonUtils.showDialog(mContext,R.id.txtOrderSummaryTitle,R.id.llMessageUs)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun iniUI() {
        mContext = this!!.requireContext()

        imgSampleMenu.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProviderDetails_to_fragmentSampleMenu) })
        btnCustomize.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProviderDetails_to_fragmentSelectPlan) })
    }
}
