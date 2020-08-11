package com.tugoapp.mobile.ui.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.RootActivity
import com.tugoapp.mobile.utils.CommonUtils
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment<V : BaseViewModel?> : Fragment() {
    var baseActivity: BaseActivity<*>? = null
    var rootView: View? = null
        private set
    private var mViewModel: V? = null
    private var mProgressDialog: ProgressDialog? = null

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int
    abstract val screenTitle: String

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val viewModel: V
    var mActivityContext: Context? = null
    val currentViewModel: V?
        get() = mViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            val activity = context
            baseActivity = activity
            activity.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)
        mViewModel = viewModel
        mActivityContext = activity
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        if (viewDataBinding == null) viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        if (rootView == null) rootView = inflater.inflate(layoutId, container, false)
        return rootView
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
//        viewDataBinding!!.lifecycleOwner = this
//        viewDataBinding!!.executePendingBindings()
        (activity as RootActivity).supportActionBar?.setTitle(screenTitle)

    }

    fun hideKeyboard() {
        baseActivity?.hideKeyboard()
    }

    fun openActivityOnTokenExpire() {
        baseActivity?.openActivityOnTokenExpire()
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            Handler().postDelayed({ mProgressDialog!!.cancel() }, 800)
        }
    }

    fun showLoading() {
        //hideLoading();
        if (mProgressDialog == null || !mProgressDialog!!.isShowing) mProgressDialog = mActivityContext?.let { CommonUtils.showLoadingDialog(it, "") }
    }

    fun showLoading(message: String?) {
        //hideLoading();
        if (mProgressDialog == null || !mProgressDialog!!.isShowing) mProgressDialog = mActivityContext?.let { message?.let { it1 -> CommonUtils.showLoadingDialog(it, it1) } }
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    interface Callback {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String?)
    }
}