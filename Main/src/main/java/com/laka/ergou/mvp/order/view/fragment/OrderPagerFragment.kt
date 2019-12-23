package com.laka.ergou.mvp.order.view.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.R
import com.laka.ergou.common.util.ui.TabUtils
import com.laka.ergou.mvp.order.constant.MyOrderConstant
import com.laka.ergou.mvp.order.view.adapter.MyOrderListPagerAdapter
import java.util.*

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:订单页面fragment
 */
class OrderPagerFragment : BaseLazyLoadFragment() {

    private val mPagetitle: ArrayList<String> = MyOrderConstant.MY_ORDER_PAGE_TITLE
    private val mFragmentList: ArrayList<BaseLazyLoadFragment> = ArrayList()
    private var myOrderPagerAdapter: MyOrderListPagerAdapter? = null
    private var mTabOrder: TabLayout? = null
    private var mViewPager: ViewPager? = null
    //订单来源（1：app  2:微信）
    private var mSourceType: Int = 1

    companion object {
        fun getInstance(sourceType: Int): OrderPagerFragment {
            val bundle = Bundle()
            bundle.putInt(MyOrderConstant.SOURCE_TYPE, sourceType)
            val fragment = OrderPagerFragment()
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
            mSourceType = arguments.getInt(MyOrderConstant.SOURCE_TYPE, 1)
        }
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        mTabOrder = rootView?.findViewById(R.id.tab_order_type)
        mViewPager = rootView?.findViewById(R.id.view_pager_order)
        initViewPager()
    }

    private fun initViewPager() {
        mFragmentList.add(MyOrderListFragment.newInstance(MyOrderConstant.ORDER_STATUS_ALL, mSourceType))
        mFragmentList.add(MyOrderListFragment.newInstance(MyOrderConstant.ORDER_STATUS_PAID, mSourceType))
        mFragmentList.add(MyOrderListFragment.newInstance(MyOrderConstant.ORDER_STATUS_SETTLEMENT, mSourceType))
        mFragmentList.add(MyOrderListFragment.newInstance(MyOrderConstant.ORDER_STATUS_REFUND, mSourceType))
        mTabOrder?.setupWithViewPager(mViewPager)
        myOrderPagerAdapter = MyOrderListPagerAdapter(childFragmentManager, mPagetitle, mFragmentList)
        mViewPager?.adapter = myOrderPagerAdapter
        TabUtils.setTabLayoutIndicator(mTabOrder)
    }

    override fun initDataLazy() {

    }

    override fun initEvent() {
    }
}