package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.request.GetProvidersRequestModel
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.data.remote.model.response.FavoriteModel
import com.tugoapp.mobile.data.remote.model.response.ProviderModel
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.OnListItemClickListener
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.ui.home.adapters.BrowseByDietListAdapter
import com.tugoapp.mobile.ui.home.adapters.FavoritesListAdapter
import com.tugoapp.mobile.ui.home.adapters.SearchHomeListAdapter
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_browse_all_providers.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar1.view.*
import javax.inject.Inject

class FragmentFavorites : BaseFragment<HomeViewModel?>(){
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: HomeViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_favorites

    override val viewModel: HomeViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
            return mViewModel!!
        }
    override val screenTitle: String
        get() = ""

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniUI()

        initObservers()
    }

    private fun initObservers() {

        mViewModel?.mToastMessage?.observe(viewLifecycleOwner, Observer { CommonUtils.showSnakeBar(rootView!!,it)})

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

        mViewModel?.mFavoriteData?.observe(viewLifecycleOwner, Observer {
            if(it != null && it.size > 0) {
                rvFavorites.visibility = View.VISIBLE
                rvFavorites.layoutManager = LinearLayoutManager(mContext)

                emptyviewFavorites.visibility = View.GONE
                val data = ArrayList<FavoriteModel>()
                data.addAll(it)
                val adapter = mContext?.let {
                    FavoritesListAdapter(it, data, object : OnListItemClickListener {
                        override fun onListItemClick(position: Int) {
                            var bundle = bundleOf(AppConstant.SELECTED_PROVIDER_FOR_PROVIDER_DETAIL to data[position].businessId)
                            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentFavorites_to_fragmentProviderDetails, bundle)
                        }
                    })
                }
                rvFavorites.adapter = adapter
            } else {
                rvFavorites.visibility = View.GONE
                emptyviewFavorites.visibility = View.VISIBLE
            }
        })
    }

    private fun iniUI() {
        mContext = context

        mViewModel?.doLoadFavorites()
    }
}

