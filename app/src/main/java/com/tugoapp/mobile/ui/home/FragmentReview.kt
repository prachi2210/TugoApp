package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.AddReviewRequestModel
import com.tugoapp.mobile.data.remote.model.request.GetReviewRequestModel
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.data.remote.model.response.MealPlanModel
import com.tugoapp.mobile.data.remote.model.response.ReviewModel
import com.tugoapp.mobile.data.remote.model.response.SampleMenu
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.home.adapters.BrowseByDietListAdapter
import com.tugoapp.mobile.ui.home.adapters.ReviewListAdapter
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_provider_details.*
import kotlinx.android.synthetic.main.fragment_reviews.*
import kotlinx.android.synthetic.main.fragment_sample_menu.*
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselOnScrollListener
import java.util.ArrayList
import javax.inject.Inject


class FragmentReview : BaseFragment<HomeViewModel?>() {

    private var mBusinessId: String? = null

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_reviews

    override val viewModel: HomeViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
            return mViewModel!!
        }

    override val screenTitle: String
        get() = getString(R.string.title_reviews)

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context

        mBusinessId = arguments?.getString(AppConstant.BUSINESS_ID).toString()

        if (mBusinessId.isNullOrEmpty()) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }


        initControllers()

        initObserver()
    }

    private fun initObserver() {
        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { CommonUtils.showSnakeBar(rootView!!, it) })

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

        mViewModel?.mReviewModel?.observe(viewLifecycleOwner, Observer {
            if(it?.reviews != null && it.reviews?.size!! > 0) {
                emptyviewReviews.visibility = View.GONE
                rvReviews.visibility = View.VISIBLE
                rvReviews.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
                val data = ArrayList<ReviewModel>()
                data.addAll(it.reviews!!)
                val adapter = mContext?.let {
                    ReviewListAdapter(it, data, object : OnListItemClickListener {
                        override fun onListItemClick(position: Int) {
                        }
                    })
                }
                rvReviews.adapter = adapter
            }  else {
                emptyviewReviews.visibility = View.VISIBLE
                rvReviews.visibility = View.GONE
            }
        })
    }

    private fun initControllers() {

        mViewModel?.doGetReviews(GetReviewRequestModel(mBusinessId))
    }
}

