package com.laka.ergou.mvp.invitationrecord.view.adapter

import android.support.v4.app.FragmentManager
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.ergou.mvp.base.view.adapter.BaseCustomFragmentPagerAdapter

/**
 * @Author:summer
 * @Date:2019/7/2
 * @Description:
 */
class InvitationRecordPageAdapter : BaseCustomFragmentPagerAdapter {

    private val mPageTitleList: ArrayList<String>

    constructor(fm: FragmentManager, fragmentList: ArrayList<BaseLazyLoadFragment>, title: ArrayList<String>) : super(fm, fragmentList) {
        this.mPageTitleList = title
    }

    override fun getPageTitle(position: Int): CharSequence {
        if (position >= 0 && position < mPageTitleList.size) {
            return mPageTitleList[position]
        }
        return ""
    }

}