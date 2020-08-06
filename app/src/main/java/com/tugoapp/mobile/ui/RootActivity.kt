package com.tugoapp.mobile.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseActivity
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_root.*
import javax.inject.Inject


class RootActivity : BaseActivity<RootViewModel?>(), HasSupportFragmentInjector, OnListItemClickListener {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null

    @JvmField
    @Inject
    var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>? = null
    var doubleBackToExitPressedOnce = false
    private var mRootViewModel: RootViewModel? = null
    private var mContext: Context? = null
    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector!!
    }

    override val layoutId: Int
        get() = R.layout.activity_root

    override val viewModel: RootViewModel
        get() = ViewModelProviders.of(this, factory).get(RootViewModel::class.java)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_root)
        mContext = this
        initToolbar()
    }

    private fun initToolbar() {
        val controller = Navigation.findNavController((mContext as Activity?)!!, R.id.fragmentNavHost)
        setSupportActionBar(toolbar)
        NavigationUI.setupWithNavController(toolbar, controller)
        NavigationUI.setupWithNavController(navigationView, controller)
        controller.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragmentSplash || destination.id == R.id.fragmentWalkthrough || destination.id == R.id.fragmentLogin
                    || destination.id == R.id.fragmentSignUp || destination.id == R.id.fragmentWelcome || destination.id == R.id.fragmentCustomizePlan
                    || destination.id == R.id.fragmentSelectPlan || destination.id == R.id.fragmentOrderSummary || destination.id == R.id.fragmentThankYou
                    || destination.id == R.id.fragmentForgotPassword) {
                toolbar.visibility = View.GONE
                navigationView.visibility = View.GONE
            } else if (destination.id == R.id.fragmentOrders || destination.id == R.id.fragmentProfile || destination.id == R.id.fragmentOnGoingOrders
                    || destination.id == R.id.fragmentHistoryOrders || destination.id == R.id.fragmentProviderDetails) {
                toolbar.visibility = View.VISIBLE
                navigationView.visibility = View.VISIBLE
            } else if (destination.id == R.id.fragmentHome || destination.id == R.id.fragmentBrowseAllProviders) {
                toolbar.visibility = View.GONE
                navigationView.visibility = View.VISIBLE
            } else {
                toolbar.visibility = View.VISIBLE
                navigationView.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return (Navigation.findNavController(this, R.id.fragmentNavHost).navigateUp()
                || super.onSupportNavigateUp())
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onListItemClick(position: Int) {

    }
}