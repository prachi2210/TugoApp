package com.tugoapp.mobile.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_sample_menu.*
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import javax.inject.Inject


class FragmentSampleMenu : BaseFragment<HomeViewModel?>() {
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
        val list = mutableListOf<CarouselItem>()

        list.add(
                CarouselItem(
                        imageUrl = "https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080"
                )
        )
        list.add(
                CarouselItem(
                        imageUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080"
                )
        )

        imgSampleMenu.addData(list)


    }
}

