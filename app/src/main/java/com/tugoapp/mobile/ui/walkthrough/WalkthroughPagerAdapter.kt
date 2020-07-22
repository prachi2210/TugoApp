package com.tugoapp.mobile.ui.walkthrough

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.tugoapp.mobile.R
import com.tugoapp.mobile.ui.RootActivity
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.layout_walkthrough.view.*

class WalkthroughPagerAdpter(val data: ArrayList<WalkthroughModel>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object` as View

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val ctx = container.context
        val view = (ctx as RootActivity).layoutInflater.inflate(R.layout.layout_walkthrough, container, false)
        view.walkthroughImage.setImageResource(data[position].image)
        view.walkthroughTitle.setText(data[position].title)
        view.walkthroughSubTitle.setText(data[position].subTitle)
        container.addView(view)
        return view
    }

    override fun getCount(): Int = data.size

    override fun getItemPosition(`object`: Any): Int = super.getItemPosition(`object`)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}