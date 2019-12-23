package com.laka.ergou.mvp.teamaward.view.adapter

import android.support.v4.app.FragmentManager
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.ergou.mvp.base.view.adapter.BaseCustomFragmentPagerAdapter


class TearmAwardPagerAdapter : BaseCustomFragmentPagerAdapter {

    private var mTitleList: ArrayList<String>

    constructor(fm: FragmentManager, title: ArrayList<String>, fragmentList: ArrayList<BaseLazyLoadFragment>) : super(fm, fragmentList) {
        this.mTitleList = title
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitleList[position]
    }

}