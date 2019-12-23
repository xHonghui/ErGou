package com.laka.ergou.mvp.order.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.ergou.mvp.base.view.adapter.BaseCustomFragmentPagerAdapter
import com.laka.ergou.mvp.order.view.fragment.BaseOrderListFragment

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:我的订单 -> viewpager 页面适配器（已付款/已结算/已退货）
 */
class MyOrderListPagerAdapter : BaseCustomFragmentPagerAdapter {

    private var mTitleList: ArrayList<String>

    constructor(fm: FragmentManager, title: ArrayList<String>, fragmentList: ArrayList<BaseLazyLoadFragment>) : super(fm, fragmentList) {
        this.mTitleList = title
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitleList[position]
    }

}