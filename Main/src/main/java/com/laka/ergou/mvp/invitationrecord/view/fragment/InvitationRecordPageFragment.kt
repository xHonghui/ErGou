package com.laka.ergou.mvp.invitationrecord.view.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.R
import com.laka.ergou.common.util.ui.TabUtils
import com.laka.ergou.mvp.invitationrecord.constant.InvitationRecordConstant
import com.laka.ergou.mvp.invitationrecord.view.adapter.InvitationRecordPageAdapter
import java.util.*

/**
 * @Author:summer
 * @Date:2019/7/2
 * @Description:
 */
class InvitationRecordPageFragment : BaseLazyLoadFragment() {

    private val mPageTitle: ArrayList<String> = InvitationRecordConstant.INVITATION_RECORD_PAGE_TITLE
    private val mFragmentList: ArrayList<BaseLazyLoadFragment> = ArrayList()
    private var mInvitationPagerAdapter: InvitationRecordPageAdapter? = null
    private var mTabInvitationRecord: TabLayout? = null
    private var mViewPager: ViewPager? = null
    //订单来源（1：app  2:微信）
    private var mSourceType: Int = 1

    companion object {
        fun getInstance(sourceType: Int): InvitationRecordPageFragment {
            val bundle = Bundle()
            bundle.putInt(InvitationRecordConstant.SOURCE_TYPE, sourceType)
            val fragment = InvitationRecordPageFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun setContentView(): Int {
        return R.layout.fragment_invitation_record
    }

    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            mSourceType = arguments.getInt(InvitationRecordConstant.SOURCE_TYPE)
        }
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        mTabInvitationRecord = rootView?.findViewById(R.id.tab_invitation_type)
        mViewPager = rootView?.findViewById(R.id.view_pager_invitation)
        initViewPager()
    }

    private fun initViewPager() {
        mFragmentList.add(InvitationRecordListFragment.getInstance(InvitationRecordConstant.INVITATION_NOT_ACTIVE, mSourceType))
        mFragmentList.add(InvitationRecordListFragment.getInstance(InvitationRecordConstant.INVITATION_ACTIVED, mSourceType))
    }

    override fun initDataLazy() {
        mTabInvitationRecord?.setupWithViewPager(mViewPager)
        mInvitationPagerAdapter = InvitationRecordPageAdapter(childFragmentManager, mFragmentList, mPageTitle)
        mViewPager?.adapter = mInvitationPagerAdapter
        TabUtils.setTabLayoutIndicator(mTabInvitationRecord)
    }

    override fun initEvent() {

    }
}