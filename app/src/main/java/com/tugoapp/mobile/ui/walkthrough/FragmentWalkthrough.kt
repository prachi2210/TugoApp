package com.tugoapp.mobile.ui.walkthrough

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.base.BaseFragment
import com.tugoapp.mobile.ui.base.BaseViewModel
import com.tugoapp.mobile.ui.base.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_walkthrough.*
import javax.inject.Inject

class FragmentWalkthrough : BaseFragment<BaseViewModel?>() {
    @JvmField
    @Inject
    var factory: ViewModelProviderFactory? = null
    private var mViewModel: BaseViewModel? = null
    var mContext: Context? = null

    override val layoutId: Int
        get() = R.layout.fragment_walkthrough

    override val viewModel: BaseViewModel
        get() {
            mViewModel = ViewModelProviders.of(this, factory).get(BaseViewModel::class.java)
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
    }

    private fun iniUI() {
        mContext = context

        val data: ArrayList<WalkthroughModel> = ArrayList()
        data.add(WalkthroughModel(getString(R.string.title_wt1), getString(R.string.subtitle_wt1), R.drawable.ic_wt1))
        data.add(WalkthroughModel(getString(R.string.title_wt2), getString(R.string.subtitle_wt2), R.drawable.ic_wt2))
        data.add(WalkthroughModel(getString(R.string.title_wt3), getString(R.string.subtitle_wt3), R.drawable.ic_wt3))

        pager.adapter = WalkthroughPagerAdpter(data)
        dotsIndicator.setViewPager(pager)

        pager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 0 || position == 1) {
                    btnWalkthrough.text = getString(R.string.btn_next)
                    btnSkip.visibility = View.VISIBLE
                } else {
                    btnSkip.visibility = View.GONE
                    btnWalkthrough.text = getString(R.string.btn_get_started)
                }
            }
        })

        btnSkip.setOnClickListener(View.OnClickListener {
            Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentWalkthrough_to_fragmentHome)
        })

        btnWalkthrough.setOnClickListener(View.OnClickListener {
            if (pager.currentItem == 2) {
                Navigation.findNavController(rootView!!).navigate(R.id.action_fragmentWalkthrough_to_fragmentHome)
            } else {
                pager.setCurrentItem(pager.currentItem + 1)
            }
        })
    }
}