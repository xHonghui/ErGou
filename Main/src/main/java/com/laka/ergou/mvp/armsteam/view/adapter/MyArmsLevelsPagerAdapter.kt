package com.laka.ergou.mvp.armsteam.view.adapter

import android.support.v4.app.FragmentManager
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.ergou.mvp.base.view.adapter.BaseCustomFragmentPagerAdapter

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:我的战友 -> viewpager 页面适配器（一级/二级/三级）
 */
class MyArmsLevelsPagerAdapter : BaseCustomFragmentPagerAdapter {

    private var mTitleList: ArrayList<String>

    constructor(fm: FragmentManager, title: ArrayList<String>, fragmentList: ArrayList<BaseLazyLoadFragment>) : super(fm, fragmentList) {
        this.mTitleList = title
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitleList[position]
    }

}