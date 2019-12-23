package com.laka.ergou.mvp.armsteam.view.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.R
import com.laka.ergou.common.util.ui.TabUtils
import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant
import com.laka.ergou.mvp.armsteam.model.event.MyArmsLevelsEvent
import com.laka.ergou.mvp.armsteam.view.adapter.MyArmsLevelsPagerAdapter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * @Author:summer
 * @Date:2019/5/24
 * @Description:我的战队page fragment
 */
class MyArmsPageFragment : BaseLazyLoadFragment() {

    private val mPagetitle: ArrayList<String> = MyArmsLevelsConstant.MY_LOWER_LEVELS_TITLE
    private val mFragmentList: ArrayList<BaseLazyLoadFragment> = ArrayList()
    //订单来源（1：一级战友  2:二级战友   3: 三级战友）
    private var mPageType: Int = 1
    private var mTabOrder: TabLayout? = null
    private var mViewPager: ViewPager? = null
    //页面适配器
    private lateinit var mMyLowerLevelsPagerAdapter: MyArmsLevelsPagerAdapter

    companion object {
        fun getInstance(pageType: Int): MyArmsPageFragment {
            val bundle = Bundle()
            bundle.putInt(MyArmsLevelsConstant.PAGE_TYPE, pageType)
            val fragment = MyArmsPageFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun setContentView(): Int {
        return R.layout.fragment_page_my_lower
    }

    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            mPageType = arguments.getInt(MyArmsLevelsConstant.PAGE_TYPE, 1)
        }
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        mTabOrder = rootView?.findViewById(R.id.tab_order_type)
        mViewPager = rootView?.findViewById(R.id.view_pager_my_lower)
        initViewPager()
    }

    private fun initViewPager() {
        mFragmentList.add(MyArmsListFragment.getInstance(mPageType, MyArmsLevelsConstant.MY_LOWER_PAGE_FIRST))
        mFragmentList.add(MyArmsListFragment.getInstance(mPageType, MyArmsLevelsConstant.MY_LOWER_PAGE_SECOND))
        //只显示一级战友和二级战友，军团战友暂时不显示
        //mFragmentList.add(MyArmsListFragment.getInstance(mPageType, MyArmsLevelsConstant.MY_LOWER_PAGE_THIRD))
    }

    override fun initDataLazy() {
        mTabOrder?.setupWithViewPager(mViewPager)
        mMyLowerLevelsPagerAdapter = MyArmsLevelsPagerAdapter(childFragmentManager, mPagetitle, mFragmentList)
        mViewPager?.adapter = mMyLowerLevelsPagerAdapter
        mViewPager?.offscreenPageLimit = 2  //同时加载两个页面
        TabUtils.setTabLayoutIndicator(mTabOrder)
    }

    override fun initEvent() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMyLowerLevelsEvent(event: MyArmsLevelsEvent) {
        //战友等级，1:一级战友  2：二级战友  3：三级战友
        if (event.pageType != mPageType) return
        when (event.eventType) {
            MyArmsLevelsConstant.MY_LOWER_PAGE_FIRST -> {
                mTabOrder?.getTabAt(0)?.text = "一级战友\n（${event.data}）"
            }
            MyArmsLevelsConstant.MY_LOWER_PAGE_SECOND -> {
                mTabOrder?.getTabAt(1)?.text = "二级战友\n（${event.data}）"
            }
            MyArmsLevelsConstant.MY_LOWER_PAGE_THIRD -> {
                mTabOrder?.getTabAt(2)?.text = "军团战友\n（${event.data}）"
            }
        }
    }
}