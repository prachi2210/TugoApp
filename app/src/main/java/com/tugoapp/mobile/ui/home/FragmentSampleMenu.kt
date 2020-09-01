package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.data.remote.model.response.MealPlanModel
import com.tugoapp.mobile.data.remote.model.response.SampleMenu
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_provider_details.*
import kotlinx.android.synthetic.main.fragment_sample_menu.*
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselOnScrollListener
import java.util.ArrayList
import javax.inject.Inject


class FragmentSampleMenu : BaseFragment<HomeViewModel?>() {
    private var mCompanyLogo: String? = null
    private var mSelectedMealPlan: Int = 0
    private var mSampleMenuList : ArrayList<MealPlanModel>? = null

    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_sample_menu

    override val viewModel: HomeViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
            return mViewModel!!
        }

    override val screenTitle: String
        get() = getString(R.string.title_sample_menu)

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()
    }

    private fun iniUI() {
        mContext = context
        mSampleMenuList = arguments?.getParcelableArrayList(AppConstant.SAMPLE_MENU_DATA)
        mCompanyLogo = arguments?.getString(AppConstant.SAMPLE_MENU_COMPANYLOGO)
        if(mSampleMenuList == null ) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.txt_err_no_pref_value))
            return
        }

        initControllers()
    }

    private fun initControllers() {
        if(mCompanyLogo != null)
        mContext?.let {
            Glide.with(it)
                .load(mCompanyLogo)
                .centerCrop()
                .into(imgCompanyLogoSampleMenu)
        }

        if(mSampleMenuList != null ) {
            for (planDetail in mSampleMenuList!!) {
                tabSampleMenu.addTab(tabSampleMenu.newTab().setText(planDetail.title))
                doSetMenuImages(0)
                mSelectedMealPlan = 0
                if (planDetail.sampleMenu == null || planDetail.sampleMenu!!.size <= 0) {
                    llMenu.visibility = View.GONE
                }
            }
        }
        tabSampleMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if(mSampleMenuList?.size!! > tab.position) {
                  doSetMenuImages(tab.position)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


    }

    private fun doSetMenuImages(position: Int) {
        val list = mutableListOf<CarouselItem>()

        for(sample in mSampleMenuList?.get(position)?.sampleMenu!!) {
            list.add(
                    CarouselItem(
                            imageUrl = sample.imagePath,
                            caption = sample.title
                    )
            )
        }
        imageSampleCarousel.addData(list)
        imageSampleCarousel.onScrollListener = object : CarouselOnScrollListener {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int, pos: Int, carouselItem: CarouselItem?) {
                if(mSampleMenuList?.get(position)?.sampleMenu?.size!! > position) {
                    txtTitleImage.text =  mSampleMenuList?.get(position)?.sampleMenu!![pos].title
                    txtSubtitleImage.text =  mSampleMenuList?.get(position)?.sampleMenu!![pos].description
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // ...
            }
        }
        imageSampleCarousel.setIndicator(custom_indicator)
    }
}

