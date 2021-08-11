package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.GetProvidersRequestModel
import com.tugoapp.mobile.data.remote.model.request.SubmitQueryRequestModel
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.data.remote.model.response.GetProviderDetailsData
import com.tugoapp.mobile.data.remote.model.response.MealPlanModel
import com.tugoapp.mobile.ui.RootActivity
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.home.adapters.CategoryListAdapter
import com.tugoapp.mobile.ui.home.adapters.PlanListAdapter
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_browse_all_providers.*
import kotlinx.android.synthetic.main.fragment_provider_details.*
import javax.inject.Inject


class FragmentProviderDetails : BaseFragment<HomeViewModel?>() {
    private var mMenu: Menu? = null
    private lateinit var mSelectedMealPlan: MealPlanModel
    private lateinit var mMealPlanList: ArrayList<MealPlanModel>
    private var mProviderDetails: GetProviderDetailsData? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        iniUI()
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_providerdetail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        mMenu = menu
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_fav) {
            mViewModel?.doSetProviderFaviroute(mBusinessId)
            if(mProviderDetails?.isFavourite != null && mProviderDetails?.isFavourite!!) {
                item.setIcon(R.drawable.ic_heart)
            } else {
                item.setIcon(R.drawable.ic_fav_selected)
            }
        } else if (item.itemId == R.id.menu_info) {
            showProviderDetailDialog()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun iniUI() {
        mContext = this.requireContext()

        mBusinessId = arguments?.getString(AppConstant.SELECTED_PROVIDER_FOR_PROVIDER_DETAIL).toString()

        if (mBusinessId.isNullOrEmpty()) {
            CommonUtils.showSnakeBar(rootView, getString(R.string.txt_err_no_pref_value))
            return
        }

        if(mProviderDetails == null)
        mViewModel?.doGetProviderDetails(mBusinessId)

        initControls()
        
        initObservers()
    }

    private fun initObservers() {
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

            mViewModel?.mProvidersDetailData?.observe(viewLifecycleOwner, Observer {
                if (!(it?.planData == null || it.planData!!.size <= 0)) {
                    doSetProviderDetails(it)
                } else {
                    CommonUtils.showSnakeBar(rootView!!, getString(R.string.txt_err_provider_detail_failed))
                }
            })

        mViewModel?.mSubmitQueryResponse?.observe(viewLifecycleOwner, Observer {
            if(it.first == 1) {
                doShowSuccessDialog(it.second)
            } else {
                CommonUtils.showSnakeBar(rootView,it.second)
            }
        })
    }

    private fun doShowSuccessDialog(message: String?) {
        val li = LayoutInflater.from(context)
        val promptsView: View = li.inflate(R.layout.dialog_thankyou, null)
        val alertDialogBuilder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        alertDialogBuilder.setView(promptsView)
        alertDialogBuilder.setCancelable(false)
        var btnBack = promptsView.findViewById<AppCompatButton>(R.id.btnBackToHomeComment)
        var txtMessage = promptsView.findViewById<TextView>(R.id.txtMessage)
        btnBack.setText(R.string.done)
        if(message != null) {
            txtMessage.setText(message)
        }
        var dialog = alertDialogBuilder.create()

        btnBack.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        dialog.show()
    }
    private fun initControls() {
        imgSampleMenu.setOnClickListener(View.OnClickListener {
            if (mSelectedMealPlan != null && mSelectedMealPlan.sampleMenu?.size!! > 0) {
                var bundle = bundleOf(AppConstant.SAMPLE_MENU_DATA to mMealPlanList, AppConstant.SAMPLE_MENU_COMPANYLOGO to mProviderDetails?.companyLogo)
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProviderDetails_to_fragmentSampleMenu, bundle)
            }
        })
        btnCustomize.setOnClickListener(View.OnClickListener {
            var bundle = bundleOf(AppConstant.SELECTED_MEAL_PLAN to mSelectedMealPlan,AppConstant.BUSINESS_ID to mBusinessId)
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProviderDetails_to_fragmentSelectPlan,bundle)
        })
        
        txtLetusKnow.setOnClickListener(View.OnClickListener {
           doShowLetUsKnowDialog()
        })

        txtReadRating.setOnClickListener(View.OnClickListener {
            var bundle = bundleOf(AppConstant.BUSINESS_ID to mSelectedMealPlan?.planId)
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProviderDetails_to_fragmentReview, bundle)
        })

        btnTryNow.setOnClickListener(View.OnClickListener {
            var bundle = bundleOf(AppConstant.SELECTED_PLAN_OBJECT to mSelectedMealPlan,

                AppConstant.SELECTED_PLAN_OBJECT to mSelectedMealPlan, AppConstant.SELECTED_MEAL_PLAN_TRIAL to true,   AppConstant.BUSINESS_ID to mBusinessId)
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentProviderDetails_to_fragmentDeliveryDetail,bundle)
        })
    }

    private fun doShowLetUsKnowDialog() {
        val li = LayoutInflater.from(context)
        val promptsView: View = li.inflate(R.layout.dialog_letusknow, null)
        val alertDialogBuilder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        alertDialogBuilder.setView(promptsView)
        alertDialogBuilder.setCancelable(false)
        var txtQuery = promptsView.findViewById<EditText>(R.id.txtQuery)
        var btnClose = promptsView.findViewById<ImageView>(R.id.imgCloseLetUsKnowDialog)
        var btnSend = promptsView.findViewById<AppCompatButton>(R.id.btnSend)

        var dialog = alertDialogBuilder.create()

        btnClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        btnSend.setOnClickListener(View.OnClickListener {
            if(txtQuery.text.isNullOrBlank()) {
                txtQuery.error = "Please enter question"
            } else {
                mViewModel?.doSubmitQuery(SubmitQueryRequestModel(txtQuery.text.toString(),mBusinessId))
                dialog.dismiss()
            }
        })

        dialog.show()
    }

    private fun doSetProviderDetails(providerData: GetProviderDetailsData) {
        mProviderDetails = providerData
        val planList = ArrayList<String>()
        (activity as RootActivity).supportActionBar?.title = providerData.companyName
        rvPlanList.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        if (providerData.planData != null && providerData.planData!!.size > 0) {
            mMealPlanList = providerData.planData!!
            for (planDetail in providerData.planData!!) {
                planDetail.title?.let { planList.add(it) }
            }

            mSelectedMealPlan = providerData.planData!![0]
            mSelectedMealPlan.addressId = providerData.addressId
            mSelectedMealPlan.defaultUserAddress = providerData.defaultUserAddress
            mSelectedMealPlan.phoneNumber = providerData?.phoneNumber

            val adapter = mContext?.let {
                PlanListAdapter(it, planList, object : OnListItemClickListener {
                    override fun onListItemClick(position: Int) {
                        if (providerData.planData!!.size > position) {
                            mSelectedMealPlan = providerData.planData!![position]
                            mSelectedMealPlan.addressId = providerData.addressId
                            mSelectedMealPlan.phoneNumber = providerData?.phoneNumber
                            mSelectedMealPlan.defaultUserAddress = providerData.defaultUserAddress
                         //   mealId = mSelectedMealPlan.mealOptions
                            doSetTabDetails()
                        }
                    }
                }, 0)
            }
            rvPlanList.adapter = adapter

            doSetTabDetails()
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

    private fun doSetTabDetails() {
        doSetMealPlanData(mSelectedMealPlan)
        doSetInfoOrLocationData(0)
        deliveryDays.text = mSelectedMealPlan.deliveryDays
        if(mMenu != null) {
            if(mProviderDetails?.isFavourite != null && mProviderDetails?.isFavourite!!) {
                mMenu?.findItem(R.id.menu_fav)?.setIcon(R.drawable.ic_fav_selected)
            }
        }
        if (mSelectedMealPlan.sampleMenu == null || mSelectedMealPlan.sampleMenu!!.size <= 0) {
            imgViewSampleMenu.visibility = View.GONE
        } else {
            imgViewSampleMenu.visibility = View.VISIBLE
        }
        if (mSelectedMealPlan.isTrialPlanAvailable) {
            llTrialMealAvailable.visibility = View.VISIBLE
            cardNoTrialmeal.visibility = View.GONE
            txtTrialPlanData.text = mSelectedMealPlan.trialPlanDescription
        } else {
            cardNoTrialmeal.visibility = View.VISIBLE
            llTrialMealAvailable.visibility = View.GONE
        }
    }

    private fun doSetInfoOrLocationData(position: Int) {
        if (mSelectedMealPlan != null) {
            if (position == 0) {
                txtInfoDeliveryLocation.visibility = View.VISIBLE
                llNutriInfo.visibility = View.GONE
                txtInfoDeliveryLocation.text = mSelectedMealPlan.description
            } else {
                llNutriInfo.visibility = View.VISIBLE
                txtInfoDeliveryLocation.visibility = View.GONE
                txtPerMealNutri.text = mSelectedMealPlan.avgMeal
                txtPerDayNutri.text = mSelectedMealPlan.avgDay
            }
        }
    }

    private fun doSetMealPlanData(mealPlanData: MealPlanModel) {
        if (mealPlanData != null) {
            txtRating.text = mealPlanData.review
            if(!mealPlanData.review.isNullOrBlank()) rateBar.rating  = mealPlanData.review?.toFloat()!!
            txtCountReviews.text = String.format(getString(R.string.txt_no_of_reviews), mealPlanData.noOfReviews)
            txtStartingFrom.text = mealPlanData.startingFrom

            Glide.with(mContext)
                    .load(mealPlanData.featuredImage)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))
                    .into(imgSampleMenu)
            Glide.with(mContext)
                    .load(mProviderDetails?.companyLogo)
                    .centerCrop()
                    .into(imgCompanyLogoDetail)
        }
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
        if (mProviderDetails != null) {
            txtProviderName.text = mProviderDetails?.companyName
            txtProviderDesc.text = mProviderDetails?.description
            txtProviderLocation.text = mProviderDetails?.address
            Glide.with(mContext)
                    .load(mProviderDetails?.icon)
                    .circleCrop()
                    .into(imgLogo)
        }

        btnClose.setOnClickListener(View.OnClickListener { providerDialog.dismiss() })
    }
}
