package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.GetProviderDetailsData
import com.tugoapp.mobile.data.remote.model.response.GetProviderDetailsResponseModel
import com.tugoapp.mobile.data.remote.model.response.MealPlanModel
import com.tugoapp.mobile.ui.RootActivity
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_provider_details.*
import javax.inject.Inject


class FragmentProviderDetails : BaseFragment<HomeViewModel?>() {
    private lateinit var mSelectedMealPlan: MealPlanModel
    private lateinit var mMealPlanList: ArrayList<MealPlanModel>
    private lateinit var mProviderDetails : GetProviderDetailsData

    private lateinit var mBusinessId: String

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
        get() = getString(R.string.title_provider_detail)

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
        val imgLogo = dialogView.findViewById<ImageView>(R.id.imgProvider)
        val txtProviderName = dialogView.findViewById<TextView>(R.id.txtProviderName)
        val txtProviderDesc = dialogView.findViewById<TextView>(R.id.txtProviderDesc)
        val txtProviderLocation = dialogView.findViewById<TextView>(R.id.txtProviderLocation)
        if(mProviderDetails != null) {
            txtProviderName.text = mProviderDetails.companyName
            txtProviderDesc.text = mProviderDetails.description
            txtProviderLocation.text = mProviderDetails.address
            Glide.with(mContext)
                    .load(mProviderDetails.icon)
                    .centerCrop()
                    .into(imgLogo)
        }

        btnClose.setOnClickListener(View.OnClickListener { providerDialog.dismiss() })
    }

    private fun iniUI() {
        mContext = this!!.requireContext()

        mBusinessId = arguments?.getString(AppConstant.SELECTED_PROVIDER_FOR_PROVIDER_DETAIL).toString()

        if (mBusinessId.isNullOrEmpty()) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.txt_err_no_pref_value))
            return
        }

        mViewModel?.doGetProviderDetails(mBusinessId)

        initControls()
    }

    private fun initControls() {
        imgSampleMenu.setOnClickListener(View.OnClickListener {
            if(mSelectedMealPlan != null && mSelectedMealPlan.sampleMenu?.size!! > 0) {
                var bundle = bundleOf(AppConstant.SAMPLE_MENU_DATA to mMealPlanList)
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProviderDetails_to_fragmentSampleMenu,bundle)
            } else {
                CommonUtils.showToast(mContext,getString(R.string.txt_err_no_sample_menu))
            }
        })
        btnCustomize.setOnClickListener(View.OnClickListener { Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProviderDetails_to_fragmentSelectPlan) })

        mViewModel?.mProvidersDetailData?.observe(viewLifecycleOwner, Observer {
            if(!(it?.planData == null || it.planData!!.size <= 0)) {
                doSetProviderDetails(it)
            } else {
                CommonUtils.showSnakeBar(rootView!!,getString(R.string.txt_err_provider_detail_failed))
            }
        })
    }

    private fun doSetProviderDetails(providerData: GetProviderDetailsData) {
        mProviderDetails = providerData
        (activity as RootActivity).supportActionBar?.title = providerData.companyName
        deliveryDays.text = providerData.deliveryDays
        if(providerData.planData != null && providerData.planData!!.size > 0) {
            mMealPlanList = providerData.planData!!
            for (planDetail in providerData.planData!!) {
                tabMainProvidersType.addTab(tabMainProvidersType.newTab().setText(planDetail.title))
                doSetMealPlanData(providerData.planData!![0])
                mSelectedMealPlan = providerData.planData!![0]
                doSetInfoOrLocationData(0)
                if(planDetail.sampleMenu == null || planDetail.sampleMenu!!.size <= 0) {
                    rlSampleMenu.visibility = View.GONE
                }
            }

            tabMainProvidersType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    if(providerData.planData!!.size > tab.position) {
                        doSetMealPlanData(providerData.planData!!.get(tab.position))
                        mSelectedMealPlan = providerData.planData!!.get(tab.position)
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {

                }
                override fun onTabReselected(tab: TabLayout.Tab) {

                }
            })

            tabInfoLocation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                   doSetInfoOrLocationData(tab.position)
                }
                override fun onTabUnselected(tab: TabLayout.Tab) {

                }
                override fun onTabReselected(tab: TabLayout.Tab) {

                }
            })
        }
    }

    private fun doSetInfoOrLocationData(position: Int) {
        if(mSelectedMealPlan != null) {
            if(position == 0) {
                txtInfoDeliveryLocation.text = mSelectedMealPlan.description
            } else {
                txtInfoDeliveryLocation.text = mSelectedMealPlan.locations
            }
        }
    }

    private fun doSetMealPlanData(mealPlanData: MealPlanModel) {
       if(mealPlanData != null) {
           txtRating.text = mealPlanData.review
           rateBar.rating = mealPlanData.review?.toFloat()!!
           txtStartingFrom.text = mealPlanData.startingFrom
           if(mealPlanData.sampleMenu != null) {
               Glide.with(mContext)
                       .load(mealPlanData.sampleMenu!![0].description)
                       .centerCrop()
                       .into(imgSampleMenu)
           }
       }
    }

    private fun doSetProviderDetails() {

    }
}
