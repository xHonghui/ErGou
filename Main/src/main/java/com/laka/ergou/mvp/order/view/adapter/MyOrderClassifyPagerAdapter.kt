package com.laka.ergou.mvp.order.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.ergou.mvp.order.view.fragment.BaseOrderListFragment

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:我的订单 -> 分类 viewpager 页面适配器（app 订单/wechat 订单）
 */
class MyOrderClassifyPagerAdapter : FragmentPagerAdapter {

    private var mFragmentList: ArrayList<BaseLazyLoadFragment>
    private var mFragmentManager: FragmentManager

    constructor(fm: FragmentManager,fragmentList: ArrayList<BaseLazyLoadFragment>) : super(fm) {
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