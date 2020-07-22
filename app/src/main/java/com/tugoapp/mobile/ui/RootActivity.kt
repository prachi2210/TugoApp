package com.tugoapp.mobile.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseActivity
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_root.*
import javax.inject.Inject


class RootActivity : BaseActivity<RootViewModel?>(), HasSupportFragmentInjector {
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
        setContentView(R.layout.activity_root)
        mContext = this
        initToolbar()
    }

    fun initToolbar() {
        val controller = Navigation.findNavController((mContext as Activity?)!!, R.id.fragmentNavHost)
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, controller)
        controller.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.fragmentSplash || destination.id == R.id.fragmentWalkthrough  || destination.id == R.id.fragmentLogin
                    || destination.id == R.id.fragmentSignUp || destination.id == R.id.fragmentWelcome) {
                toolbar.visibility = View.GONE
                navigationView.visibility = View.GONE
            } else if (destination.id == R.id.fragmentHome || destination.id == R.id.fragmentOrders || destination.id == R.id.fragmentProfile) {
                navigationView.visibility = View.VISIBLE
            } else {
                toolbar.visibility = View.VISIBLE
                navigationView.visibility = View.GONE
            }
        }
        //navigationView.setupWithNavController(controller)
        //navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    /*private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val controller = Navigation.findNavController((mContext as Activity?)!!, R.id.fragmentNavHost)
                if(controller.currentDestination != null && controller.currentDestination!!.id == R.id.fragmentOrders) {
                    Navigation.findNavController(rootView).navigate(R.id.action_fragmentHome_to_fragmentOrders)
                }
                if(controller.currentDestination != null && controller.currentDestination!!.id == R.id.fragmentProfile) {
                    Navigation.findNavController(rootView).navigate(R.id.action_fragmentHome_to_fragmentProfile)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_orders -> {
                val controller = Navigation.findNavController((mContext as Activity?)!!, R.id.fragmentNavHost)

                if(controller.currentDestination != null && controller.currentDestination!!.id == R.id.fragmentHome) {
                    Navigation.findNavController(rootView).navigate(R.id.action_fragmentOrders_to_fragmentHome)
                }
                if(controller.currentDestination != null && controller.currentDestination!!.id == R.id.fragmentProfile) {
                    Navigation.findNavController(rootView).navigate(R.id.action_fragmentOrders_to_fragmentProfile)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                val controller = Navigation.findNavController((mContext as Activity?)!!, R.id.fragmentNavHost)

                if(controller.currentDestination != null && controller.currentDestination!!.id == R.id.fragmentHome) {
                    Navigation.findNavController(rootView).navigate(R.id.action_fragmentProfile_to_fragmentHome)
                }
                if(controller.currentDestination != null && controller.currentDestination!!.id == R.id.fragmentOrders) {
                    Navigation.findNavController(rootView).navigate(R.id.action_fragmentProfile_to_fragmentOrders)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
*/
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_bottom_view_home, menu)
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        return (Navigation.findNavController(this, R.id.fragmentNavHost).navigateUp()
                || super.onSupportNavigateUp())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        checkBackPress()
    }

    private fun checkBackPress() {
        super.onBackPressed()
    }

    private val isDisableBackFragment: Boolean
        private get() {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentNavHost)
            return fragment != null && fragment.childFragmentManager.fragments != null && fragment.childFragmentManager.fragments.size > 0
        }
}