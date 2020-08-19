package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tugoapp.mobile.R
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

        initControllers()

        initObserver()
    }

    private fun initObserver() {
        rvReviews.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        val data = ArrayList<ReviewModel>()
        data.add(ReviewModel("1","Review User 1","","Review meal plan detail",1.0f, "This is a detail review"))
        data.add(ReviewModel("2","Review User 2","","Review meal plan detail",2.0f, "This is a detail review"))
        data.add(ReviewModel("3","Review User 3","","Review meal plan detail",3.0f, "This is a detail review"))
        data.add(ReviewModel("4","Review User 4","","Review meal plan detail",4.0f, "This is a detail review"))


        val adapter = mContext?.let {
            ReviewListAdapter(it, data, object : OnListItemClickListener {
                override fun onListItemClick(position: Int) {
                }
            })
        }
        rvReviews.adapter = adapter
    }

    private fun initControllers() {
    }
}

