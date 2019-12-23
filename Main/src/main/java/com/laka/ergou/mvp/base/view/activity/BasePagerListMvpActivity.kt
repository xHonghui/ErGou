package com.laka.ergou.mvp.base.view.activity

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.view.PagerAdapter
import android.widget.LinearLayout
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R
import com.laka.ergou.common.widget.PagerListTab
import kotlinx.android.synthetic.main.activity_base_pager_list.*

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:
 */
abstract class BasePagerListMvpActivity<D> : BaseMvpActivity<D>() {

    lateinit var mViewExtendOne: LinearLayout

    override fun setContentView(): Int {
        return R.layout.activity_base_pager_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    /** onCreate() 执行，确保其他变量已经初始化完成*/
    fun init() {
        title_bar?.setLeftIcon(R.drawable.nav_btn_back_n)?.setTitleTextColor(R.color.black)
        val pagerAdapter = getPagerAdapter()
        pagerAdapter?.let {
            view_pager_list.adapter = pagerAdapter
        }
        pl_tab.selectTabItem(PagerListTab.LEFT_ITEM)
        pl_tab.setItemClickListener {
            if (it == PagerListTab.LEFT_ITEM) {
                val pagerCount = view_pager_list.adapter.count
                if (pagerCount > 0) {
                    view_pager_list.setCurrentItem(0, false)//第一页
                } else {
                    LogUtils.info("current viewPager no data!")
                }
            } else if (it == PagerListTab.RIGHT_ITEM) {
                val pagerCount = view_pager_list.adapter.count
                if (pagerCount > 1) {
                    view_pager_list.setCurrentItem(1, false)//第二页
                } else {
                    LogUtils.info("current viewPager only has $pagerCount pager")
                }
            }
        }
    }

    @CallSuper
    override fun initViews() {
        mViewExtendOne = view_extend_one
    }

    fun setLeftTabText(txt: String) {
        pl_tab.setLeftTabText(txt)
    }

    fun setRightTabText(txt: String) {
        pl_tab.setRightTabStr(txt)
    }

    //子类提供适配器，该viewPager固定只有两个pager
    abstract fun getPagerAdapter(): PagerAdapter

}