package com.tugoapp.mobile.ui.base

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.tugoapp.mobile.ui.RootViewModel
import com.tugoapp.mobile.ui.base.NetworkStateChangeListener.NetworkStateReceiverListener
import com.tugoapp.mobile.utils.NetworkUtils
import dagger.android.AndroidInjection

abstract class BaseActivity<V : BaseViewModel?> : AppCompatActivity(), BaseFragment.Callback, NetworkStateReceiverListener {

    private var mViewModel: V? = null
    private var mNetworkStatusListener: NetworkStateChangeListener? = null

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V
    override fun onFragmentAttached() {}
    override fun onFragmentDetached(tag: String?) {}
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        mNetworkStatusListener = NetworkStateChangeListener(this)
        performDataBinding()
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkConnected(applicationContext)

    fun openActivityOnTokenExpire() {
        //startActivity(FragmentLogin.newIntent(this));
        finish()
    }

    fun performDependencyInjection() {
        AndroidInjection.inject(this)
    }

    private fun performDataBinding() {
      //  viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mViewModel = if (mViewModel == null) viewModel else mViewModel
//        viewDataBinding?.setVariable(bindingVariable, mViewModel)
//        viewDataBinding?.executePendingBindings()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(mNetworkStatusListener, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mNetworkStatusListener != null) {
            unregisterReceiver(mNetworkStatusListener)
        }
    }

    override fun networkAvailable() {}
    override fun networkUnavailable() {}
}