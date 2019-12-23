package com.laka.ergou.mvp.teamaward.view.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.R
import com.laka.ergou.common.util.ui.TabUtils
import com.laka.ergou.mvp.teamaward.constant.TearmAwardConstant
import com.laka.ergou.mvp.teamaward.view.adapter.TearmAwardPagerAdapter
import java.util.*

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:订单页面fragment
 */
class TearmAwardPagerFragment : BaseLazyLoadFragment() {

    private val mPagetitle: ArrayList<String> = TearmAwardConstant.TEARM_AWARD_PAGE_TITLE
    private val mFragmentList: ArrayList<BaseLazyLoadFragment> = ArrayList()
    private var myOrderPagerAdapter: TearmAwardPagerAdapter? = null
    private var mTabOrder: TabLayout? = null
    private var mViewPager: ViewPager? = null
    //订单来源（1：app  2:微信）
    private var mSourceType: Int = 1

    companion object {
        fun getInstance(sourceType: Int): TearmAwardPagerFragment {
            val bundle = Bundle()
            bundle.putInt(TearmAwardConstant.PAGE_TYPE, sourceType)
            val fragment = TearmAwardPagerFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_app_order
    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            mSourceType = arguments.getInt(TearmAwardConstant.PAGE_TYPE, 1)
        }
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        mTabOrder = rootView?.findViewById(R.id.tab_order_type)
        mViewPager = rootView?.findViewById(R.id.view_pager_order)
        initViewPager()
    }

    private fun initViewPager() {
        mFragmentList.add(TearmAwardFragment.newInstance(mSourceType,TearmAwardConstant.TEARM_AWARD_ALL_TYPE))
        mFragmentList.add(TearmAwardFragment.newInstance(mSourceType,TearmAwardConstant.TEARM_AWARD_PAID_TYPE))
        mFragmentList.add(TearmAwardFragment.newInstance(mSourceType,TearmAwardConstant.Tearm_Award_SETTLEMENT_TYPE))
        mFragmentList.add(TearmAwardFragment.newInstance(mSourceType,TearmAwardConstant.Tearm_Award_REFUND_TYPE))
    }

    override fun initDataLazy() {
        mTabOrder?.setupWithViewPager(mViewPager)
        myOrderPagerAdapter = TearmAwardPagerAdapter(childFragmentManager, mPagetitle, mFragmentList)
        mViewPager?.adapter = myOrderPagerAdapter
        TabUtils.setTabLayoutIndicator(mTabOrder)
    }

    override fun initEvent() {
    }
}