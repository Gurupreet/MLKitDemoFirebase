package com.guru.mlkitdemofirebase

import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.pager_image_layout.view.*

class ImagePagerAdapter(val images : ArrayList<Int>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var layout = LayoutInflater.from(container.context).inflate(R.layout.pager_image_layout, container, false)
        if (position == 0) {
            layout.pager_image.setBackgroundColor(ContextCompat.getColor(container.context, R.color.colorPrimary))
        } else {
            layout.pager_image.setBackgroundColor(ContextCompat.getColor(container.context, R.color.colorAccent))

        }
        container.addView(layout, 0)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}