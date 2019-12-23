package com.laka.ergou.mvp.base.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:ViewPager 的基类adapter
 */
open class BaseCustomFragmentPagerAdapter : FragmentPagerAdapter {

    private var mFragmentList: ArrayList<BaseLazyLoadFragment>
    private var mFragmentManager: FragmentManager

    constructor(fm: FragmentManager, fragmentList: ArrayList<BaseLazyLoadFragment>) : super(fm) {
        this.mFragmentManager = fm
        this.mFragmentList = fragmentList
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        this.mFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        return fragment
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
        val fragment = mFragmentList[position]
        this.mFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss()
    }
}