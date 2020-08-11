package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.tugoapp.mobile.R
import com.tugoapp.mobile.data.remote.model.response.CategoryDetailModel
import com.tugoapp.mobile.data.remote.model.response.SampleMenu
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import com.tugoapp.mobile.utils.AppConstant
import com.tugoapp.mobile.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_sample_menu.*
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import java.util.ArrayList
import javax.inject.Inject


class FragmentSampleMenu : BaseFragment<HomeViewModel?>() {
    private var mSampleMenuList : ArrayList<SampleMenu>? = null

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
        if(mSampleMenuList == null) {
            CommonUtils.showSnakeBar(rootView,getString(R.string.txt_err_no_pref_value))
            return
        }

        val list = mutableListOf<CarouselItem>()

        for(sample in mSampleMenuList!!) {
            list.add(
                    CarouselItem(
                            imageUrl = sample.description,
                            caption = sample.title
                    )
            )
        }
        imgSampleMenu.addData(list)
    }
}

